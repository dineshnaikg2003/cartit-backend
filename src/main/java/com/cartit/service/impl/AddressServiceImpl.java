package com.cartit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.dto.request.AddressRequest;
import com.cartit.dto.response.AddressResponse;
import com.cartit.entity.Address;
import com.cartit.entity.User;
import com.cartit.mapper.AddressMapper;
import com.cartit.security.CurrentUserService;
import com.cartit.service.AddressService;
import com.cartit.service.builder.AddressResponseBuilder;
import com.cartit.service.helper.AddressHelper;
import com.cartit.service.validator.AddressValidator;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressHelper addressHelper;
    private final AddressValidator addressValidator;
    private final AddressResponseBuilder addressResponseBuilder;
    private final CurrentUserService currentUserService;

    public AddressServiceImpl(
            AddressHelper addressHelper,
            AddressValidator addressValidator,
            AddressResponseBuilder addressResponseBuilder,
            CurrentUserService currentUserService) {

        this.addressHelper = addressHelper;
        this.addressValidator = addressValidator;
        this.addressResponseBuilder = addressResponseBuilder;
        this.currentUserService = currentUserService;
    }

    @Override
    public AddressResponse createAddress(AddressRequest request) {

        addressValidator.validate(request);

        User user = currentUserService.getCurrentUser();

        Address address = AddressMapper.toEntity(request);

        address.setUser(user);

        if (!addressHelper.hasAnyAddress()) {
            address.setDefaultAddress(true);
        } else if (Boolean.TRUE.equals(request.getDefaultAddress())) {
            addressHelper.clearDefaultAddress(user);
            address.setDefaultAddress(true);
        }

        Address savedAddress = addressHelper.save(address);

        return addressResponseBuilder.build(savedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getMyAddresses() {

        return addressResponseBuilder.build(
                addressHelper.getMyAddresses());
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getAddressById(Long addressId) {

        return addressResponseBuilder.build(
                addressHelper.getAddress(addressId));
    }

    @Override
    public AddressResponse updateAddress(
            Long addressId,
            AddressRequest request) {

        addressValidator.validate(request);

        Address address = addressHelper.getAddress(addressId);

        AddressMapper.updateEntity(address, request);

        if (Boolean.TRUE.equals(request.getDefaultAddress())
                && !Boolean.TRUE.equals(address.getDefaultAddress())) {

            User user = currentUserService.getCurrentUser();

            addressHelper.clearDefaultAddress(user);

            address.setDefaultAddress(true);
        }

        Address updatedAddress = addressHelper.save(address);

        return addressResponseBuilder.build(updatedAddress);
    }

    @Override
    public AddressResponse setDefaultAddress(Long addressId) {

        User user = currentUserService.getCurrentUser();

        addressHelper.clearDefaultAddress(user);

        Address address = addressHelper.getAddress(addressId);

        address.setDefaultAddress(true);

        Address updatedAddress = addressHelper.save(address);

        return addressResponseBuilder.build(updatedAddress);
    }

    @Override
    public void deleteAddress(Long addressId) {

        User user = currentUserService.getCurrentUser();

        Address address = addressHelper.getAddress(addressId);

        boolean wasDefault = Boolean.TRUE.equals(
                address.getDefaultAddress());

        addressHelper.delete(address);

        if (wasDefault) {
            addressHelper.assignDefaultAddress(user);
        }
    }
}