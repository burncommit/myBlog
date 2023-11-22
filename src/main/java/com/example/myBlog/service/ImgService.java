package com.example.myBlog.service;

import com.example.myBlog.entity.ImgEntity;
import com.example.myBlog.entity.PostEntity;
import com.example.myBlog.repository.ImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImgService {



    private final ImgRepository imgRepository;



    public ImgEntity saveImg(MultipartFile files, PostEntity postEntity) throws IOException {
        if (files.isEmpty()){
            return null;
        }

        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";//저장경로지정

        UUID uuid = UUID.randomUUID();//파일이름에 붙일 랜덤 식별자

        String fileName = uuid + "_" + files.getOriginalFilename();//랜덤 이름을 붙이고

        File saveFile = new File(projectPath, fileName);

        files.transferTo(saveFile);

        ImgEntity imgEntity = ImgEntity.builder() //이미지 엔티티 생성
                .postEntity(postEntity)
                .fileName(fileName)
                .filePath("/files/" + fileName)
                .build();

        return imgRepository.save(imgEntity);

    }
}
