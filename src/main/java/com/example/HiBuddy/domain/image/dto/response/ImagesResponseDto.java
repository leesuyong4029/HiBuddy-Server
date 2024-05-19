package com.example.HiBuddy.domain.image.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImagesResponseDto {
    public List<Long> imageIds;
}
