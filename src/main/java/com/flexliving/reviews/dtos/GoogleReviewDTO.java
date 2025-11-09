package com.flexliving.reviews.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleReviewDTO {
    private String authorName;
    private Integer rating;
    private String text;
    private Long time; // epoch seconds (Google returns unix timestamp)
}

