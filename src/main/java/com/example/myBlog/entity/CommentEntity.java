package com.example.myBlog.entity;

import com.example.myBlog.common.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Getter
@Setter
@Component
@NoArgsConstructor
@Entity
public class CommentEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private PostEntity postEntity;

    @ManyToOne
    private MemberEntity author;



    @Builder
    public CommentEntity(Long id, String content, PostEntity postEntity, MemberEntity author){
        this.id = id;
        this.content = content;
        this.postEntity = postEntity;
        this.author = author;
    }

}
