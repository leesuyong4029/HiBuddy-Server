package com.example.HiBuddy.domain.post;

import com.example.HiBuddy.domain.image.Images;
import com.example.HiBuddy.domain.image.ImagesRepository;
import com.example.HiBuddy.domain.post.dto.request.PostsRequestDto;
import com.example.HiBuddy.domain.post.dto.response.PostsResponseDto;
import com.example.HiBuddy.domain.postLike.PostLikes;
import com.example.HiBuddy.domain.postLike.PostLikesRepository;
import com.example.HiBuddy.domain.scrap.Scraps;
import com.example.HiBuddy.domain.scrap.ScrapsRepository;
import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.domain.user.UsersRepository;
import com.example.HiBuddy.global.response.ApiResponse;
import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import com.example.HiBuddy.global.response.code.resultCode.SuccessStatus;
import com.example.HiBuddy.global.response.exception.handler.PostLikesHandler;
import com.example.HiBuddy.global.response.exception.handler.PostsHandler;
import com.example.HiBuddy.global.response.exception.handler.ScrapsHandler;
import com.example.HiBuddy.global.response.exception.handler.UsersHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsService {

    private final ImagesRepository imagesRepository;
    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;
    private final PostLikesRepository postLikesRepository;
    private final ScrapsRepository scrapsRepository;

    // 시간 구하는 메서드
    public static String getCreatedAt(LocalDateTime createdAt) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");

        // LocalDateTime을 ZoneDateTime으로 변환하고, GMT+9 타임존을 설정
        ZonedDateTime zonedDateTime = createdAt.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Asia/Seoul"));

        return zonedDateTime.format(formatter);
    }

    // 게시글 생성 메서드
    @Transactional
    public PostsResponseDto.PostsInfoDto createPost(Long userId, PostsRequestDto.CreatePostRequestDto request) {
        if (request.getImageIds().size() > 3) {
            throw new PostsHandler(ErrorStatus.POST_IMAGE_LIMIT_EXCEEDED);
        }

        if (request.getTitle().length() > 100) {
            throw new PostsHandler(ErrorStatus.POST_TITLE_MAX_LENGTH_100);
        }

        if (request.getContent().length() > 500) {
            throw new PostsHandler(ErrorStatus.POST_CONTENT_MAX_LENGTH_500);
        }

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersHandler(ErrorStatus.USER_NOT_FOUND));

        Posts newPost = PostsConverter.toPostsEntity(user, request);
        postsRepository.save(newPost);

        // Images 엔티티의 post 필드 설정
        List<Images> images = imagesRepository.findAllById(request.getImageIds());
        for (Images image : images) {
            image.setPost(newPost);
            imagesRepository.save(image);
        }

        newPost.getPostImageList().addAll(images); // 양방향 연관관계 설정

        return PostsConverter.toPostInfoResultDto(newPost, user, false, false, getCreatedAt(newPost.getCreatedAt()), true);
    }

    // 특정 게시글 조회 메서드
    @Transactional(readOnly = true)
    public PostsResponseDto.PostsInfoDto getPostInfoResult(Long userId, Long postId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersHandler(ErrorStatus.USER_NOT_FOUND));

        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostsHandler(ErrorStatus.POST_NOT_FOUND));

        boolean checkLike = postLikesRepository.existsByUserAndPost(user, post);
        boolean checkScrap = scrapsRepository.existsByUserAndPost(user, post);

        String createdAt = getCreatedAt(post.getCreatedAt());
        boolean isAuthor = post.getUser().getId().equals(userId);

        return PostsConverter.toPostInfoResultDto(post, user, checkLike, checkScrap, createdAt, isAuthor);
    }

    // 게시글 수정 메서드
    @Transactional
    public void updatePost(Long userId, Long postId, PostsRequestDto.UpdatePostRequestDto request) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersHandler(ErrorStatus.USER_NOT_FOUND));

        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostsHandler(ErrorStatus.POST_NOT_FOUND));

        if (request.getTitle().length() > 100) {
            throw new PostsHandler(ErrorStatus.POST_TITLE_MAX_LENGTH_100);
        }

        if (request.getContent().length() > 500) {
            throw new PostsHandler(ErrorStatus.POST_CONTENT_MAX_LENGTH_500);
        }

        if (request.getImageIds().size() > 3) {
            throw new PostsHandler(ErrorStatus.POST_IMAGE_LIMIT_EXCEEDED);
        }

        if (!user.getId().equals(post.getUser().getId())) {
            throw new UsersHandler(ErrorStatus.POST_NOT_AUTHORIZED);
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        // 기존 이미지 제거 및 새로운 이미지 추가
        if (request.getImageIds() != null && !request.getImageIds().isEmpty()) {
            for (Images image : post.getPostImageList()) {
                image.setPost(null);
            }
            post.getPostImageList().clear();
            postsRepository.save(post); // title과 content를 먼저 저장

            List<Images> images = imagesRepository.findAllById(request.getImageIds());
            for (Images image : images) {
                image.setPost(post);
            }
            post.getPostImageList().addAll(images);
        }
        postsRepository.save(post); // 변경된 내용 저장
    }

    // 게시글 삭제 메서드
    @Transactional
    public void deletePost(Long userId, Long postId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersHandler(ErrorStatus.USER_NOT_FOUND));

        Posts post = postsRepository.findByUserIdAndId(userId, postId)
                .orElseThrow(() -> new PostsHandler(ErrorStatus.POST_NOT_FOUND));

        if (!user.getId().equals(post.getUser().getId())) {
            throw new UsersHandler(ErrorStatus.POST_NOT_AUTHORIZED);
        }

        postsRepository.deleteById(post.getId());
    }

    // 메인페이지 게시글 3개 조회 (좋아요 순) 메서드
    @Transactional(readOnly = true)
    public List<PostsResponseDto.MainPageTopThreePostListDto> getMainPageTopThreePostList() {
        Pageable pageable = PageRequest.of(0, 3);
        List<Posts> topThreePost = postsRepository.findTopThreePosts(pageable);

        return topThreePost.stream()
                .map(PostsConverter::mainPageTopThreePostListDto)
                .collect(Collectors.toList());
    }

    // 게시글 5개씩 조회 메서드 (페이지당)
    @Transactional(readOnly = true)
    public PostsResponseDto.PostsInfoPageDto getPostsInfoResultsOnPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Posts> postsPage = postsRepository.findAll(pageRequest);

        if (postsPage.isEmpty()) {
            return PostsConverter.toPostInfoResultPageDto(
                    List.of(),
                    postsPage.getTotalPages(),
                    (int) postsPage.getTotalElements(),
                    postsPage.isFirst(),
                    postsPage.isLast(),
                    postsPage.getNumber() + 1,
                    postsPage.getNumberOfElements()
            );
        }

        List<PostsResponseDto.PostsInfoDto> postsInfoDtoList = postsPage.getContent().stream()
                .map(post -> {
                    Users user = post.getUser();
                    boolean checkLike = postLikesRepository.existsByUserAndPost(user, post);
                    boolean checkScrap = scrapsRepository.existsByUserAndPost(user, post);
                    String createdAt = getCreatedAt(post.getCreatedAt());
                    boolean isAuthor = user.getId().equals(post.getUser().getId());

                    return PostsConverter.toPostInfoResultDto(post, user, checkLike, checkScrap, createdAt, isAuthor);

                })
                .collect(Collectors.toList());

        return PostsConverter.toPostInfoResultPageDto(postsInfoDtoList,
                postsPage.getTotalPages(),
                (int) postsPage.getTotalElements(),
                postsPage.isFirst(),
                postsPage.isLast(),
                postsPage.getNumber() + 1,
                postsPage.getNumberOfElements()
        );
    }

    // 좋아요 생성 메서드
    @Transactional
    public ApiResponse<SuccessStatus> addLike(Long userId, Long postId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersHandler(ErrorStatus.USER_NOT_FOUND));
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostLikesHandler(ErrorStatus.POSTLIKE_NOT_FOUND));

        boolean alreadyLike = postLikesRepository.existsByUserAndPost(user, post);
        if(alreadyLike) {
            throw new PostLikesHandler(ErrorStatus.POSTLIKE_ALREADY_EXISTS);
        }
        PostLikes postLikes = PostLikes.builder()
                .user(user)
                .post(post)
                .build();

        post.incrementLikeNum();
        postLikesRepository.save(postLikes);
        postsRepository.save(post);

        try {
            return ApiResponse.onSuccess(SuccessStatus._OK);
        } catch (Exception e) {
            throw new PostLikesHandler(ErrorStatus.POSTLIKE_CREATE_FAIL);
        }
    }

    // 좋아요 해제 메서드
    @Transactional
    public void removeLike(Long userId, Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostLikesHandler(ErrorStatus.POSTLIKE_NOT_FOUND));

        PostLikes postLikes = postLikesRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new PostLikesHandler(ErrorStatus.POSTLIKE_NOT_FOUND));

        if (post.getLikeNum() == null || post.getLikeNum() == 0) {
            throw new PostLikesHandler(ErrorStatus.POSTLIKE_DELETE_FAIL);
        }

        post.decrementLikeNum();
        postsRepository.save(post);
        postLikesRepository.delete(postLikes);
    }

    // 스크랩 생성 메서드
    @Transactional
    public ApiResponse<SuccessStatus> createScraps(Long userId, Long postId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersHandler(ErrorStatus.USER_NOT_FOUND));
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostsHandler(ErrorStatus.POST_NOT_FOUND));
        boolean alreadyScrap = scrapsRepository.existsByUserAndPost(user, post);
        if (alreadyScrap) {
            throw new ScrapsHandler(ErrorStatus.SCRAP_ALREADY_EXISTS);
        }
        Scraps newScrap = Scraps.builder()
                .user(user)
                .post(post)
                .build();

        scrapsRepository.save(newScrap);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    // 스크랩 해제 메서드
    @Transactional
    public void deleteScraps(Long userId, Long postId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersHandler(ErrorStatus.USER_NOT_FOUND));
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostsHandler(ErrorStatus.POST_NOT_FOUND));

        Scraps scraps = scrapsRepository.findByUserAndPost(user, post);

        scrapsRepository.delete(scraps);

    }

    // 게시글 제목으로 검색하기 메서드
    @Transactional(readOnly = true)
    public PostsResponseDto.PostsInfoPageDto searchPostByTitle(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Posts> postsPage = postsRepository.findByTitle(keyword, pageable);

        if (postsPage.isEmpty()) {
            return PostsConverter.toPostInfoResultPageDto(
                    List.of(),
                    postsPage.getTotalPages(),
                    (int) postsPage.getTotalElements(),
                    postsPage.isFirst(),
                    postsPage.isLast(),
                    postsPage.getNumber() + 1,
                    postsPage.getNumberOfElements()
            );
        }

        List<PostsResponseDto.PostsInfoDto> postsInfoDtoList = postsPage.getContent().stream()
                .map(post -> {
                    Users user = post.getUser();
                    boolean checkLike = postLikesRepository.existsByUserAndPost(user, post);
                    boolean checkScrap = scrapsRepository.existsByUserAndPost(user, post);
                    String createdAt = getCreatedAt(post.getCreatedAt());
                    boolean isAuthor = post.getUser().getId().equals(user.getId());

                    return PostsConverter.toPostInfoResultDto(post, user, checkLike, checkScrap, createdAt, isAuthor);
                })
                .collect(Collectors.toList());
        return PostsConverter.toPostInfoResultPageDto(
                postsInfoDtoList,
                postsPage.getTotalPages(),
                (int) postsPage.getTotalElements(),
                postsPage.isFirst(),
                postsPage.isLast(),
                postsPage.getNumber() + 1,
                postsPage.getNumberOfElements()
        );
    }
}
