package com.flexliving.reviews.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hostawayId;
    private String type;
    private String status;
    private Double rating;             // <-- new field
    private String publicReview;
    private String guestName;
    private String listingName;
    private LocalDateTime submittedAt;
    private String channel;
    private boolean approved = false;

    @ElementCollection
    private List<CategoryRating> reviewCategories;
}
