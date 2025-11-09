package com.flexliving.reviews.mapper;

import com.flexliving.reviews.dtos.*;
import com.flexliving.reviews.models.CategoryRating;
import com.flexliving.reviews.models.Review;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewMapper {

    private static final DateTimeFormatter HOSTAWAY_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Entity -> normalized DTO (for admin/frontend)
    public static ReviewDTO toDTO(Review r) {
        return new ReviewDTO(
                r.getId(),
                r.getHostawayId(),
                r.getType(),
                r.getStatus(),
                r.getRating(),
                r.getGuestName(),
                r.getListingName(),
                r.getPublicReview(),
                r.getChannel(),
                r.getSubmittedAt(),
                r.getReviewCategories(),
                r.isApproved()
        );
    }

    public static List<ReviewDTO> toDTOList(List<Review> reviews) {
        return reviews.stream().map(ReviewMapper::toDTO).collect(Collectors.toList());
    }

    // Entity -> Hostaway-style ReviewResultDTO
    public static ReviewResultDTO toResultDTO(Review review) {
        Integer ratingInt = review.getRating() == null ? null : review.getRating().intValue();
        List<ReviewCategoryDTO> cats = review.getReviewCategories() == null ? List.of()
                : review.getReviewCategories().stream()
                .map(cat -> new ReviewCategoryDTO(
                        cat.getCategory(),
                        cat.getRating() == null ? null : cat.getRating().intValue()
                ))
                .collect(Collectors.toList());

        String submitted = review.getSubmittedAt() == null ? null : review.getSubmittedAt().format(HOSTAWAY_DATE_FORMAT);

        Long hostawayIdAsLong = null;
        if (review.getHostawayId() != null) {
            try {
                hostawayIdAsLong = Long.parseLong(review.getHostawayId());
            } catch (NumberFormatException ignored) {}
        }

        ReviewResultDTO r = new ReviewResultDTO();
        r.setId(hostawayIdAsLong);
        r.setType(review.getType());
        r.setStatus(review.getStatus());
        r.setRating(ratingInt);
        r.setPublicReview(review.getPublicReview());
        r.setReviewCategory(cats);
        r.setSubmittedAt(submitted);
        r.setGuestName(review.getGuestName());
        r.setListingName(review.getListingName());
        return r;
    }

    public static HostawayResponseDTO toHostawayResponse(List<Review> reviews) {
        List<ReviewResultDTO> results = reviews.stream().map(ReviewMapper::toResultDTO).collect(Collectors.toList());
        return new HostawayResponseDTO("success", results);
    }
}

