package com.project.code.Controller;

// 1. Spring Web Imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 2. Java Utility Imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// 3. Project Model & Repo Imports
import com.project.code.Model.Customer;
import com.project.code.Model.Review;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.ReviewRepository;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // 3. Define the getReviews Method
    @GetMapping("/{storeId}/{productId}")
    public ResponseEntity<Map<String, Object>> getReviews(
            @PathVariable Long storeId, 
            @PathVariable Long productId) {

        // 1. Fetch the raw reviews from MongoDB
        List<Review> rawReviews = reviewRepository.findByStoreIdAndProductId(storeId, productId);
        
        // 2. Create a list to hold our "filtered" custom reviews
        List<Map<String, Object>> filteredReviews = new ArrayList<>();

        // 3. Loop through the raw reviews to build the custom response
        for (Review review : rawReviews) {
            Map<String, Object> customReview = new HashMap<>();
            
            // Add the comment and rating directly from the review
            customReview.put("comment", review.getComment());
            customReview.put("rating", review.getRating());

            // Fetch the Customer to get their actual Name
            Optional<Customer> customerOpt = customerRepository.findById(review.getCustomerId());
            
            if (customerOpt.isPresent()) {
                customReview.put("customerName", customerOpt.get().getName());
            } else {
                customReview.put("customerName", "Anonymous User"); // Safe fallback
            }

            // Add this customized review to our final list
            filteredReviews.add(customReview);
        }

        // 4. Wrap it in the final response Map
        Map<String, Object> response = new HashMap<>();
        response.put("reviews", filteredReviews);

        return ResponseEntity.ok(response);
    }
}