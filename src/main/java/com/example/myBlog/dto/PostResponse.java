package com.example.myBlog.dto;

import com.example.myBlog.entity.CommentEntity;
import com.example.myBlog.entity.ImgEntity;
import com.example.myBlog.entity.MemberEntity;
import com.example.myBlog.entity.PostEntity;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Data
@Setter
@NoArgsConstructor
@Component
public class PostResponse {
    private Long id;
    private  String title;
    private  String content;
    private int readCnt;
    private LocalDateTime modifiedDate;
    private LocalDateTime createdDate;
    private List<CommentEntity> commentList;
    private MemberEntity author;
    private Set<MemberResponse> voter;
    private List<ImgEntity> imgEntity;

    @Builder
    public PostResponse(PostEntity postEntity){
        this.id = postEntity.getId();
        this.title = postEntity.getTitle();
        this.content = postEntity.getContent();
        this.readCnt = postEntity.getReadCnt();
        this.createdDate = postEntity.getCreatedDate();
        this.commentList = postEntity.getCommentList();
        this.modifiedDate = postEntity.getModifiedDate();
        this.author = postEntity.getAuthor();
       //this.voter = postEntity.getVoter().stream().map(MemberResponse::new).collect(Collectors.toSet());
        this.voter = postEntity.getVoter().stream().map(MemberEntity::toDto).collect(Collectors.toSet());
        this.imgEntity = postEntity.getImgEntity();
    }
    public PostEntity toEntity(){
        return PostEntity.builder()
                .id(id)
                .title(title)
                .content(content)
                .author(author)
                .voter(voter.stream().map(MemberResponse::toEntity).collect(Collectors.toSet()))
                .imgEntity(imgEntity)
                .build();
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }


}
