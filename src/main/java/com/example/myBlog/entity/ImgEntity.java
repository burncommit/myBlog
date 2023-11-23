package com.example.myBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore //PostEntity와 json객체로 파싱할때 순환참조 문제때문에 한곳에 걸어줌
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
