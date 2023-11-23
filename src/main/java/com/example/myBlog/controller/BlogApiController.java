package com.example.myBlog.controller;

import com.example.myBlog.dto.PostRequest;
import com.example.myBlog.dto.PostResponse;
import com.example.myBlog.entity.PostEntity;
import com.example.myBlog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class BlogApiController {

    private final BlogService blogService;



    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/update/{id}")
    public String postUpdate2(@Valid PostRequest postRequest, BindingResult bindingResult, @PathVariable("id") Long id
            , Principal principal, List<MultipartFile> files)throws Exception{
        if(bindingResult.hasErrors()){
            return "post_update";
        }
        PostResponse postResponse = blogService.getPostNoReadCnt(id);
        if(!postResponse.getAuthor().getNickname().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다");
        }
        blogService.update(postResponse, postRequest.getTitle(), postRequest.getContent(), files);
        return "redirect:/post/detail/" + id;
    }
















}
