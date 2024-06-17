package com.example.HiBuddy.global.response.code.resultCode;


import com.example.HiBuddy.global.response.code.BaseCode;
import com.example.HiBuddy.global.response.code.ReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// Enum Naming Format: {행위}_{목적어}_{성공여부}
// Message Format: 동사 명사형으로 마무리
@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),

    // OAuth
    OAUTH_USER_CREATE_SUCCESS(HttpStatus.OK, "OAUTH001", "소셜 로그인 사용자 생성 성공"),

    // Users
    USER_CREATE_SUCCESS(HttpStatus.OK, "USER201", "사용자 생성 성공"),
    USER_LOGIN_SUCCESS(HttpStatus.OK, "USER202", "사용자 로그인 성공"),
    USER_TOKEN_REISSUE_SUCCESS(HttpStatus.OK, "USER203", "사용자 토큰 재발급 성공"),
    USER_INFO_GET_SUCCESS(HttpStatus.OK, "USER204", "내 정보(마이페이지) 가져오기 성공"),
    USER_NICKNAME_CHANGE_SUCCESS(HttpStatus.OK, "USER205", "닉네임 수정하기 성공"),
    USER_PROFILE_IMAGE_CHANGE_SUCCESS(HttpStatus.OK, "USER206", "프로필 이미지 수정하기 성공"),
    GET_MY_UPLOAD_COMMENT_SUCCESS(HttpStatus.OK,"USER207", "내가 업로드한 게시글 가져오기 성공"),
    GET_MY_SCRAB_COMMENT_SUCCESS(HttpStatus.OK,"USER208", "내가 스크랩한 게시글 가져오기 성공"),
    USER_DELETE_SUCCESS(HttpStatus.OK, "USER209", "유저 삭제 성공"),
    USER_ONBOARDING_SUCCESS(HttpStatus.OK, "USER210", "온보딩 데이터 적용 성공"),
    USER_LOGOUT_SUCCESS(HttpStatus.OK, "USER211", "사용자 로그아웃 성공"),

    // Posts
    POST_S3_IMAGE_UPLOAD_SUCCESS(HttpStatus.OK,"POST201", "S3 이미지 업로드 성공"),
    POST_CREATE_SUCCESS(HttpStatus.OK,"POST202", "게시글 생성 성공"),
    POST_CHANGE_SUCCESS(HttpStatus.OK,"POST203", "게시글 수정 성공"),
    GET_RANDOM_COMMENT_SUCCESS(HttpStatus.OK,"POST204", "랜덤으로 게시글들 가져오기 성공"),
    POST_COMMENT_DELETE_SUCCESS(HttpStatus.OK,"POST205", "게시글 삭제 성공"),
    POST_CHOOSE_LIKE_SUCCESS(HttpStatus.OK,"POST206", "공감 누르기 성공"),
    POST_CANCEL_LIKE_SUCCESS(HttpStatus.OK,"POST207", "공감 해제하기 성공"),

    // Comments
    COMMENT_CREATE_SUCCESS(HttpStatus.OK, "COMMENT201", "댓글 생성 성공"),
    COMMENT_CHANGE_SUCCESS(HttpStatus.OK, "COMMENT2O2", "댓글 수정 성공"),
    COMMENT_DELETE_SUCCESS(HttpStatus.OK, "COMMENT203", "댓글 삭제 성공"),
    COMMENT_CHOOSE_LIKE_SUCCESS(HttpStatus.OK, "COMMENT204", "댓글 좋아요 누르기 성공"),
    COMMENT_CANCEL_LIKE_SUCCESS(HttpStatus.OK, "COMMENT205", "댓글 좋아요 해제하기 성공"),

    // Searchs
    SEARCH_POST_TITLE_SUCCESS(HttpStatus.OK, "SEARCH201", "게시물 제목으로 검색 성공"),

    // chat
    CHATROOM_ADD_USER_SUCCESS(HttpStatus.OK, "CHATROOM202", "채팅방 유저 join 성공"),
    CHATROOM_ENTER_SUCCESS(HttpStatus.OK, "CHATROOM203", "채팅방 입장 성공"),
    CHATROOM_LEAVE_SUCCESS(HttpStatus.OK, "CHATROOM204", "채팅방 퇴장 성공"),
    CHATROOM_DELETE_SUCCESS(HttpStatus.OK, "CHATROOM205", "매칭글 삭제 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDto getReason() {
        return ReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDto getReasonHttpStatus() {
        return ReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
