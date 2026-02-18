package com.project.code.DTO; // Or com.project.code.Model

import java.util.List;

public class PlaceOrderRequestDTO {

    // 1. Who? (Customer Data)
    // We send strings, not the full Customer object, because we might need to create a new one.
    private String email;
    private String name;
    private String phone;

    // 2. Where? (Store ID)
    private Long storeId;

    // 3. What & How Many? (List of Items)
    private List<ItemRequest> items;

    // ==========================================
    // INNER CLASS: Groups Product ID + Quantity
    // ==========================================
    public static class ItemRequest {
        private Long productId;
        private int quantity;

        // Getters and Setters for ItemRequest
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    // Getters and Setters for the Main DTO
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }

    public List<ItemRequest> getItems() { return items; }
    public void setItems(List<ItemRequest> items) { this.items = items; }
}