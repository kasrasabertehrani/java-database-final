package com.project.code.Model;

// 1. MongoDB Specific Imports
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// 2. Validation Imports (Still using Jakarta)
import jakarta.validation.constraints.NotNull;

@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    @NotNull(message ="Customer ID cannot be null")   
    private Long customerId;

    @NotNull(message ="Product ID cannot be null")
    private Long productId;

    @NotNull(message ="Store ID cannot be null")
    private Long storeId;

    @NotNull(message ="rating cannot be null")
    private int rating;

    private String comment;

    public Review(Long customerId, Long productId, Long storeId, int rating, String comment){
        this.customerId = customerId;
        this.productId = productId;
        this.storeId = storeId;
        this.rating = rating;
        this.comment = comment;
    }

    public Review(){}

    public Long getCustomerId(){
        return customerId;
    }
    public void setCustomerId(Long customerId){
        this.customerId = customerId;
    }
    public Long getProductId(){
        return productId;
    }
    public void setProductId(Long productId){
        this.productId = productId;
    }
    public Long getStoreId(){
        return storeId;
    }
    public void setStoreId(Long storeId){
        this.storeId = storeId;
    }
    public int getRating(){
        return rating;
    }
    public void setRating(int rating){
        this.rating = rating;
    }
    public String getComment(){
        return comment;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
}
