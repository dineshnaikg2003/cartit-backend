package com.cartit.mapper;

import com.cartit.dto.request.AddressRequest;
import com.cartit.dto.response.AddressResponse;
import com.cartit.entity.Address;

public final class AddressMapper {

    private AddressMapper() {
    }

    public static Address toEntity(AddressRequest request) {

        Address address = new Address();

        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setAlternatePhoneNumber(request.getAlternatePhoneNumber());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setLandmark(request.getLandmark());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setAddressType(request.getAddressType());

        if (request.getDefaultAddress() != null) {
            address.setDefaultAddress(request.getDefaultAddress());
        }

        return address;
    }

    public static AddressResponse toResponse(Address address) {

        return new AddressResponse(
                address.getId(),
                address.getFullName(),
                address.getPhoneNumber(),
                address.getAlternatePhoneNumber(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getLandmark(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getPostalCode(),
                address.getAddressType(),
                address.getDefaultAddress(),
                address.getActive()
        );
    }

    public static void updateEntity(
            Address address,
            AddressRequest request) {

        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setAlternatePhoneNumber(request.getAlternatePhoneNumber());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setLandmark(request.getLandmark());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setAddressType(request.getAddressType());
    }
}