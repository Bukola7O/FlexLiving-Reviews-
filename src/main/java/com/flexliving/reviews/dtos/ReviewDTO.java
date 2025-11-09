package com.flexliving.reviews.dtos;

import com.flexliving.reviews.models.CategoryRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;                // DB id
    private String hostawayId;
    private String type;
    private String status;
    private Double rating;
    private String guestName;
    private String listingName;
    private String publicReview;
    private String channel;
    private LocalDateTime submittedAt;
    private List<CategoryRating> reviewCategories;
    private boolean approved;
}

