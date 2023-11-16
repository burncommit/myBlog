package com.example.myBlog.service;

import com.example.myBlog.dto.CommentResponse;
import com.example.myBlog.dto.MemberResponse;
import com.example.myBlog.dto.PostResponse;
import com.example.myBlog.entity.CommentEntity;
import com.example.myBlog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;


   public CommentResponse create(PostResponse postResponse, String content, MemberResponse author){
       CommentResponse commentResponse = new CommentResponse();
       commentResponse.setContent(content);
       commentResponse.setPostResponse(postResponse);
       commentResponse.setAuthor(author);

       CommentEntity commentEntity = commentResponse.toEntity();




       commentEntity = commentRepository.save(commentEntity); //author id가 없음






       return commentResponse;
   }
}
