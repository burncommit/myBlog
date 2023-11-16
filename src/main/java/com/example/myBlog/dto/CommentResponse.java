package com.example.myBlog.dto;

import com.example.myBlog.entity.CommentEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private PostResponse postResponse;
    private MemberResponse author;

    @Builder
    public CommentResponse(CommentEntity commentEntity){
        this.id = commentEntity.getId();
        this.content = commentEntity.getContent();
        this.postResponse = new PostResponse(commentEntity.getPostEntity());
        this.author = commentEntity.getAuthor().toDto();
    }
    public CommentEntity toEntity(){
        return CommentEntity.builder()
                .id(id)
                .content(content)
                .postEntity(postResponse.toEntity())
                .author(author.toEntity())
                .build();
    }
}
