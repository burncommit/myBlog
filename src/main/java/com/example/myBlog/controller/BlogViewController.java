package com.example.myBlog.controller;

import com.example.myBlog.dto.CommentRequest;
import com.example.myBlog.dto.MemberResponse;
import com.example.myBlog.dto.PostRequest;
import com.example.myBlog.dto.PostResponse;
import com.example.myBlog.entity.PostEntity;
import com.example.myBlog.service.BlogService;
import com.example.myBlog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;
    private final MemberService memberService;

    @GetMapping("/post/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0")
                          int page, @RequestParam(value = "kw", required = false) String kw){
        Page<PostEntity> paging = blogService.getPage(kw  , page);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "postList";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/create")
    public String postCreate(PostRequest postRequest){
        return "post_form";
    }
    //  PostRequest --> @NotEmpty, @Size 등으로 설정한 검증 기능 동작
// BindingResult 매개변수는 @Valid 애너테이션으로 인해 검증이 수행된 결과를 의미하는 객체이다.


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/post/create")
    public String postCreate(@Valid PostRequest postRequest,
                             BindingResult bindingResult, Principal principal, MultipartFile file)
    throws Exception{
        if(bindingResult.hasErrors()){
            return "post_form";
        }
        if(principal == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "등록 권한이 없습니다.");
        }
        MemberResponse memberResponse = memberService.getMember(principal.getName());
        blogService.create(postRequest.getTitle(), postRequest.getContent(), memberResponse, file);
        return "redirect:/";
    }

    @GetMapping("/post/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id, CommentRequest commentRequest){
        PostResponse postResponse = blogService.getPost(id);

        model.addAttribute("postResponse", postResponse);
        return "post_detail";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/delete/{id}")
    public String postDelete(@PathVariable("id") Long id){
        PostResponse postResponse = blogService.getPost(id);

        blogService.delete(postResponse);
        return "redirect:/";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/update/{id}")
    public String postUpdate(PostRequest postRequest, @PathVariable("id")Long id, Principal principal){ //@ModelAttribute 생략된거임
        PostResponse postResponse = blogService.getPostNoReadCnt(id);
        if(!postResponse.getAuthor().getNickname().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다");
        }
        postRequest.setTitle(postResponse.getTitle());
        postRequest.setContent(postResponse.getContent());
        postRequest.setFilePath(postResponse.getFilePath());
        postRequest.setFileName(postResponse.getFileName());

        return "post_update";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/post/update/{id}")
    public String postUpdate(@Valid PostRequest postRequest,BindingResult bindingResult, @PathVariable("id") Long id
            ,Principal principal, MultipartFile file)throws Exception{
        if(bindingResult.hasErrors()){
            return "post_update";
        }
        PostResponse postResponse = blogService.getPostNoReadCnt(id);
        if(!postResponse.getAuthor().getNickname().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다");
        }
        blogService.update(postResponse, postRequest.getTitle(), postRequest.getContent(), file);
        return "redirect:/post/detail/" + id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/vote/{id}")
    public String postVote(Principal principal, @PathVariable("id") Long id){
        PostResponse postResponse = blogService.getPostNoReadCnt(id);
        MemberResponse memberResponse = memberService.getMember(principal.getName());
//        System.out.println("===========" + postResponse.getAuthor().getNickname());
//        System.out.println(postResponse.getCreatedDate());
        //postResponse는 잘 전달되는중
        blogService.vote(postResponse , memberResponse);
        return String.format("redirect:/post/detail/%s", id);
    }


}
