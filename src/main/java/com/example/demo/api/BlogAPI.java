package com.example.demo.api;

import com.example.demo.entity.Blog;
import com.example.demo.entity.request.BlogRequest;
import com.example.demo.service.BlogService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
@SecurityRequirement(name = "api")
public class BlogAPI {
    @Autowired
    BlogService blogService;
    @GetMapping
    public ResponseEntity<List<Blog>> getAllBlogs() {
        List<Blog> blogs = blogService.getAllBlog();
        return ResponseEntity.ok(blogs);
    }

    // Tạo blog mới
    @PostMapping
    public ResponseEntity<Blog> createBlog(@RequestBody BlogRequest blogRequest) {
        Blog newBlog = blogService.create(blogRequest);
        return ResponseEntity.ok(newBlog);
    }

    // Xóa mềm blog theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Blog> deleteBlog(@PathVariable long id) {
        Blog deletedBlog = blogService.delete(id);
        return ResponseEntity.ok(deletedBlog);
    }
}
