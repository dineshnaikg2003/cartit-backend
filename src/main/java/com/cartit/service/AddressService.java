package com.cartit.service;

import java.util.List;

import com.cartit.dto.request.AddressRequest;
import com.cartit.dto.response.AddressResponse;

public interface AddressService {

    AddressResponse createAddress(AddressRequest request);

    List<AddressResponse> getMyAddresses();

    AddressResponse getAddressById(Long addressId);

    AddressResponse updateAddress(
            Long addressId,
            AddressRequest request);

    AddressResponse setDefaultAddress(Long addressId);

    void deleteAddress(Long addressId);
}