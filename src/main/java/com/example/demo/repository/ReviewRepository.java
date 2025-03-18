package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.example.demo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    Optional<Review> findById (long id);
    Review findReviewById (long id);
}
