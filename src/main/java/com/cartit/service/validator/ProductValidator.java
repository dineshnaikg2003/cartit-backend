package com.cartit.service.validator;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.cartit.dto.request.ProductSearchRequest;
import com.cartit.exception.BadRequestException;

@Component
public class ProductValidator {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "name",
            "sellingPrice",
            "createdAt",
            "averageRating");

    public void validateSearch(ProductSearchRequest request) {

        if (!ALLOWED_SORT_FIELDS.contains(request.getSortBy())) {

            throw new BadRequestException(
                    "Invalid sort field.");
        }

        String direction = request.getDirection().toLowerCase();

        if (!direction.equals("asc")
                && !direction.equals("desc")) {

            throw new BadRequestException(
                    "Sort direction must be asc or desc.");
        }

        if (request.getPage() < 0) {

            throw new BadRequestException(
                    "Page cannot be negative.");
        }

        if (request.getSize() <= 0) {

            throw new BadRequestException(
                    "Size must be greater than zero.");
        }

        if (request.getMinPrice() != null
                && request.getMaxPrice() != null
                && request.getMinPrice().compareTo(request.getMaxPrice()) > 0) {

            throw new BadRequestException(
                    "Minimum price cannot be greater than maximum price.");
        }
    }
}