package com.project.code.Repo;

// 1. Spring Data Imports
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// 2. Java Utility Imports
import java.util.List;
import java.util.Optional;

// 3. Model Import
import com.project.code.Model.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    // 1. Find by ID
    // It's best practice to return Optional in case the ID doesn't exist.
    Optional<Store> findById(Long id);

    // 2. Find Store by Substring of Name
    // INSTRUCTION FIX: The goal is to find STORES, not Products.
    // We select 's' (the Store) where s.name matches the pattern.
    @Query("SELECT s FROM Store s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :pname, '%'))")
    List<Store> findBySubName(@Param("pname") String pname);

}