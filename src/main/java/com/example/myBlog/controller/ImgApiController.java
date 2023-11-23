package com.example.myBlog.controller;

import com.example.myBlog.service.ImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/img")
public class ImgApiController {
    private final ImgService imgService;






}
