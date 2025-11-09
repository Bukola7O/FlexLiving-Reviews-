package com.flexliving.reviews.models;

import lombok.*;

import jakarta.persistence.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRating {
    private String category;
    private Double rating;
}
