package com.project.code.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

@Entity
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message ="name cannot be null")
    private String name;

    @NotBlank(message ="Address cannot be blank")
    private String address;

    @OneToMany(mappedBy = "store")
    @JsonManagedReference("inventory-store")
    private List<Inventory> inventory;


    public Store(String name, String address){
        this.name = name;
        this.address = address;
    }
    public Store(){}


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
    public String getAddress(){
        return address;
    }
    public void setAddress(String address){
        this.address = address;
    }

}

