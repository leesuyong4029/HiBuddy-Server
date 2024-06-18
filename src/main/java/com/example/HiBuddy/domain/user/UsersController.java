package com.example.HiBuddy.domain.user;

import com.example.HiBuddy.domain.post.dto.response.PostsResponseDto;
import com.example.HiBuddy.domain.scrap.response.ScrapsResponseDto;
import com.example.HiBuddy.domain.user.dto.request.UsersRequestDto;
import com.example.HiBuddy.domain.user.dto.response.UsersResponseDto;
import com.example.HiBuddy.global.response.ApiResponse;
import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import com.example.HiBuddy.global.response.code.resultCode.SuccessStatus;
import com.example.HiBuddy.global.response.exception.handler.GeneralHandler;
import com.example.HiBuddy.global.response.exception.handler.PostsHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/me")
public class UsersController {
    private final UsersService usersService;

    @GetMapping
    @Operation(summary = "유저 정보 조회 API", description = "유저 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER404", description = "유저를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<UsersResponseDto.UsersMyPageDto> getUserById(@AuthenticationPrincipal UserDetails user) {
        Long userId = usersService.getUserId(user);
        return usersService.getUserDTOById(userId)
                .map(ApiResponse::onSuccess)
                .orElseGet(() -> ApiResponse.onFailure(ErrorStatus.USER_NOT_FOUND.getCode(), ErrorStatus.USER_NOT_FOUND.getMessage(), null));
    }

    @PatchMapping("/nickname")
    @Operation(summary = "프로필 닉네임 수정하기 API", description = "프로필 닉네임 수정하기, UserNicknameDto 사용")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER404", description = "유저가 존재하지 않습니다", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER410", description = "닉네임 수정 실패", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<SuccessStatus> updateUserNickname(@AuthenticationPrincipal UserDetails user, @RequestBody UsersRequestDto.UserNicknameDto userNicknameDto) {
        try {
            usersService.updateUserNickname(user, userNicknameDto);
            return ApiResponse.onSuccess(SuccessStatus.USER_NICKNAME_CHANGE_SUCCESS);
        } catch (Exception e) {
            return ApiResponse.onFailure(ErrorStatus.USER_NICKNAME_CHANGE_FAIL.getCode(), ErrorStatus.USER_NICKNAME_CHANGE_FAIL.getMessage(), null);
        }
    }

