package com.example.daydreamer.specification;

import com.example.daydreamer.entity.Review;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecification {

    public static Specification<Review> hasBookingId(String bookingId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("booking").get("id"), bookingId);
    }

    public static Specification<Review> hasStudioId(String studioId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("studio").get("id"), studioId);
    }

    public static Specification<Review> hasRating(Integer rating) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("rating"), rating);
    }

    public static Specification<Review> hasStatus(String status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }
}