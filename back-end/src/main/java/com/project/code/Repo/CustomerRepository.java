package com.project.code.Repo;

// 1. Spring Data JPA Imports
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 2. Java Utility Imports
import java.util.Optional;
import java.util.List;

import com.project.code.Model.Customer;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optinal<Customer> findByEmail(String email);
    List<Customer> findByName(String name);

}


