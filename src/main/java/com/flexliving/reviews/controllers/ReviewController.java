package com.flexliving.reviews.controllers;

import com.flexliving.reviews.dtos.*;
import com.flexliving.reviews.mapper.ReviewMapper;
import com.flexliving.reviews.models.Review;
import com.flexliving.reviews.repositories.ReviewRepository;
import com.flexliving.reviews.services.GoogleReviewService;
import com.flexliving.reviews.services.HostawayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReviewController {

    private final HostawayService hostawayService;
    private final ReviewRepository reviewRepository;
    private final GoogleReviewService googleReviewService;

    @GetMapping("/hostaway")
    public HostawayResponseDTO getHostawayReviews() {
        return ReviewMapper.toHostawayResponse(hostawayService.fetchMockReviews());
    }

    // Normalized admin/public endpoints
    @GetMapping("/approved")
    public List<ReviewDTO> getApprovedReviews() {
        return ReviewMapper.toDTOList(reviewRepository.findByApprovedTrue());
    }

    @GetMapping("/pending")
    public List<ReviewDTO> getPendingReviews() {
        return ReviewMapper.toDTOList(reviewRepository.findByApprovedFalse());
    }

    @PostMapping("/{id}/approve")
    public ReviewDTO approveReview(@PathVariable Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setApproved(true);
        Review saved = reviewRepository.save(review);
        return ReviewMapper.toDTO(saved);
    }

    // Google exploration endpoint: call with ?placeId=ChI...
    @GetMapping("/google")
    public List<GoogleReviewDTO> getGoogleReviews(@RequestParam String placeId) {
        return googleReviewService.fetchReviews(placeId);
    }
}
