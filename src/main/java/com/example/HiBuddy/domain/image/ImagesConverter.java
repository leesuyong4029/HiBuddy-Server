package com.example.HiBuddy.domain.image;

import com.example.HiBuddy.domain.image.dto.response.ImagesResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImagesConverter {

    public static ImagesResponseDto toUploadImagesDto(List<Images> imagesList) {
        List<Long> imageIds = imagesList.stream().map(Images::getId).collect(Collectors.toList());
        return new ImagesResponseDto(imageIds);
    }
}
