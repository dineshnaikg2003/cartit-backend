package com.cartit.service;

import com.cartit.dto.request.CheckoutRequest;
import com.cartit.dto.response.OrderResponse;

public interface CheckoutService {

    OrderResponse checkout(CheckoutRequest request);

}