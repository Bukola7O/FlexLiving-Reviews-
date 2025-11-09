package com.flexliving.reviews.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexliving.reviews.models.CategoryRating;
import com.flexliving.reviews.models.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HostawayService {

    private final ObjectMapper mapper = new ObjectMapper();

    public List<Review> fetchMockReviews() {
        try (InputStream is = getClass().getResourceAsStream("/mock/hostaway-reviews.json")) {
            Map<String, Object> json = mapper.readValue(is, new TypeReference<>() {});
            List<Map<String, Object>> result = (List<Map<String, Object>>) json.get("result");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            return result.stream().map(item -> {
                List<CategoryRating> cats = ((List<Map<String, Object>>) item.get("reviewCategory"))
                        .stream()
                        .map(c -> new CategoryRating((String) c.get("category"),
                                Double.valueOf(c.get("rating").toString())))
                        .collect(Collectors.toList());

                Double rating = null;
                if (item.get("rating") != null) {
                    try {
                        rating = Double.valueOf(item.get("rating").toString());
                    } catch (Exception ignored) {}
                }

                return Review.builder()
                        .hostawayId(String.valueOf(item.get("id")))
                        .type((String) item.get("type"))
                        .status((String) item.get("status"))
                        .rating(rating)                         // <-- set rating
                        .publicReview((String) item.get("publicReview"))
                        .guestName((String) item.get("guestName"))
                        .listingName((String) item.get("listingName"))
                        .channel("Hostaway")
                        .submittedAt(LocalDateTime.parse((String) item.get("submittedAt"), formatter))
                        .reviewCategories(cats)
                        .build();
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error reading mock Hostaway data", e);
        }
    }
}
