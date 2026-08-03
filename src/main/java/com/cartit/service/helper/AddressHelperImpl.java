package com.cartit.service.helper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cartit.entity.Address;
import com.cartit.entity.User;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.repository.AddressRepository;
import com.cartit.security.CurrentUserService;

@Service
public class AddressHelperImpl implements AddressHelper {

    private final AddressRepository addressRepository;
    private final CurrentUserService currentUserService;

    public AddressHelperImpl(
            AddressRepository addressRepository,
            CurrentUserService currentUserService) {

        this.addressRepository = addressRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public Address getAddress(Long addressId) {

        User user = currentUserService.getCurrentUser();

        return addressRepository
                .findByIdAndUserIdAndActiveTrue(
                        addressId,
                        user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found"));
    }

    @Override
    public List<Address> getMyAddresses() {

        User user = currentUserService.getCurrentUser();

        return addressRepository
                .findByUserIdAndActiveTrueOrderByDefaultAddressDescCreatedAtDesc(
                        user.getId());
    }

    @Override
    public Address getDefaultAddress() {

        User user = currentUserService.getCurrentUser();

        return addressRepository
                .findByUserIdAndDefaultAddressTrueAndActiveTrue(
                        user.getId())
                .orElse(null);
    }

    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public void delete(Address address) {
        addressRepository.delete(address);
    }

    @Override
    public boolean hasAnyAddress() {

        User user = currentUserService.getCurrentUser();

        return addressRepository.existsByUserIdAndActiveTrue(
                user.getId());
    }
    
    @Override
    public void assignDefaultAddress(User user) {

        addressRepository
                .findFirstByUserIdAndActiveTrueOrderByCreatedAtDesc(user.getId())
                .ifPresent(address -> {

                    address.setDefaultAddress(true);

                    addressRepository.save(address);
                });
    }

    @Override
    public void clearDefaultAddress(User user) {

        addressRepository
                .findByUserIdAndDefaultAddressTrueAndActiveTrue(user.getId())
                .ifPresent(address -> {

                    address.setDefaultAddress(false);

                    addressRepository.save(address);
                });
    }
}