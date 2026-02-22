package com.project.code.Controller;

// 1. Spring Imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 2. Java Utility Imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// 3. Project Imports
import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ServiceClass serviceClass;

    // 3. updateInventory
    @PutMapping
    public ResponseEntity<Map<String, String>> updateInventory(@RequestBody CombinedRequest combinedRequest) {
        Map<String, String> response = new HashMap<>();
        
        if (serviceClass.validateProductId(combinedRequest.getProduct().getId())) {
            combinedRequest.getInventory().setProduct(combinedRequest.getProduct());
            
            // MISSING STEP FIXED: We actually save it to the DB now!
            inventoryRepository.save(combinedRequest.getInventory());
            
            response.put("status", "success");
            response.put("message", "Inventory was updated successfully!");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Data not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // 4. saveInventory
    @PostMapping
    public ResponseEntity<Map<String, String>> saveInventory(@RequestBody Inventory inventory) {
        Map<String, String> response = new HashMap<>();
        
        if (serviceClass.validateInventory(inventory)) { // True if it DOES NOT exist
            inventoryRepository.save(inventory);
            response.put("status", "success");
            response.put("message", "Inventory was created successfully!");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Inventory already exists!");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 5. getAllProducts
    @GetMapping("/{storeId}")
    public ResponseEntity<Map<String, Object>> getAllProducts(@PathVariable Long storeId) {
        List<Product> allProducts = productRepository.findByStoreId(storeId);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "successful");
        response.put("products", allProducts); // Fixed syntax error here
        return ResponseEntity.ok(response);
    }

    // 6. getProductName (Filter)
    @GetMapping("/filter/{category}/{name}/{storeid}")
    public ResponseEntity<Map<String, Object>> getProductName(
            @PathVariable String category, 
            @PathVariable String name, 
            @PathVariable Long storeid) {
            
        List<Product> allProducts;
        Map<String, Object> response = new HashMap<>();
        
        // FIXED: Use .equals() for String comparison
        if (category.equals("null")) {
            allProducts = productRepository.findByNameLike(storeid, name);
        } else if (name.equals("null")) {
            allProducts = productRepository.findByCategoryAndStoreId(storeid, category);
        } else {
            allProducts = productRepository.findByNameAndCategory(storeid, name, category);
        }
        
        response.put("status", "successful");
        response.put("product", allProducts); // Instructions said "product", not "products"
        return ResponseEntity.ok(response);
    }

    // 7. searchProduct
    @GetMapping("/search/{name}/{storeId}")
    public ResponseEntity<Map<String, Object>> searchProduct(@PathVariable String name, @PathVariable Long storeId) {
        List<Product> allProducts = productRepository.findByNameLike(storeId, name);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "successful");
        response.put("product", allProducts); // Instructions said "product"
        return ResponseEntity.ok(response);
    }

    // 8. removeProduct
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> removeProduct(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        
        if (serviceClass.validateProductId(id)) {
            // FIXED: Using standard deleteById for the Product repository
            productRepository.deleteById(id);
            inventoryRepository.deleteByProductId(id);
            
            response.put("message", "Product was removed from product and inventory repositories successfully!");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Data not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // 9. validateQuantity
    @GetMapping("/validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(
            @PathVariable int quantity, // FIXED: Added 'int'
            @PathVariable Long storeId, 
            @PathVariable Long productId) {
            
        // SAFTEY FIX: Use Optional to prevent crashes if inventory doesn't exist
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductIdAndStoreId(productId, storeId);
        
        if (inventoryOpt.isPresent()) {
            return inventoryOpt.get().getStockLevel() >= quantity;
        }
        
        // If inventory doesn't exist at all, return false
        return false; 
    }
}