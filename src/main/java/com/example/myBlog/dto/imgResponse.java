package com.example.myBlog.dto;

import com.example.myBlog.entity.ImgEntity;
import com.example.myBlog.entity.PostEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class imgResponse {
    private Long id;
    private PostResponse postResponse;
    private String fileName;
    private String filePath;
    private String originName;

    @Builder
    public imgResponse(ImgEntity imgEntity){
        this.id = imgEntity.getId();
        this.postResponse = new PostResponse(imgEntity.getPostEntity());
        this.fileName = imgEntity.getFileName();
        this.filePath = imgEntity.getFilePath();
        this.originName = imgEntity.getOriginName();
    }

    public ImgEntity toEntity(){
        return ImgEntity.builder()
                .id(id)
                .postEntity(postResponse.toEntity())
                .fileName(fileName)
                .filePath(filePath)
                .originName(originName)
                .build();
    }

}
