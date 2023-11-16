package com.example.myBlog.service;


import com.example.myBlog.config.DataNotFoundException;
import com.example.myBlog.dto.MemberResponse;
import com.example.myBlog.dto.PostResponse;
import com.example.myBlog.entity.MemberEntity;
import com.example.myBlog.entity.PostEntity;
import com.example.myBlog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;


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

    public void create(String title, String content ,MemberResponse memberResponse){
//        PostResponse postResponse = new PostResponse();
//        postResponse.setTitle(title);
//        postResponse.setContent(content);
        // 작성자(Member) 정보를 DB에서 가져온 MemberResponse에서 설정
        MemberEntity author = MemberEntity.builder()
                .id(memberResponse.getId())
                .build();
//        postResponse.setAuthor(author);




        //PostEntity  postEntity = postResponse.toEntity();
        PostEntity postEntity = PostEntity.builder()

                .title(title)
                .content(content)
                .author(author)
                .build();


        blogRepository.save(postEntity);

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
    }

    public PostResponse update(PostResponse postResponse, String title, String content){
        postResponse.setTitle(title);
        postResponse.setContent(content);
        postResponse.setModifiedDate(LocalDateTime.now());
        PostEntity postEntity = PostEntity.builder()
                .id(postResponse.getId())
                .title(postResponse.getTitle())
                .content(postResponse.getContent())
                .author(postResponse.getAuthor())
                .voter(postResponse.getVoter().stream().map(MemberResponse::toEntity).collect(Collectors.toSet()))
                .createdDate(postResponse.getCreatedDate())
                .modifiedDate(postResponse.getModifiedDate())
                .readCnt(postResponse.getReadCnt())
                .build();




        blogRepository.save(postEntity);
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
