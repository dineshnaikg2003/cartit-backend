package com.cartit.service.builder;

import org.springframework.data.domain.Page;

import com.cartit.dto.response.PageResponse;

public interface PageResponseBuilder {

    <T> PageResponse<T> build(Page<T> page);

}