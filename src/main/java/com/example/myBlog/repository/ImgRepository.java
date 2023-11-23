package com.example.myBlog.repository;

import com.example.myBlog.entity.ImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImgRepository extends JpaRepository<ImgEntity, Long>    {

    ImgEntity findByFileNameContaining(String fileName);
}
