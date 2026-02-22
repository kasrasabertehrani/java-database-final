package com.project.code.Controller;

// 1. Spring Web Imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 2. Java Utility Imports
import java.util.HashMap;
import java.util.Map;

// 3. Project Model, Repo, and Service Imports
import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderService orderService;

    // 3. Define the addStore Method
    @PostMapping
    public ResponseEntity<Map<String, String>> addStore(@RequestBody Store store) {
        storeRepository.save(store);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Store was added successfully"); 
        return ResponseEntity.ok(response);
    }

    // 4. Define the validateStore Method
    @GetMapping("/validate/{storeId}") // Added leading slash for safety
    public boolean validateStore(@PathVariable Long storeId) {
        // Clean, one-line boolean return!
        return storeRepository.findById(storeId).isPresent();
    }

    // 5. Define the placeOrder Method
    @PostMapping("/placeOrder")
    public ResponseEntity<Map<String, String>> placeOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequestDTO) {
        Map<String, String> response = new HashMap<>();
        
        try {
            orderService.saveOrder(placeOrderRequestDTO);
            response.put("message", "Order was placed successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            // FIXED: Capitalized "Error" to strictly match instructions
            response.put("Error", e.getMessage()); 
            return ResponseEntity.badRequest().body(response);
        }
    }
}