    @DeleteMapping
    @Operation(summary = "유저 삭제 API", description = "유저 삭제하기")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "해당 유저가 삭제되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER404", description = "유저가 존재하지 않습니다", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER409", description = "유저 삭제 실패", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<SuccessStatus> deleteUser(@AuthenticationPrincipal UserDetails user) {
        try {
            usersService.deleteUser(usersService.getUserId(user));
            return ApiResponse.onSuccess(SuccessStatus.USER_DELETE_SUCCESS);
        } catch (Exception e) {
            return ApiResponse.onFailure(ErrorStatus.USER_DELETE_FAIL.getCode(), ErrorStatus.USER_DELETE_FAIL.getMessage(), null);
        }
    }

    @PatchMapping("/onboarding")
    @Operation(summary = "온보딩 데이터 수정 API", description = "온보딩 데이터 수정 API, UserOnboardingDto 사용")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER402", description = "유효하지 않은 국가입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER404", description = "유저를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<SuccessStatus> updateOnboardingData(@AuthenticationPrincipal UserDetails user, @RequestBody UsersRequestDto.UserOnboardingDto dto) {
        try {
            usersService.onboardingData(user, dto);
            return ApiResponse.onSuccess(SuccessStatus.USER_ONBOARDING_SUCCESS);
        } catch (IllegalArgumentException e) {
            return ApiResponse.onFailure(ErrorStatus.INVALID_COUNTRY.getCode(), ErrorStatus.INVALID_COUNTRY.getMessage(), null);
        } catch (UsernameNotFoundException e) {
            return ApiResponse.onFailure(ErrorStatus.USER_NOT_FOUND.getCode(), ErrorStatus.USER_NOT_FOUND.getMessage(), null);
        }
    }

    @PatchMapping(path = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "S3 프로필 이미지 업로드 API", description = "프로필 이미지를 S3에 업로드하고 사용자 엔티티에 설정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "IMAGE401", description = "파일이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<SuccessStatus> uploadProfileImage(@AuthenticationPrincipal UserDetails user, @RequestParam("file") MultipartFile file) {
        if(file.isEmpty()){
            return ApiResponse.onFailure(ErrorStatus.IMAGE_NOT_FOUND.getCode(),ErrorStatus.IMAGE_NOT_FOUND.getMessage(), null);
        }
        Long userId = usersService.getUserId(user);
        usersService.uploadProfileImage(file, userId);
        return ApiResponse.onSuccess(SuccessStatus.USER_PROFILE_IMAGE_CHANGE_SUCCESS);
    }

    @GetMapping("/posts")
    @Operation(summary = "내가 업로드한 게시글들 가져오기", description = "내가 업로드한 게시글들을 반환해줍니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST401", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<PostsResponseDto.PostsInfoPageDto> getMyPosts(@AuthenticationPrincipal UserDetails user,
                                                               @RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "5") int limit) {
        if (page < 1) {
            throw new GeneralHandler(ErrorStatus.PAGE_NUM_STARTS_WITH_ONE);
        }
        int pageNumber = page - 1;

        Long userId = usersService.getUserId(user);
        try {
            PostsResponseDto.PostsInfoPageDto pagedPosts = usersService.getPostsByUserId(userId, pageNumber, limit);

            // 페이지 번호를 다시 1부터 시작하도록 변환
            PostsResponseDto.PostsInfoPageDto fixedPagedPosts = PostsResponseDto.PostsInfoPageDto.builder()
                    .posts(pagedPosts.getPosts())
                    .totalPages(pagedPosts.getTotalPages())
                    .totalElements(pagedPosts.getTotalElements())
                    .isFirst(pagedPosts.isFirst())
                    .isLast(pagedPosts.isLast())
                    .number(pagedPosts.getNumber()) // 페이지 번호를 1부터 시작하도록 변환
                    .numberOfElements(pagedPosts.getNumberOfElements())
                    .build();

            return ApiResponse.onSuccess(fixedPagedPosts);
        } catch (PostsHandler e) {
            return ApiResponse.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }

    @GetMapping("/scraps")
    @Operation(summary = "내가 스크랩한 게시글들 가져오기", description = "내가 스크랩한 게시글들을 반환해줍니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST403", description = "스크랩 목록을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<ScrapsResponseDto.ScrapsInfoPageDto> getMyScrabs(@AuthenticationPrincipal UserDetails user,
                                                                        @RequestParam(defaultValue = "1") int page,
                                                                        @RequestParam(defaultValue = "5") int limit) {
        if (page < 1) {
            throw new GeneralHandler(ErrorStatus.PAGE_NUM_STARTS_WITH_ONE);
        }
        int pageNumber = page - 1;

        Long userId = usersService.getUserId(user);
        try {
            ScrapsResponseDto.ScrapsInfoPageDto pageScraps = usersService.getScrapsByUserId(userId, pageNumber, limit);

            ScrapsResponseDto.ScrapsInfoPageDto fixedScrapsPagedPosts = ScrapsResponseDto.ScrapsInfoPageDto.builder()
                    .posts(pageScraps.getPosts())
                    .totalPages(pageScraps.getTotalPages())
                    .totalElements(pageScraps.getTotalElements())
                    .isFirst(pageScraps.isFirst())
                    .isLast(pageScraps.isLast())
                    .number(pageScraps.getNumber()) // 페이지 번호를 1부터 시작하도록 변환
                    .numberOfElements(pageScraps.getNumberOfElements())
                    .build();

            return ApiResponse.onSuccess(fixedScrapsPagedPosts);
        } catch (PostsHandler e) {
            return ApiResponse.onFailure(e.getErrorReason().getCode(), e.getErrorReason().getMessage(), null);
        }
    }
}
