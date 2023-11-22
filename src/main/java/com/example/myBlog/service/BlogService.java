package com.example.myBlog.service;


import com.example.myBlog.config.DataNotFoundException;
import com.example.myBlog.dto.MemberResponse;
import com.example.myBlog.dto.PostResponse;
import com.example.myBlog.entity.ImgEntity;
import com.example.myBlog.entity.MemberEntity;
import com.example.myBlog.entity.PostEntity;
import com.example.myBlog.repository.BlogRepository;
import com.example.myBlog.repository.ImgRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final ImgService imgService;

    private final ImgRepository imgRepository;


    public List<PostEntity> findAll(){
        return blogRepository.findAll();
    }


    public Page<PostEntity> getPage(String kw, int page) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(Sort.Order.desc("createdDate")); //createdDate 필드를 내림차순으로 정렬, desc : 역순(최근 날짜순)

        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders)); //정렬 정보를 포함한 Pageable 생성

        //검색해서 보여 주는 기능
        if (kw != null && !kw.isEmpty()) {  //키워드가 null이 아니거라, 비어있지 않거나 둘 다 넣어서 확실하게 하려고
            return blogRepository.findByTitleContaining(kw, pageable); //키워드가 포함된 걸 보여주고
        } else {
            return this.blogRepository.findAll(pageable); //검색어가 비어 있으면 전체를 보여줘라
        }

    }

    public void create(String title, String content , MemberResponse memberResponse, List<MultipartFile> files)
    throws Exception{

        MemberEntity author = MemberEntity.builder()
                .id(memberResponse.getId())
                .build();

        PostEntity postEntity = PostEntity.builder()

                .title(title)
                .content(content)
                .author(author)
                .build();

        blogRepository.save(postEntity); //이미지 없이 나머지 컬럼들 저장

        List<ImgEntity> imgEntities = new ArrayList<>();

        for(MultipartFile m : files){

            imgEntities.add(imgService.saveImg(m, postEntity)); // postEntity에 매핑된 이미지 테이블 저장
        }


    }

    public PostResponse getPost(Long id){
        Optional<PostEntity> postEntity = blogRepository.findById(id);
        if(postEntity.isPresent()){

            PostEntity p1 = postEntity.get();
            p1.setReadCnt(p1.getReadCnt() + 1);

            blogRepository.save(p1);
            return new PostResponse(p1);

        }else{
            throw new DataNotFoundException("해당 글이 없습니다.");
        }
    }

    public PostResponse getPostNoReadCnt(Long id){
        Optional<PostEntity> postEntity = blogRepository.findById(id);
        if(postEntity.isPresent()){

            PostEntity p1 = postEntity.get();


            return new PostResponse(p1);

        }else{
            throw new DataNotFoundException("해당 글이 없습니다.");
        }
    }

    public void delete(PostResponse postResponse){
        blogRepository.deleteById(postResponse.getId());


        //업로드한 이미지 삭제 기능
//        File file = new File(System.getProperty("user.dir") + "/src/main/resources/static/files/" + postResponse.getFileName());
        for(ImgEntity img : postResponse.getImgEntity()){
            File file = new File(System.getProperty("user.dir") + "/src/main/resources/static/files/" + img.getFileName()   );
            if(file.exists()){
                file.delete();
                System.out.println("delete success");
            }else{
                System.out.println("delete fail");
            }
        }
        //File file = new File(System.getProperty("user.dir") + "/src/main/resources/static/files/" + postResponse.getImgEntity());

//        if(file.exists()){
//            file.delete();
//            System.out.println("delete success");
//        }else{
//            System.out.println("delete fail");
//        }



    }

    public PostResponse update(PostResponse postResponse, String title, String content, List<MultipartFile> files) throws Exception{

        postResponse.setTitle(title);
        postResponse.setContent(content);
        postResponse.setModifiedDate(LocalDateTime.now());

        if(files.isEmpty()){ //이미지 넘어온게 없다면 본래 이미지 그대로

            PostEntity postEntity = PostEntity.builder()
                    .id(postResponse.getId())
                    .title(postResponse.getTitle())
                    .content(postResponse.getContent())
                    .author(postResponse.getAuthor())
                    .voter(postResponse.getVoter().stream().map(MemberResponse::toEntity).collect(Collectors.toSet()))
                    .createdDate(postResponse.getCreatedDate())
                    .modifiedDate(postResponse.getModifiedDate())
                    .readCnt(postResponse.getReadCnt())
                    .imgEntity(postResponse.getImgEntity())
                    .build();
            blogRepository.save(postEntity);
        }else {

            PostEntity postEntity = PostEntity.builder()
                    .id(postResponse.getId())
                    .title(postResponse.getTitle())
                    .content(postResponse.getContent())
                    .author(postResponse.getAuthor())
                    .voter(postResponse.getVoter().stream().map(MemberResponse::toEntity).collect(Collectors.toSet()))
                    .createdDate(postResponse.getCreatedDate())
                    .modifiedDate(postResponse.getModifiedDate())
                    .readCnt(postResponse.getReadCnt())
                    .imgEntity(postResponse.getImgEntity())
                    .build();
            blogRepository.save(postEntity);

            List<ImgEntity> imgEntities = new ArrayList<>();

            for(MultipartFile m : files){

                imgEntities.add(imgService.saveImg(m, postEntity)); // postEntity에 매핑된 이미지 테이블 저장
            }
        }






        return postResponse;
    }

    public PostResponse vote(PostResponse postResponse, MemberResponse memberResponse){
        postResponse.getVoter().add(memberResponse);

        System.out.println(postResponse.getCreatedDate());

        //blogRepository.save(postResponse.toEntity());
        blogRepository.save(PostEntity.builder()
                        .id(postResponse.getId())
                        .title(postResponse.getTitle())
                        .content(postResponse.getContent())
                        .author(postResponse.getAuthor())
                .voter(postResponse.getVoter().stream().map(MemberResponse::toEntity).collect(Collectors.toSet()))
                        .createdDate(postResponse.getCreatedDate())
                        .modifiedDate(postResponse.getModifiedDate())
                .build()
    );
        return postResponse;
    }




}
