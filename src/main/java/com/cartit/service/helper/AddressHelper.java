package com.cartit.service.helper;

import java.util.List;

import com.cartit.entity.Address;
import com.cartit.entity.User;

public interface AddressHelper {

    Address getAddress(Long addressId);

    List<Address> getMyAddresses();

    Address getDefaultAddress();

    Address save(Address address);

    void delete(Address address);

    boolean hasAnyAddress();
    
    void assignDefaultAddress(User user);

    void clearDefaultAddress(User user);
}