package com.flexliving.reviews.repositories;

import com.flexliving.reviews.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByApprovedTrue();
    List<Review> findByApprovedFalse();
}


