package com.example.daydreamer.service;

import com.example.daydreamer.entity.Booking;
import com.example.daydreamer.entity.Review;
import com.example.daydreamer.entity.Studio;
import com.example.daydreamer.model.review.ReviewRequest;
import com.example.daydreamer.model.review.ReviewResponse;
import com.example.daydreamer.repository.BookingRepository;
import com.example.daydreamer.repository.ReviewRepository;
import com.example.daydreamer.repository.StudioRepository;
import com.example.daydreamer.specification.ReviewSpecification;
import com.example.daydreamer.utils.CustomValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final BookingRepository orderRepository;
    private final StudioRepository studioRepository;

    public List<ReviewResponse> searchReviews(String bookingId, String studioId, Integer rating, String status) {
        LOGGER.info("Searching reviews with dynamic criteria");

        Specification<Review> spec = Specification.where(null);

        if (bookingId != null && !bookingId.isEmpty()) {
            spec = spec.and(ReviewSpecification.hasBookingId(bookingId));
        }
        if (studioId != null && !studioId.isEmpty()) {
            spec = spec.and(ReviewSpecification.hasStudioId(studioId));
        }
        if (rating != null) {
            spec = spec.and(ReviewSpecification.hasRating(rating));
        }
        if (status != null && !status.isEmpty()) {
            spec = spec.and(ReviewSpecification.hasStatus(status));
        }

        List<Review> reviews = reviewRepository.findAll(spec);

        return reviews.stream()
                .map(this::reviewResponseGenerator)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> findAll() {
        LOGGER.info("Find all order details");
        List<Review> reviews = reviewRepository.findAll();
        if (reviews.isEmpty()) {
            LOGGER.warn("No order details were found!");
        }

        return reviews.stream()
                .map(this::reviewResponseGenerator)
                .collect(Collectors.toList());
    }

    public ReviewResponse findById(String id) {
        LOGGER.info("Find order detail with id " + id);
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isEmpty()) {
            LOGGER.warn("No order detail was found!");
            return null;
        }
        return review.map(this::reviewResponseGenerator).get();
    }

    public ReviewResponse save(ReviewRequest reviewRequest) {
        Review review;
        Optional<Booking> booking = orderRepository.findById(reviewRequest.getBookingId());
        Optional<Studio> studio = studioRepository.findById(reviewRequest.getStudioId());
        if (booking.isEmpty() || studio.isEmpty()) {
            throw new CustomValidationException(List.of("No booking or studio was found!"));
        }

        if (reviewRequest.getId() != null) {
            LOGGER.info("Update order detail with id " + reviewRequest.getId());
            checkExist(reviewRequest.getId());
            review = reviewRepository.findById(reviewRequest.getId()).get();
        } else {
            LOGGER.info("Create new order detail");
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
            LOGGER.info("Delete order detail with id " + id);
            checkExist(id);
            Review review = reviewRepository.findById(id).get();
            reviewRepository.delete(review);
        }
    }

    private ReviewResponse reviewResponseGenerator(Review review) {
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setId(review.getId());
        reviewResponse.setBookingId(review.getBooking().getId());
        reviewResponse.setStudioId(review.getStudio().getId());
        reviewResponse.setContent(review.getContent());
        reviewResponse.setRating(review.getRating());
        reviewResponse.setStatus(review.getStatus());
        reviewResponse.setCreatedBy(review.getCreatedBy());
        reviewResponse.setCreatedDate(review.getCreatedDate());
        reviewResponse.setUpdatedDate(review.getUpdatedDate());
        reviewResponse.setUpdatedBy(review.getUpdatedBy());
        return reviewResponse;
    }

    private void checkExist(String id) {
        if (reviewRepository.findById(id).isEmpty()) {
            LOGGER.error("No order detail was found!");
            throw new CustomValidationException(List.of("No order detail was found!"));
        }
    }
    
}
