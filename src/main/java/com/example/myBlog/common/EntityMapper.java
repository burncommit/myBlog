package com.example.myBlog.common;

public interface EntityMapper <DTO, Entity> {
    Entity toEntity(DTO dto);
}
