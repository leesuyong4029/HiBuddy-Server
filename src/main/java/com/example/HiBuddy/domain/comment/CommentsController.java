package com.example.HiBuddy.domain.comment;

import com.example.HiBuddy.domain.comment.dto.request.CommentsRequestDto;
import com.example.HiBuddy.domain.comment.dto.response.CommentsResponseDto;
import com.example.HiBuddy.domain.user.UsersService;
import com.example.HiBuddy.global.response.ApiResponse;
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


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/thread/posts/{postId}/comments")
public class CommentsController {

    private final UsersService usersService;
    private final CommentsService commnetsService;

    @PostMapping
    @Operation(summary = "댓글 생성 API", description = "댓글 생성. CreateCommentsRequestDto 사용")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENTS401", description = "댓글 생성에 실패했습니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENTS402", description = "이미 작성한 댓글입니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT405", description = "댓글은 최대 500자까지 입력 가능합니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글의 id"),
    })
    public ApiResponse<CommentsResponseDto.CommentDto> createComment(@AuthenticationPrincipal UserDetails user, @PathVariable Long postId, @RequestBody CommentsRequestDto.CreateCommentRequestDto request) {
        CommentsResponseDto.CommentDto response = commnetsService.createComment(usersService.getUserId(user), postId, request);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping
    @Operation(summary = "댓글 조회 API", description = "게시글당 1페이지에 최대 10개의 댓글을 조회하는 기능. 게시글은 생성날짜 기준으로 오름차순 정렬")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENTS403", description = "존재하지 않는 댓글입니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글의 id"),
    })
    public ApiResponse<CommentsResponseDto.CommentsInfoPageDto> getCommnetsInfoResultOnPage(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        int pageNumber = page - 1;
        Long userId = usersService.getUserId(user);

        CommentsResponseDto.CommentsInfoPageDto commentsInfoPageDto =
                commnetsService.getCommentsInfoResultsOnPage(postId, userId, pageNumber, limit);

        CommentsResponseDto.CommentsInfoPageDto fixedCommentsInfoPageDto = CommentsResponseDto.CommentsInfoPageDto.builder()
                .comments(commentsInfoPageDto.getComments())
                .totalPages(commentsInfoPageDto.getTotalPages())
                .totalElements(commentsInfoPageDto.getTotalElements())
                .isFirst(commentsInfoPageDto.isFirst())
                .isLast(commentsInfoPageDto.isLast())
                .number(commentsInfoPageDto.getNumber()) // 페이지 번호를 1부터 시작하도록 변환
                .numberOfElements(commentsInfoPageDto.getNumberOfElements())
                .build();


        return ApiResponse.onSuccess(fixedCommentsInfoPageDto);
    }


    @PatchMapping("/{commentId}")
    @Operation(summary = "댓글 수정 API", description = "댓글 수정, UpdateCommentsRequestDto 사용")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENTS403", description = "존재하지 않는 댓글입니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENTS404", description = "해당 댓글에 대한 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT405", description = "댓글은 최대 500자까지 입력 가능합니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글의 id"),
            @Parameter(name = "commentId", description = "댓글의 id"),
    })
    public ApiResponse<SuccessStatus> updateComment(@AuthenticationPrincipal UserDetails user, @PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentsRequestDto.UpdateCommentRequestDto request) {
        commnetsService.updateComment(usersService.getUserId(user), postId, commentId, request);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제 API", description = "댓글 id를 받아 해당하는 댓글 삭제")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENTS403", description = "존재하지 않는 댓글입니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENTS404", description = "해당 댓글에 대한 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponses.class))),
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글의 id"),
            @Parameter(name = "commentId", description = "댓글의 id"),
    })
    public ApiResponse<SuccessStatus> deleteComment(@AuthenticationPrincipal UserDetails user, @PathVariable Long postId, @PathVariable Long commentId) {
        commnetsService.deleteComment(usersService.getUserId(user), commentId);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

}
