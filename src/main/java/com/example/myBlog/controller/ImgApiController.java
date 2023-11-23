package com.example.myBlog.controller;

import com.example.myBlog.entity.ImgEntity;
import com.example.myBlog.entity.PostEntity;
import com.example.myBlog.service.BlogService;
import com.example.myBlog.service.ImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/img")
public class ImgApiController {
    private final ImgService imgService;
    private final BlogService blogService;


    @DeleteMapping("/delete/{removingImgs}")
    public ResponseEntity<Void> deleteImg(@PathVariable String[] removingImgs){

        for (String s: removingImgs) {
            imgService.delete(s);

        //업로드한 이미지 서버 정적파일에서 삭제 기능
            File file = new File(System.getProperty("user.dir") + "/src/main/resources/static/files/" + s );
            if(file.exists()){
                file.delete();
                System.out.println("delete success");
            }else{
                System.out.println("delete fail");
            }
        }


        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<List<ImgEntity>> findArticle(@PathVariable long id){

        PostEntity postEntity = blogService.findById(id);

        List<ImgEntity> imgEntity = postEntity.getImgEntity();

        return ResponseEntity.ok()
                .body(imgEntity);
    }

}
