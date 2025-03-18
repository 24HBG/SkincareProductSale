package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Product;
import com.example.demo.entity.Review;
import com.example.demo.entity.request.ReviewRequest;
import com.example.demo.exception.exceptions.EntityNotFoundException;
import com.example.demo.repository.AuthenticationRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AuthenticationRepository authenticationRepository;
    @Autowired
    ProductRepository productRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review creatd(ReviewRequest reviewRequest) {
        Review review = new Review();
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        Account account = authenticationRepository.findById(reviewRequest.getAccountId());
        if (account == null) {
            throw new RuntimeException("Account does not exist");
        }
        Product product = productRepository.findById(reviewRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product does not exist"));

        review.setAccount(account);
        review.setProduct(product);
        return reviewRepository.save(review);
    }

    public Review deletedReview(Long id) {
        Review review = reviewRepository.findReviewById(id);
        review.isDeleted = true;
        return reviewRepository.save(review);
    }

    public Review updateReview(long id, ReviewRequest reviewRequest) {
        Review review = reviewRepository.findReviewById(id);
        if (review == null) {
            throw new EntityNotFoundException("Review with ID " + id + " not found");
        }

        // Cập nhật dữ liệu từ ReviewRequest vào Review (bỏ qua ID)
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());

        // Tìm Account và Product từ database
        Account account = authenticationRepository.findById(reviewRequest.getAccountId());
        Product product = productRepository.findById(reviewRequest.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product with ID " + reviewRequest.getProductId() + " not found"));

        // Gán Account và Product vào Review
        review.setAccount(account);
        review.setProduct(product);

        return reviewRepository.save(review);
    }
}
