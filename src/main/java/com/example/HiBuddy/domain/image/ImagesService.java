package com.example.HiBuddy.domain.image;

import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.domain.user.UsersRepository;
import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import com.example.HiBuddy.global.response.exception.handler.ImagesHandler;
import com.example.HiBuddy.global.response.exception.handler.UsersHandler;
import com.example.HiBuddy.global.s3.S3Service;
import com.example.HiBuddy.global.s3.dto.S3Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImagesService {
    private final ImagesRepository imagesRepository;
    private final UsersRepository usersRepository;
    private final S3Service s3Service;

    @Transactional
    public List<Images> uploadImages(List<MultipartFile> fileList, Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new UsersHandler(ErrorStatus.USER_NOT_FOUND));

        List<Images> savedImages = new ArrayList<>();
        for(MultipartFile file : fileList) {
            S3Result s3Result = s3Service.uploadFile(file);

            Images newImages = new Images();
            newImages.setName(file.getOriginalFilename());
            newImages.setUrl(s3Result.getFileUrl());
            newImages.setType(file.getContentType());
            newImages.setUser(user);

            savedImages.add(imagesRepository.save(newImages));
        }

        return savedImages;
    }

    @Transactional
    public void deleteImages(Long imageId) {
        Images image = imagesRepository.findById(imageId).orElseThrow(() -> new ImagesHandler(ErrorStatus.IMAGE_NOT_FOUND));
        String imageUrl = image.getUrl();
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        s3Service.deleteFile(fileName);
        imagesRepository.delete(image);

    }
}
