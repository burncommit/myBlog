package com.example.myBlog.dto;

import com.example.myBlog.entity.ImgEntity;
import com.example.myBlog.entity.PostEntity;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {
    private Long id;
    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(max = 200)
    private String title;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

    private List<ImgEntity> imgEntity;

    public PostRequest(PostEntity postEntity) {

        this.title = postEntity.getTitle();
        this.content = postEntity.getContent();
        this.imgEntity = postEntity.getImgEntity();
    }

}
