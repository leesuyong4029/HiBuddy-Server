package com.example.HiBuddy.domain.user;

import com.example.HiBuddy.domain.image.Images;
import com.example.HiBuddy.domain.image.ImagesRepository;
import com.example.HiBuddy.domain.image.ImagesService;
import com.example.HiBuddy.domain.post.Posts;
import com.example.HiBuddy.domain.post.PostsConverter;
import com.example.HiBuddy.domain.post.PostsRepository;
import com.example.HiBuddy.domain.post.dto.response.PostsResponseDto;
import com.example.HiBuddy.domain.postLike.PostLikesRepository;
import com.example.HiBuddy.domain.scrap.Scraps;
import com.example.HiBuddy.domain.scrap.ScrapsConverter;
import com.example.HiBuddy.domain.scrap.ScrapsRepository;
import com.example.HiBuddy.domain.scrap.response.ScrapsResponseDto;
import com.example.HiBuddy.domain.test.TestsRepository;
import com.example.HiBuddy.domain.user.dto.request.UsersRequestDto;
import com.example.HiBuddy.domain.user.dto.response.UsersResponseDto;
import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import com.example.HiBuddy.global.response.exception.handler.UsersHandler;
import com.example.HiBuddy.global.s3.S3Service;
import com.example.HiBuddy.global.s3.dto.S3Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.HiBuddy.domain.post.PostsService.getCreatedAt;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final ImagesService imagesService;
    private final ImagesRepository imagesRepository;
    private final S3Service s3Service;
    private final PostsRepository postsRepository;
    private final PostLikesRepository postLikesRepository;
    private final ScrapsRepository scrapsRepository;
    private final TestsRepository testsRepository;

    @Transactional(readOnly = true)
    public Optional<UsersResponseDto.UsersMyPageDto> getUserDTOById(Long id) {
        return usersRepository.findById(id).map(user -> {
            String profileImageUrl = user.getProfileImage();

            return UsersResponseDto.UsersMyPageDto.builder()
                    .nickname(user.getNickname())
                    .country(user.getCountry())
                    .major(user.getMajor())
                    .profileImage(profileImageUrl)
                    .build();
        });
    }

    @Transactional
    public Long getUserId(UserDetails user) {
        String username = user.getUsername();
        Users users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsersHandler(ErrorStatus.USER_NOT_FOUND));
        return users.getId();
    }


    @Transactional
    public void deleteUser(Long userId) {
        Users user = usersRepository.findUsersById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User with userId " + userId + " not found"));
        // 연관된 엔티티 삭제
        imagesRepository.deleteAllByUser(user);
        postsRepository.deleteAllByUser(user);
        testsRepository.deleteAllByUser(user);

        // 사용자 삭제
        usersRepository.delete(user);
        usersRepository.delete(user);
    }

    @Transactional
    public UsersRequestDto.UserNicknameDto updateUserNickname(UserDetails userDetails, UsersRequestDto.UserNicknameDto userNicknameDto) {
        String username = userDetails.getUsername();
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
        user.setNickname(userNicknameDto.getNickname());
        usersRepository.save(user);
        return userNicknameDto;
    }

    @Transactional
    public UsersRequestDto.UserOnboardingDto onboardingData(UserDetails user, UsersRequestDto.UserOnboardingDto dto) {
        String username = user.getUsername();
        Users users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));

        users.setCountry(dto.getCountry());
        users.setMajor(dto.getMajor());
        users.setNickname(dto.getNickname());
        usersRepository.save(users);

        return dto;
    }


    @Transactional
    public Images uploadProfileImage(MultipartFile file, Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new UsersHandler(ErrorStatus.USER_NOT_FOUND));

        // 기존 프로필 이미지 삭제 (필요한 경우)
        if (user.getProfileImage() != null) {
            imagesService.deleteImagesByUrl(user.getProfileImage());
        }

        // 새로운 프로필 이미지 업로드
        S3Result s3Result = s3Service.uploadFile(file);

        Images newImage = new Images();
        newImage.setName(file.getOriginalFilename());
        newImage.setUrl(s3Result.getFileUrl());
        newImage.setType(file.getContentType());
        newImage.setUser(user);

        Images savedImage = imagesRepository.save(newImage);

        // 사용자 엔티티에 프로필 이미지 설정
        user.setProfileImage(s3Result.getFileUrl());
        usersRepository.save(user);

        return savedImage;
    }

    @Transactional(readOnly = true)
    public PostsResponseDto.PostsInfoPageDto getPostsByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Posts> postsPage = postsRepository.findByUserId(userId, pageable);

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


    @Transactional(readOnly = true)
    public ScrapsResponseDto.ScrapsInfoPageDto getScrapsByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Scraps> scrapsPage = scrapsRepository.findByUserId(userId, pageable);

        if (scrapsPage.isEmpty()) {
            return ScrapsConverter.toScrapsInfoResultPageDto(
                    List.of(),
                    scrapsPage.getTotalPages(),
                    (int) scrapsPage.getTotalElements(),
                    scrapsPage.isFirst(),
                    scrapsPage.isLast(),
                    scrapsPage.getNumber() + 1,
                    scrapsPage.getNumberOfElements()
            );
        }

        List<ScrapsResponseDto.ScrapsInfoDto> scrapsInfoDtoList = scrapsPage.getContent().stream()
                .map(scrap -> {
                    Posts post = scrap.getPost();
                    Users user = post.getUser();
                    boolean checkLike = postLikesRepository.existsByUserAndPost(user, post);
                    boolean checkScrap = scrapsRepository.existsByUserAndPost(user, post);
                    String createdAt = getCreatedAt(post.getCreatedAt());
                    boolean isAuthor = user.getId().equals(userId);

                    return ScrapsConverter.toScrapsInfoResultDto(post, user, checkLike, checkScrap, createdAt, isAuthor);

                })
                .collect(Collectors.toList());

        return ScrapsConverter.toScrapsInfoResultPageDto(
                scrapsInfoDtoList,
                scrapsPage.getTotalPages(),
                (int) scrapsPage.getTotalElements(),
                scrapsPage.isFirst(),
                scrapsPage.isLast(),
                scrapsPage.getNumber() + 1,
                scrapsPage.getNumberOfElements()
        );
    }

}