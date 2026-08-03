package com.cartit.service.builder;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cartit.dto.response.AddressResponse;
import com.cartit.entity.Address;
import com.cartit.mapper.AddressMapper;

@Component
public class AddressResponseBuilder {

    public AddressResponse build(Address address) {
        return AddressMapper.toResponse(address);
    }

    public List<AddressResponse> build(List<Address> addresses) {
        return addresses.stream()
                .map(AddressMapper::toResponse)
                .toList();
    }
}