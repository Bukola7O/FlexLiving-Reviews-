package com.flexliving.reviews.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexliving.reviews.dtos.GoogleReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleReviewService {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${google.api.key:}")
    private String apiKey;

    /**
     * Fetch basic reviews for provided placeId from Google Places Details API.
     * Returns empty list if none found. Caller should handle missing API key.
     */
    public List<GoogleReviewDTO> fetchReviews(String placeId) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Google API key not configured. Set google.api.key in application.yml or environment.");
        }

        String url = UriComponentsBuilder
                .fromHttpUrl("https://maps.googleapis.com/maps/api/place/details/json")
                .queryParam("place_id", URLEncoder.encode(placeId, StandardCharsets.UTF_8))
                .queryParam("fields", "reviews,rating,user_ratings_total")
                .queryParam("key", apiKey)
                .build().toUriString();

        ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new RuntimeException("Google Places API error: " + resp.getStatusCode());
        }

        try {
            JsonNode root = mapper.readTree(resp.getBody());
            JsonNode reviews = root.path("result").path("reviews");
            if (!reviews.isArray()) return Collections.emptyList();

            List<GoogleReviewDTO> list = new ArrayList<>();
            for (JsonNode r : reviews) {
                String author = r.path("author_name").asText(null);
                Integer rating = r.has("rating") && !r.get("rating").isNull() ? r.get("rating").asInt() : null;
                String text = r.path("text").asText(null);
                Long time = r.has("time") && !r.get("time").isNull() ? r.get("time").asLong() : null;
                list.add(new GoogleReviewDTO(author, rating, text, time));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Google Places response", e);
        }
    }
}

//After exploring Google Places API for Reviews,
//I found that reviews are limited to 5 per place and require billing activation.
//Due to these limitations, integration is not included in this version.
//The GoogleReviewService is implemented and ready to fetch reviews if an API key and valid place IDs are provided