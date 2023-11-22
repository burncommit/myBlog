package com.example.myBlog.entity;

import com.example.myBlog.common.BaseTimeEntity;
import com.example.myBlog.dto.PostResponse;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PostEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 200)
    private String title;

    private String content;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int readCnt;

    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.REMOVE)
    private List<CommentEntity> commentList;
    //예제에서는 Team 객체에 MappedBy가 적용되어 있는 것을 볼 수 있는데 이 경우에는 Team 객체는 MEMBER 테이블을
    // 관리할 수 없고 Member 객체만이 권한을 받고 주인이 아닌 쪽은 읽기(조회)만 가능해진다.
    // 즉, MappedBy가 정의되지 않은 객체가 주인(Owner)가 되는 것이다.
    //일반적으로 외래키를 가진 객체를 주인으로 정의하는 것이 좋다.(예제도 외래키를 가진 Memeber 객체가 주인이 된 것이다.)
    //무결성 해치지 않기위해
    @ManyToOne
    private MemberEntity author;

    @ManyToMany
    Set<MemberEntity> voter;

    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.REMOVE)
    private List<ImgEntity> imgEntity;


    @Builder
    public PostEntity(Long id, String title, String content, List<CommentEntity> commentList, MemberEntity author
            , Set<MemberEntity> voter, LocalDateTime createdDate, LocalDateTime modifiedDate
    ,int readCnt,List<ImgEntity> imgEntity){
        this.id = id;
     this.title = title;
     this.content = content;
     this.commentList = commentList;
     this.author = author;
     this.voter = voter;
     this.setCreatedDate(createdDate);
     this.setModifiedDate(modifiedDate);
     this.readCnt = readCnt;
     this.imgEntity = imgEntity;
}

public void update(PostResponse postResponse){
        this.id = postResponse.getId();
        this.title = postResponse.getTitle();
        this.content = postResponse.getContent();
        this.setCreatedDate(postResponse.getCreatedDate());
        this.setModifiedDate(postResponse.getModifiedDate());
        this.readCnt = postResponse.getReadCnt();
        this.author = postResponse.getAuthor();
}
}
