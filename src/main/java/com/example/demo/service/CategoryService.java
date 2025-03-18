package com.example.demo.service;

import com.example.demo.entity.Blog;
import com.example.demo.entity.Category;
import com.example.demo.entity.request.CategoryRequest;
import com.example.demo.exception.exceptions.EntityNotFoundException;
import com.example.demo.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<Category> get(){
        return categoryRepository.findAll();
    }


    // POST
    public Category post(CategoryRequest categoryRequest){

        // request => entity
        Category category = modelMapper.map(categoryRequest, Category.class);
        return categoryRepository.save(category);
    }

    public Category update(long id, CategoryRequest categoryRequest) {
        // Tìm category theo ID
        Category category = categoryRepository.findCategoryById(id);

        // Nếu không tìm thấy, ném lỗi
        if (category == null) {
            throw new EntityNotFoundException("Category with ID " + id + " not found");
        }

        // Cập nhật thông tin từ categoryRequest
        modelMapper.map(categoryRequest, category);

        // Lưu lại category đã cập nhật
        return categoryRepository.save(category);
    }

    public Category delete(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        category.setDeleted(true);
        return categoryRepository.save(category);
    }
}
