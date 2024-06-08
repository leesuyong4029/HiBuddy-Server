package com.example.HiBuddy.domain.post;

import com.example.HiBuddy.domain.post.dto.request.PostsRequestDto;
import com.example.HiBuddy.domain.post.dto.response.PostsResponseDto;
import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.global.response.ApiResponse;
import com.example.HiBuddy.domain.user.UsersService;
import com.example.HiBuddy.global.response.code.resultCode.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/thread")
public class PostsController {

    private final UsersService usersService;
    private final PostsService postsService;

    @PostMapping("/posts")
    @Operation(summary = "게시글 생성 API", description = "게시글을 생성, CreatePostRequestDto 사용")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST402", description = "게시글 작성 실패", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST405", description = "최대 3장의 이미지만 업로드 가능합니다.6", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST408", description = "게시글의 제목은 최대 100자까지 입력 가능합니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST409", description = "게시글의 본문은 최대 500자까지 입력 가능합니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
    })
    public ApiResponse<PostsResponseDto.PostsInfoDto> createPost(@AuthenticationPrincipal UserDetails user, @RequestBody PostsRequestDto.CreatePostRequestDto request) {
        PostsResponseDto.PostsInfoDto post = postsService.createPost(usersService.getUserId(user), request);
        return ApiResponse.onSuccess(post);
    }

    @GetMapping("/posts/{postId}")
    @Operation(summary = "특정 게시글 조회 API", description = "게시글의 id를 이용하여 특정 게시글의 정보 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST401", description = "존재하지 않는 게시글입니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글의 id"),
    })
    public ApiResponse<PostsResponseDto.PostsInfoDto> getPostInfoResult(@AuthenticationPrincipal UserDetails user, @PathVariable(name = "postId") Long postId) {

        PostsResponseDto.PostsInfoDto postsInfoDto = postsService.getPostInfoResult(usersService.getUserId(user), postId);
        return ApiResponse.onSuccess(postsInfoDto);
    }

    @PatchMapping("/posts/{postId}")
    @Operation(summary = "게시글 수정 API", description = "특정 게시글을 수정, UpdatePostRequestDto 사용")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST401", description = "존재하지 않는 게시글입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST407", description = "게시글에 대한 권한이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글의 id"),
    })
    public ApiResponse<SuccessStatus> updatePost(@AuthenticationPrincipal Users user, @PathVariable(name = "postId") Long postId, @RequestBody PostsRequestDto.UpdatePostRequestDto request) {
        postsService.updatePost(usersService.getUserId(user), postId, request);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "게시글 삭제 API", description = "특정 게시글을 삭제")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST401", description = "존재하지 않는 게시글입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST407", description = "게시글에 대한 권한이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글의 id"),
    })
    public ApiResponse<SuccessStatus> deletePost(@AuthenticationPrincipal UserDetails user, @PathVariable(name = "postId") Long postId) {
        postsService.deletePost(usersService.getUserId(user), postId);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @GetMapping("/main/popular")
    @Operation(summary = "메인페이지 게시글 3개 조회 API (좋아요 순)", description = "좋아요 수가 많은 게시글 일수록 메인 페이지 상단에 위치, 게시글 3개 조회, MainPageTopThreePostListDto 사용")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST401", description = "존재하지 않는 게시글입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<List<PostsResponseDto.MainPageTopThreePostListDto>> getTopThreePosts() {
        List<PostsResponseDto.MainPageTopThreePostListDto> mainPageTopThreePostListDto = postsService.getMainPageTopThreePostList();
        return ApiResponse.onSuccess(mainPageTopThreePostListDto);
    }


    @GetMapping("/posts")
    @Operation(summary = "게시글 5개씩 조회 API (1페이지당)", description = "페이지 한 개당 최대 5개의 글을 조회 가능. 게시글은 생성된 날짜가 최신일수록 상단에 위치")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST401", description = "존재하지 않는 게시글입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<PostsResponseDto.PostsInfoPageDto> getPostsInfoResultOnPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "created_at.desc") String sort) {
        // 페이지 번호는 1부터 시작하도록 설정. Spring Data JPA의 페이지 번호는 0부터 시작하기 때문에 1을 빼줌
        int pageNumber = page - 1;

        PostsResponseDto.PostsInfoPageDto postsInfoPageDto = postsService.getPostsInfoResultsOnPage(pageNumber, limit);

        return ApiResponse.onSuccess(postsInfoPageDto);
    }

    @PostMapping("posts/{postId}/likes")
    @Operation(summary = "좋아요 누르기 API", description = "게시글에 좋아요를 생성하는 기능")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER404", description = "존재하지 않는 유저입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST401", description = "존재하지 않는 게시글입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POSTLIKE401", description = "좋아요 누르기에 실패했습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POSTLIKE402", description = "이미 좋아요를 누른 게시글입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글의 id"),
    })
    public ApiResponse<SuccessStatus> addPostLikes(@AuthenticationPrincipal UserDetails user, @PathVariable(name = "postId") Long postId) {

        postsService.addLike(usersService.getUserId(user), postId);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @DeleteMapping("posts/{postId}/likes")
    @Operation(summary = "좋아요 해제 API", description = "게시글에 좋아요를 해제하는 기능")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POSTLIKE403", description = "존재하지 않는 좋아요 입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POSTLIKE404", description = "좋아요의 개수가 0개 입니다", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글의 id"),
    })
    public ApiResponse<SuccessStatus> removePostLike(@AuthenticationPrincipal UserDetails user, @PathVariable(name = "postId") Long postId) {

        postsService.removeLike(usersService.getUserId(user), postId);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @PostMapping("posts/{postId}/scraps")
    @Operation(summary = "스크랩 생성 API", description = "게시글을 스크랩하는 기능")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST401", description = "존재하지 않는 게시글입니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SCRAP401", description = "스크랩 생성에 실패했습니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SCRAP402", description = "이미 스크랩한 게시글입니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글의 id"),
    })
    public ApiResponse<SuccessStatus> createScraps(@AuthenticationPrincipal UserDetails user, @PathVariable(name = "postId") Long postId) {
        postsService.createScraps(usersService.getUserId(user), postId);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @DeleteMapping("posts/{postId}/scraps")
    @Operation(summary = "스크랩 해제 API", description = "게시글을 스크랩 해제하는 기능")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST401", description = "존재하지 않는 게시글입니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글의 id"),
    })
    public ApiResponse<SuccessStatus> deleteScraps(@AuthenticationPrincipal UserDetails user, @PathVariable(name = "postId") Long postId) {
        postsService.deleteScraps(usersService.getUserId(user), postId);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @GetMapping("/search")
    @Operation(summary = "게시글 제목으로 검색하기 API", description = "게시글 제목을 기준으로 검색하는 기능")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST401", description = "존재하지 않는 게시글입니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEARCH401", description = "존재하지 않는 게시글 제목입니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),

    })
    public ApiResponse<PostsResponseDto.PostsInfoPageDto> searchPostsByTtile(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "created_at.desc") String sort) {
        int pageNumber = page - 1;

        PostsResponseDto.PostsInfoPageDto postsInfoPage = postsService.searchPostByTitle(keyword, pageNumber, limit);
        return ApiResponse.onSuccess(postsInfoPage);
    }

}
