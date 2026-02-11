package com.project.code.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonManagedReference
    private OrderDetails order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonManagedReference
    private Product product;

    private int quantity;

    private Double price;


    public OrderItem(OrderDetails order, Product product, int price){
        this.order = order;
        this.product = product;
        this.price = price;
    }
    public OrderItem(){}

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public OrderDetails getOrder(){
        return order;
    }
    public void setOrder(OrderDetails order){
        this.order = order;
    }
    public Product getProduct(){
        return product;
    }
    public void setProduct(Product product){
        this.product = product;
    }
    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public Double getPrice(){
        return price;
    }
    public void setPrice(Double price){
        this.price = price;
    }
}

