package com.example.demo.repository;

import com.example.demo.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Long>{
    List<Blog> findByIsDeletedFalse();
}
