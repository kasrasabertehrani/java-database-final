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
import com.project.code.Model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    // 1. Find all products (Standard)
    List<Product> findAll();

    // 2. Find by Category
    List<Product> findByCategory(String category);

    // 3. Find by Price Range
    // Spring automatically generates: WHERE price BETWEEN ? AND ?
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    // 4. Find by SKU (Unique ID)
    // Returns Optional because the SKU might not exist
    Optional<Product> findBySku(String sku);

    // 5. Find by Name (Exact Match)
    // Returns Optional because the name might not exist
    Optional<Product> findByName(String name);



    // 6. Find by Name Pattern in a specific Store
    // UPDATED: Added LOWER() and CONCAT() to match your other methods.
    // Logic: "Go to Inventory -> Check Store ID -> Check Product Name"
    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId AND LOWER(i.product.name) LIKE LOWER(CONCAT('%', :pname, '%'))")
    List<Product> findByNameLike(@Param("storeId") Long storeId, @Param("pname") String pname);

    // 7. Find by Name AND Category in a specific Store
    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId AND LOWER(i.product.name) LIKE LOWER(CONCAT('%', :pname, '%')) AND i.product.category = :category")
    List<Product> findByNameAndCategory(@Param("storeId") Long storeId, @Param("pname") String pname, @Param("category") String category);

    // 8. Find by Category in a specific Store
    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId AND i.product.category = :category")
    List<Product> findByCategoryAndStoreId(@Param("storeId") Long storeId, @Param("category") String category);

    // 9. Find Product by Name (Global Search)
    // 'i' here refers to the Product entity itself
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :pname, '%'))")
    List<Product> findProductBySubName(@Param("pname") String pname);

    // 10. Find All Products in a Store
    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId")
    List<Product> findByStoreId(@Param("storeId") Long storeId);

    // 11. Find by Name AND Category (Global Search)
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :pname, '%')) AND p.category = :category")
    List<Product> findBySubNameAndCategory(@Param("category") String category, @Param("pname") String pname);
}