package com.example.HiBuddy.global.response.code.resultCode;

import com.example.HiBuddy.global.response.code.BaseErrorCode;
import com.example.HiBuddy.global.response.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// Enum Naming Format: {주체}_{이유}
@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // Global
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL501", "서버 오류"),
    KAKAO_TOKEN_ERROR(HttpStatus.BAD_REQUEST, "GLOBAL502", "토큰관련 서버 에러"),
    GOOGLE_TOKEN_ERROR(HttpStatus.BAD_REQUEST, "GLOBAL503", "구글 토큰관련 서버 에러"),

    // OAuth
    EXPIRED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "OAUTH401", "기존 토큰이 만료되었습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "OAUTH402", "Refresh Token을 찾지 못했습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "OAUTH403", "Refresh Token이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "OAUTH404", "Refresh Token이 유효하지 않습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "OAUTH405", "Access Token이 유효하지 않습니다."),

    // Users
    USER_EXISTS_NICKNAME(HttpStatus.BAD_REQUEST, "USER401", "중복된 닉네임입니다."),
    INVALID_COUNTRY(HttpStatus.BAD_REQUEST, "USER402", "유효하지 않은 국가입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404", "유저를 찾을 수 없습니다."),
    USER_NICKNAME_CHANGE_FAIL(HttpStatus.BAD_REQUEST, "USER405", "닉네임 수정하기 실패"),
    USER_DELETE_FAIL(HttpStatus.BAD_REQUEST, "USER409", "유저 삭제 실패"),

    // Posts
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST401", "존재하지 않는 게시글입니다."),
    POST_CREATE_FAIL(HttpStatus.BAD_REQUEST, "POST402", "게시글 작성에 실패했습니다."),
    POST_SCRAP_LIST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST403", "스크랩 목록을 찾을 수 없습니다."),
    POST_LIST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST404", "게시글 목록이 존재하지 않습니다."),
    POST_TITLE_NOT_FOUND(HttpStatus.NOT_FOUND, "POST405", "존재하지 않는 게시글 제목입니다."),
    POST_IMAGE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "POST406", "최대 3장의 이미지만 업로드 가능합니다."),
    POST_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "POST407", "게시글에 대한 권한이 없습니다."),
    POST_TITLE_MAX_LENGTH_100(HttpStatus.BAD_REQUEST, "POST408", "게시글의 제목은 최대 100자까지 입력 가능합니다."),
    POST_CONTENT_MAX_LENGTH_500(HttpStatus.BAD_REQUEST, "POST409", "게시글의 본문은 최대 500자까지 입력 가능합니다."),

    // PostsLike
    POSTLIKE_CREATE_FAIL(HttpStatus.NOT_FOUND, "POSTLIKE401", "좋아요 누르기에 실패했습니다."),
    POSTLIKE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "POSTLIKE402", "이미 좋아요를 누른 게시물입니다."),
    POSTLIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "POSTLIKE403", "존재하지 않는 공감입니다."),
    POSTLIKE_DELETE_FAIL(HttpStatus.NOT_FOUND, "POSTLIKE404", "좋아요의 개수가 0입니다."),

    // Comments
    COMMENT_CREATE_FAIL(HttpStatus.NOT_FOUND, "COMMENT401", "댓글 생성에 실패했습니다."),
    COMMENT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "COMMENT402", "이미 작성한 댓글입니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT403", "존재하지 않는 댓글입니다."),
    COMMENT_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMENT404", "해당 댓글에 대한 권한이 없습니다."),
    COMMENT_MAX_LENGTH_500(HttpStatus.BAD_REQUEST, "COMMENT405", "댓글은 최대 500자까지 입력 가능합니다."),

    // Scraps
    SCRAP_CREATE_FAIL(HttpStatus.NOT_FOUND, "SCRAP401", "스크랩 생성에 실패했습니다."),
    SCRAP_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "SCRAP402", "이미 스크랩한 게시글입니다."),

    // Images
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "IMAGE401", "파일이 존재하지 않습니다."),
    IMAGE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "IMAGE402", "이미지 업로드에 실패했습니다."),
    IMAGE_NOT_PROVIDED(HttpStatus.NOT_FOUND, "IMAGE403", "프로필 이미지가 존재하지 않습니다."),

    // Searchs
    SEARCH_NOT_FOUND(HttpStatus.NOT_FOUND, "SEARCH401", "존재하지 않는 게시글 제목입니다."),

<<<<<<< HEAD
    // Chat
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT401", "채팅방을 찾지 못했습니다."),
    CHATROOM_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "CHAT402", "정원이 초과하였습니다.");
=======
    // Common
    PAGE_NUM_STARTS_WITH_ONE(HttpStatus.BAD_REQUEST, "COMMON401", "페이지 번호는 1 이상이어야 합니다.");
>>>>>>> d26e2c5b2b7623b29aeff4d3cb30e4e03d1006d2

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
