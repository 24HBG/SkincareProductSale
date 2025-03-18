package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Blog;
import com.example.demo.entity.request.BlogRequest;
import com.example.demo.repository.AuthenticationRepository;
import com.example.demo.repository.BlogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    BlogRepository blogRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AuthenticationRepository authenticationRepository;

    public List<Blog> getAllBlog() {
        return blogRepository.findByIsDeletedFalse();
    }

    public Blog create(BlogRequest blogRequest) {
        Blog blog = modelMapper.map(blogRequest, Blog.class);
        Account account = authenticationRepository.findById(blogRequest.getAccountId()).
                orElseThrow(() -> new RuntimeException("Account does not exist"));
        blog.setAccount(account);
        return blogRepository.save(blog);
    }

    public Blog delete(long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        blog.setDeleted(true); // Xóa mềm bằng cách đặt isDeleted = true
        return blogRepository.save(blog);
    }

    public Blog update(long id, BlogRequest blogRequest) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        // Kiểm tra nếu blog đã bị xóa mềm
        if (blog.isDeleted()) {
            throw new RuntimeException("Cannot update a deleted blog.");
        }
        // Cập nhật thông tin blog
        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());

        return blogRepository.save(blog);
    }
}
