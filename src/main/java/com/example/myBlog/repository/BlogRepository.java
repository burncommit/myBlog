package com.example.myBlog.repository;

import com.example.myBlog.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findAll(Pageable pageable);
    Page<PostEntity> findByTitleContaining(String kw, Pageable pageable);
}
