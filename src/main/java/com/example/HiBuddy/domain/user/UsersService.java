package com.example.HiBuddy.domain.user;

import com.example.HiBuddy.domain.image.Images;
import com.example.HiBuddy.domain.image.ImagesRepository;
import com.example.HiBuddy.domain.image.ImagesService;
import com.example.HiBuddy.domain.post.Posts;
import com.example.HiBuddy.domain.post.PostsRepository;
import com.example.HiBuddy.domain.post.dto.response.PostsResponseDto;
import com.example.HiBuddy.domain.scrap.Scraps;
import com.example.HiBuddy.domain.scrap.ScrapsRepository;
import com.example.HiBuddy.domain.scrap.response.ScrapsResponseDto;
import com.example.HiBuddy.domain.user.dto.request.UsersRequestDto;
import com.example.HiBuddy.domain.user.dto.response.UsersResponseDto;
import com.example.HiBuddy.global.response.PagedResponse;
import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import com.example.HiBuddy.global.response.exception.handler.PostsHandler;
import com.example.HiBuddy.global.response.exception.handler.ScrapsHandler;
import com.example.HiBuddy.global.response.exception.handler.UsersHandler;
import com.example.HiBuddy.global.s3.S3Service;
import com.example.HiBuddy.global.s3.dto.S3Result;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
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

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final ImagesService imagesService;
    private final ImagesRepository imagesRepository;
    private final S3Service s3Service;
    private final PostsRepository postsRepository;
    private final ScrapsRepository scrapsRepository;

    public Optional<UsersResponseDto.UsersMyPageDto> getUserDTOById(Long id) {
        return usersRepository.findById(id).map(user -> {
            Hibernate.initialize(user.getProfileImage()); // Lazy-loaded 관계 초기화
            return new UsersResponseDto.UsersMyPageDto(
                    user.getNickname(),
                    user.getCountry(),
                    user.getMajor(),
                    user.getProfileImage() != null ? user.getProfileImage() : null
            );
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
    public void deleteUser(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
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

        // 기존 프로필 이미지 삭제
        if (user.getProfileImage() != null) {
            imagesService.deleteImages(user.getProfileImage().getId());
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
        user.setProfileImage(savedImage);
        usersRepository.save(user);

        return savedImage;
    }

    @Transactional
    public PagedResponse<PostsResponseDto.PostsDto> getPostsByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Posts> postsPage = postsRepository.findByUserId(userId, pageable);

        if (postsPage.isEmpty()) {
            throw new PostsHandler(ErrorStatus.POST_NOT_FOUND);
        }

        List<PostsResponseDto.PostsDto> postsDtos = postsPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                postsDtos,
                postsPage.getNumber(),
               /* postsPage.getSize(),*/
                postsPage.getTotalElements(),
                postsPage.getTotalPages(),
                postsPage.isFirst(),
                postsPage.isLast()
        );
    }
    private PostsResponseDto.PostsDto convertToDto(Posts post) {
        List<PostsResponseDto.PostImageDto> postImages = post.getPostImageList().stream()
                .map(image -> new PostsResponseDto.PostImageDto(image.getId(), image.getUrl()))
                .limit(3)
                .collect(Collectors.toList());

        return PostsResponseDto.PostsDto.builder()
                .postId(post.getId())
                .likeNum(post.getPostLikeList().size())
                /*.commentNum(post.getComments().size())*/ // 아직 구현 안되어 있음
                .checkLike(false)
                .checkScrab(false)
                .checkMine(true)
                .createdAt(post.getCreatedAt().toString())
                .user(new UsersResponseDto.UsersPostDto(
                        post.getUser().getId(),
                        post.getUser().getCountry(),
                        post.getUser().getMajor(),
                        post.getUser().getNickname()
                ))
                .postImages(postImages)
                .build();
    }

    @Transactional
    public PagedResponse<ScrapsResponseDto.ScrabsDto> getScrabsByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Scraps> scrabsPage = scrapsRepository.findByUserId(userId, pageable);

        if (scrabsPage.isEmpty()) {
            throw new ScrapsHandler(ErrorStatus.POST_SCRAP_LIST_NOT_FOUND);
        }

        List<ScrapsResponseDto.ScrabsDto> scrabsDtos = scrabsPage.getContent().stream()
                .map(scrab -> convertToScrabsDto(scrab.getPost()))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                scrabsDtos,
                scrabsPage.getNumber(),
                /*scrabsPage.getSize(),*/
                scrabsPage.getTotalElements(),
                scrabsPage.getTotalPages(),
                scrabsPage.isFirst(),
                scrabsPage.isLast()
        );
    }

    private ScrapsResponseDto.ScrabsDto convertToScrabsDto(Posts post) {
        List<PostsResponseDto.PostImageDto> postImages = post.getPostImageList().stream()
                .map(image -> new PostsResponseDto.PostImageDto(image.getId(), image.getUrl()))
                .limit(3)
                .collect(Collectors.toList());

        return ScrapsResponseDto.ScrabsDto.builder()
                .postId(post.getId())
                .likeNum(post.getPostLikeList().size())
                // .commentNum(post.getComments().size()) // Uncomment if comments are available
                .checkLike(false)
                .checkScrab(true)
                .checkMine(false)
                .createdAt(post.getCreatedAt().toString())
                .user(new UsersResponseDto.UsersPostDto(
                        post.getUser().getId(),
                        post.getUser().getCountry(),
                        post.getUser().getMajor(),
                        post.getUser().getNickname()
                ))
                .postImages(postImages)
                .build();
    }
}


