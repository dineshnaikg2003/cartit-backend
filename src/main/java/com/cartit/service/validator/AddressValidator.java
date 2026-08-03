package com.cartit.service.validator;

import org.springframework.stereotype.Component;

import com.cartit.dto.request.AddressRequest;
import com.cartit.exception.BadRequestException;

@Component
public class AddressValidator {

    public void validate(AddressRequest request) {

        if (request.getPhoneNumber().equals(
                request.getAlternatePhoneNumber())) {

            throw new BadRequestException(
                    "Phone number and alternate phone number cannot be the same.");
        }
    }
}