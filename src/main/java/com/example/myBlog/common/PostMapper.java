package com.example.myBlog.common;

import com.example.myBlog.dto.PostRequest;
import com.example.myBlog.entity.PostEntity;

public class PostMapper implements EntityMapper<PostRequest, PostEntity> {

    @Override
    public PostEntity toEntity(PostRequest postRequest) {
//        PostEntity postEntity = new PostEntity();
//        postEntity.setTitle(postRequest.getTitle());
//        postEntity.setContent(postRequest.getContent());
//        postEntity.setCreatedDate(postRequest.get);
          return null;
    }
}
