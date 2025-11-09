package com.flexliving.reviews.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HostawayResponseDTO {
    private String status;
    private List<ReviewResultDTO> result;
}

