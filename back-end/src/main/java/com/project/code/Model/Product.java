package com.project.code.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message ="name cannot be null")
    private String name;

    @NotNull(message ="category cannot be null")
    private String category;

    @NotNull(message ="price cannot be null")
    private Double price;

    @NotNull
    @Table(name = "product", uniqueConstraints = @UniqueConstraint(columnNames = "sku"))
    private String sku;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference("inventory-product")
    private List<Inventory> inventory;

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getCategory(){
        return category;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public Double getPrice(){
        return price;
    }
    public void setPrice(Double price){
        this.price = price;
    }
    public String getSku(){
        return sku;
    }
    public void setSku(String sku){
        this.sku = sku;
    }

}


