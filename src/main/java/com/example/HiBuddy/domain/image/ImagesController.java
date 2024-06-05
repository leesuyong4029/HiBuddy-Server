package com.example.HiBuddy.domain.image;

import com.example.HiBuddy.domain.image.dto.response.ImagesResponseDto;
import com.example.HiBuddy.domain.user.UsersService;
import com.example.HiBuddy.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/images")
public class ImagesController {
    private final ImagesService imagesService;
    private final UsersService usersService;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "S3 이미지 업로드 API", description = "ImagesResponseDto 사용")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "IMAGE401", description = "이미지 업로드 실패", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<ImagesResponseDto> uploadImages(@AuthenticationPrincipal UserDetails user, @RequestPart("fileList") List<MultipartFile> fileList) {
        Long userId = usersService.getUserId(user);
        List<Images> imageList = imagesService.uploadImages(fileList, userId);
        ImagesResponseDto imagesResponseDto = ImagesConverter.toUploadImagesDto(imageList);
        return ApiResponse.onSuccess(imagesResponseDto);
    }

    @DeleteMapping("/{imageId}")
    @Operation(summary = "S3 이미지 업로드 취소 API", description = "dto 사용 x")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "IMAGE401"),
    })
    public ApiResponse<Void> deleteImage(@PathVariable Long imageId) {
        imagesService.deleteImages(imageId);
        return ApiResponse.onSuccess(null);
    }


}
