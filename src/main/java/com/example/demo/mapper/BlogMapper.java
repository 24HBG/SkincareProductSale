package com.example.demo.mapper;

import com.example.demo.entity.Blog;
import com.example.demo.entity.request.BlogRequest;
import org.modelmapper.PropertyMap;

import java.time.LocalDateTime;

public class BlogMapper extends PropertyMap<BlogRequest, Blog> {
    @Override
    protected void configure() {
        map().setId(0); // Đặt ID mặc định là 0 (JPA sẽ tự sinh)
        map().setCreatedAt(LocalDateTime.now()); // Gán thời gian tạo mặc định
    }
}
