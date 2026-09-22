package com.cartit.service.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.cartit.dto.request.ProductSearchRequest;
import com.cartit.entity.Product;

import jakarta.persistence.criteria.Predicate;

public class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<Product> search(
            ProductSearchRequest request) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

         // Active Filter
            if (request.getActive() != null) {

                predicates.add(
                        cb.equal(
                                root.get("active"),
                                request.getActive()));
            }

            // Keyword Search
            if (request.getKeyword() != null
                    && !request.getKeyword().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + request.getKeyword().toLowerCase() + "%"));
            }

            // Category Filter
            if (request.getCategoryId() != null) {

                predicates.add(
                        cb.equal(
                                root.get("category").get("id"),
                                request.getCategoryId()));
            }

            // Brand Filter
            if (request.getBrandId() != null) {

                predicates.add(
                        cb.equal(
                                root.get("brand").get("id"),
                                request.getBrandId()));
            }

            // Featured Filter
            if (request.getFeatured() != null) {

                predicates.add(
                        cb.equal(
                                root.get("featured"),
                                request.getFeatured()));
            }

            // Minimum Price
            if (request.getMinPrice() != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("sellingPrice"),
                                request.getMinPrice()));
            }

            // Maximum Price
            if (request.getMaxPrice() != null) {

                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("sellingPrice"),
                                request.getMaxPrice()));
            }

            return cb.and(
                    predicates.toArray(new Predicate[0]));
        };
    }
}