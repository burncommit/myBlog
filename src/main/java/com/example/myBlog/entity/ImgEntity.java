package com.example.myBlog.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class ImgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //파일번호

    @ManyToOne
    private PostEntity postEntity; // 게시글번호

    private String fileName;
    private String filePath;

    @Builder
    public ImgEntity(Long id, PostEntity postEntity, String fileName, String filePath){
        this.id = id;
        this.postEntity = postEntity;
        this.fileName = fileName;
        this.filePath = filePath;
    }

}
