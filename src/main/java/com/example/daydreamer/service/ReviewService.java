package com.example.daydreamer.service;

import com.example.daydreamer.entity.Review;
import com.example.daydreamer.entity.Booking;
import com.example.daydreamer.entity.Studio;
import com.example.daydreamer.model.review.ReviewResponse;
import com.example.daydreamer.model.review.ReviewRequest;
import com.example.daydreamer.repository.BookingRepository;
import com.example.daydreamer.repository.ReviewRepository;
import com.example.daydreamer.repository.StudioRepository;
import com.example.daydreamer.specification.GenericSpecification;
import com.example.daydreamer.utils.CustomValidationException;
import com.example.daydreamer.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final Logger LOGGER = LoggerFactory.getLogger(ReviewService.class);
    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final StudioRepository studioRepository;

    public List<ReviewResponse> searchReviews(String bookingId, String studioId, Integer rating, String status, int page, int limit) {
        LOGGER.info("Searching reviews with dynamic criteria");

        Specification<Review> spec = buildSpecification(bookingId, studioId, rating, status);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Review> reviews = reviewRepository.findAll(spec, pageable);

        return reviews.stream()
                .map(this::reviewResponseGenerator)
                .collect(Collectors.toList());
    }

    private Specification<Review> buildSpecification(String bookingId, String studioId, Integer rating, String status) {
        Specification<Review> spec = Specification.where(null);

        spec = GenericSpecification.addSpecification(spec, studioId, "studio.id", "equal");
        spec = GenericSpecification.addSpecification(spec, bookingId, "booking.id", "equal");
        spec = GenericSpecification.addSpecification(spec, rating, "rating", "equal");
        spec = GenericSpecification.addSpecification(spec, status, "status", "equal");

        return spec;
    }

    public List<ReviewResponse> findAll(int page, int limit) {
        LOGGER.info("Find all reviews");
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Review> reviews = reviewRepository.findAll(pageable);
        if (reviews.isEmpty()) {
            LOGGER.warn("No reviews were found!");
        }

        return reviews.stream()
                .map(this::reviewResponseGenerator)
                .collect(Collectors.toList());
    }

    public ReviewResponse findById(String id) {
        LOGGER.info("Find review with id " + id);
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isEmpty()) {
            LOGGER.warn("No review was found!");
            return null;
        }
        return review.map(this::reviewResponseGenerator).get();
    }

    public ReviewResponse save(ReviewRequest reviewRequest) {
        Review review;
        Optional<Booking> booking = bookingRepository.findById(reviewRequest.getBookingId());
        Optional<Studio> studio = studioRepository.findById(reviewRequest.getStudioId());
        if (booking.isEmpty() || studio.isEmpty()) {
            throw new CustomValidationException(List.of("No booking or studio was found!"));
        }

        if (reviewRequest.getId() != null) {
            LOGGER.info("Update review with id " + reviewRequest.getId());
            checkExist(reviewRequest.getId());
            review = reviewRepository.findById(reviewRequest.getId()).get();
        } else {
            LOGGER.info("Create new review");
            review = new Review();
        }

        review.setBooking(booking.get());
        review.setContent(reviewRequest.getContent());
        review.setRating(reviewRequest.getRating());
        review.setStatus(reviewRequest.getStatus());

        reviewRepository.save(review);

        return reviewResponseGenerator(review);
    }

    public void delete(String id) {
        if (id != null) {
            LOGGER.info("Delete review with id " + id);
            checkExist(id);
            Review review = reviewRepository.findById(id).get();
            reviewRepository.delete(review);
        }
    }

    private ReviewResponse reviewResponseGenerator(Review review) {
        return ResponseUtil.generateResponse(review, ReviewResponse.class);
    }

    private void checkExist(String id) {
        if (reviewRepository.findById(id).isEmpty()) {
            LOGGER.error("No review was found!");
            throw new CustomValidationException(List.of("No review was found!"));
        }
    }
    
}
