package com.example.myBlog.controller;

import com.example.myBlog.dto.CommentRequest;
import com.example.myBlog.dto.CommentResponse;
import com.example.myBlog.dto.MemberResponse;
import com.example.myBlog.dto.PostResponse;
import com.example.myBlog.service.BlogService;
import com.example.myBlog.service.CommentService;
import com.example.myBlog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final BlogService blogService;
    private final CommentService commentService;
    private final MemberService memberService;

    //    현재 로그인한 사용자에 대한 정보를 알기 위해서는 스프링 시큐리티가 제공하는 Principal 객체를 사용해야 한다.
//    메서드에 Principal 객체를 매개변수로 지정하면 된다.

    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Long id,
                                @Valid CommentRequest commentRequest, BindingResult bindingResult, Principal principal){
        PostResponse postResponse = blogService.getPostNoReadCnt(id);
        MemberResponse memberResponse = memberService.getMember(principal.getName());//현재 로그인한 사용자의 사용자명(nickname)
        if (bindingResult.hasErrors()){
            model.addAttribute("postResponse" , postResponse);
            return "post_detail";
        }
        CommentResponse commentResponse = commentService.create(postResponse, commentRequest.getContent(), memberResponse);
        return String.format("redirect:/post/detail/%s#comment_%s",
                commentResponse.getPostResponse().getId(), commentResponse.getId());
    }

}
