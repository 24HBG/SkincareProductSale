package com.example.demo.api;

import com.example.demo.entity.Review;
import com.example.demo.entity.request.ReviewRequest;
import com.example.demo.service.ReviewService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@SecurityRequirement(name = "api")
public class ReviewAPI {
    @Autowired
    ReviewService reviewService;

    @GetMapping
    public List<Review> getAllReview(){
        return reviewService.getAllReviews();
    }

    @PostMapping
    public ResponseEntity<Review> createdReview(@RequestBody ReviewRequest reviewRequest){
        Review newReview = reviewService.creatd(reviewRequest);
        return ResponseEntity.ok(newReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Review> deletedReview(@PathVariable long id){
        Review review = reviewService.deletedReview(id);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable long id, @RequestBody ReviewRequest reviewRequest){
        Review review = reviewService.updateReview(id,reviewRequest);
        return ResponseEntity.ok(review);
    }
}
