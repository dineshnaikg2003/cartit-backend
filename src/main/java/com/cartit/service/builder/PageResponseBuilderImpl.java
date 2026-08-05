package com.cartit.service.builder;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.cartit.dto.response.PageResponse;

@Component
public class PageResponseBuilderImpl implements PageResponseBuilder {

    @Override
    public <T> PageResponse<T> build(Page<T> page) {

        PageResponse<T> response = new PageResponse<>();

        response.setContent(page.getContent());
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());

        return response;
    }
}