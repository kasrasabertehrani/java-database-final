package com.project.code.Repo;

// 1. Spring Data Imports
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// 2. Java Utility Imports
import java.util.List;
import java.util.Optional;

// 3. Model Import
import com.project.code.Model.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductIdAndStoreId(Long productId, Long storeId);
    List<Inventory> findByStore_Id(Long storeId);
    @Modifying
    @Transactional
    void deleteByProductId(Long productId);
}