package com.example.HiBuddy.domain.comment;

import com.example.HiBuddy.domain.comment.dto.request.CommentsRequestDto;
import com.example.HiBuddy.domain.comment.dto.response.CommentsResponseDto;
import com.example.HiBuddy.domain.post.Posts;
import com.example.HiBuddy.domain.post.PostsRepository;
import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.domain.user.UsersRepository;
import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import com.example.HiBuddy.global.response.exception.handler.CommentsHandler;
import com.example.HiBuddy.global.response.exception.handler.PostsHandler;
import com.example.HiBuddy.global.response.exception.handler.UsersHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentsService {

    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;
    private final CommentsRepository commentsRepository;

    public String getCreatedAt(LocalDateTime createdAt) {

        // 서버시간을 UTC로 설정
        ZoneId serverZone = ZoneId.systemDefault(); // 시스템 기본 시간대를 사용
        LocalDateTime now = ZonedDateTime.now(serverZone).toLocalDateTime();

        Duration duration = Duration.between(createdAt, now);

        long seconds = duration.getSeconds();
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (minutes < 1) {
            return seconds + "s ago";
        } else if (minutes < 60) {
            return minutes + "m ago";
        } else if (hours < 24) {
            return hours + "h ago";
        } else {
            return days + "d ago";
        }
    }

    // 댓글 생성 API
    @Transactional
    public CommentsResponseDto.CommentDto createComment(Long userId, Long postId, CommentsRequestDto.CreateCommentRequestDto request) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersHandler(ErrorStatus.USER_NOT_FOUND));
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostsHandler(ErrorStatus.POST_NOT_FOUND));

        if (request.getComment().length() > 500) {
            throw new CommentsHandler(ErrorStatus.COMMENT_MAX_LENGTH_500);
        }

        Comments newComment = CommentsConverter.toCommentEntity(user, post, request);
        commentsRepository.save(newComment);

        return CommentsConverter.toCommentInfoResultDto(newComment, post, user, newComment.getCreatedAt().toString());
    }

    // 댓글 조회 API
    @Transactional(readOnly = true)
    public CommentsResponseDto.CommentsInfoPageDto getCommentsInfoResultsOnPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        Page<Comments> commentsPage = commentsRepository.findAll(pageRequest);

        List<CommentsResponseDto.CommentDto> commentInfoDtoList = commentsPage.getContent().stream()
                .map(comment -> {
                    Users user = comment.getUser();
                    Posts post = comment.getPost();
                    String createdAt = getCreatedAt(comment.getCreatedAt());
                    return CommentsConverter.toCommentInfoResultDto(comment, post, user, createdAt);
                })
                .collect(Collectors.toList());

        return CommentsConverter.toCommentsInfoResultPageDto(commentInfoDtoList, commentsPage.getTotalPages(),
                (int) commentsPage.getTotalElements(), commentsPage.isFirst(), commentsPage.isLast(),
                commentsPage.getNumber(), commentsPage.getNumberOfElements());

    }

    // 댓글 수정 API
    @Transactional
    public void updateComment(Long userId, Long postId, Long commentId, CommentsRequestDto.UpdateCommentRequestDto request) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersHandler(ErrorStatus.USER_NOT_FOUND));
        Comments comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new CommentsHandler(ErrorStatus.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CommentsHandler(ErrorStatus.COMMENT_NOT_AUTHORIZED);
        }

        if (comment.getComment().length() > 500) {
            throw new CommentsHandler(ErrorStatus.COMMENT_MAX_LENGTH_500);
        }

        comment.setComment(request.getComment());

        Posts post = comment.getPost();

        commentsRepository.save(comment);
    }

    // 댓글 삭제 API
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersHandler(ErrorStatus.USER_NOT_FOUND));
        Comments comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new CommentsHandler(ErrorStatus.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CommentsHandler(ErrorStatus.COMMENT_NOT_AUTHORIZED);
        }

        commentsRepository.deleteById(comment.getId());
    }
}
