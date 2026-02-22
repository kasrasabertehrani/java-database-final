package com.project.code.Controller;

// 1. Spring Imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 2. Java Utility Imports
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// 3. Project Imports
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ServiceClass serviceClass;


    // 3. addProduct
    @PostMapping
    public ResponseEntity<Map<String, String>> addProduct(@RequestBody Product product) {
        Map<String, String> response = new HashMap<>();
        
        // FIXED: Actually use validateProduct (Returns true if it does NOT exist yet)
        if (!serviceClass.validateProduct(product)) {
            response.put("status", "error");
            response.put("message", "Product already exists!");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            productRepository.save(product);
            response.put("status", "success");
            response.put("message", "Product was added successfully!");
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            response.put("status", "error");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    // 4. getProductbyId
    @GetMapping("/{id}") // Cleaned up path to avoid /product/product/id
    public ResponseEntity<Map<String, Object>> getProductbyId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>(); // FIXED: Typo
        Optional<Product> productOpt = productRepository.findById(id);
        
        if (productOpt.isPresent()) {
            response.put("status", "success");
            response.put("products", productOpt.get()); // Instructions asked for "products"
            return ResponseEntity.ok(response);
        } else {
            // FIXED: Added missing fallback return
            response.put("status", "error");
            response.put("message", "Product not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    // 5. updateProduct
    @PutMapping
    public ResponseEntity<Map<String, String>> updateProduct(@RequestBody Product product) {
        Map<String, String> response = new HashMap<>(); // FIXED: Typo & Type
        
        productRepository.save(product);
        
        response.put("status", "success");
        response.put("message", "Product was successfully updated!");
        return ResponseEntity.ok(response);
    }


    // 6. filterbyCategoryProduct
    @GetMapping("/category/{name}/{category}")
    public ResponseEntity<Map<String, Object>> filterbyCategoryProduct(@PathVariable String name, @PathVariable String category) {
        List<Product> allProducts;
        Map<String, Object> response = new HashMap<>();
        
        if (category.equals("null")) {
            // FIXED: Called the repository
            allProducts = productRepository.findProductBySubName(name);
        } else if (name.equals("null")) {
            allProducts = productRepository.findByCategory(category);
        } else {
            allProducts = productRepository.findBySubNameAndCategory(category, name);
        }

        response.put("status", "successful");
        response.put("products", allProducts);
        return ResponseEntity.ok(response);
    }


    // 7. listProduct
    @GetMapping
    public ResponseEntity<Map<String, Object>> listProduct() {
        Map<String, Object> response = new HashMap<>();
        List<Product> allProducts = productRepository.findAll();
        
        response.put("status", "successful");
        response.put("products", allProducts);
        return ResponseEntity.ok(response);
    }


    // 8. getProductbyCategoryAndStoreId
    @GetMapping("/filter/{category}/{storeid}")
    public ResponseEntity<Map<String, Object>> getProductbyCategoryAndStoreId(@PathVariable String category, @PathVariable Long storeid) {
        Map<String, Object> response = new HashMap<>();
        List<Product> allProducts = productRepository.findByCategoryAndStoreId(storeid, category);
        
        response.put("status", "successful");
        response.put("product", allProducts); // Instructions asked for "product"
        return ResponseEntity.ok(response);
    }


    // 9. deleteProduct
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>(); // FIXED: Added missing map
        
        if (serviceClass.validateProductId(id)) {
            inventoryRepository.deleteByProductId(id);
            productRepository.deleteById(id); // FIXED: Uses standard deleteById
            
            response.put("status", "successful");
            response.put("message", "product was removed successfully!");
            return ResponseEntity.ok(response);
        } else {
            // FIXED: Added missing fallback return
            response.put("status", "error");
            response.put("message", "Product not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    // 10. searchProduct
    @GetMapping("/searchProduct/{name}")
    public ResponseEntity<Map<String, Object>> searchProduct(@PathVariable String name) {
        Map<String, Object> response = new HashMap<>();
        
        // FIXED: Actually assigned the result to the variable!
        List<Product> allProducts = productRepository.findProductBySubName(name);
        
        response.put("status", "successful");
        response.put("products", allProducts);
        return ResponseEntity.ok(response);
    }
}