package com.flexliving.reviews.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResultDTO {
    private Long id;
    private String type;
    private String status;
    private Integer rating;
    private String publicReview;
    private List<ReviewCategoryDTO> reviewCategory;
    private String submittedAt;
    private String guestName;
    private String listingName;
}

