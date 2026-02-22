package com.project.code.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceClass {

    @Autowired 
    private ProductRepository productRepository;
    
    @Autowired 
    private InventoryRepository inventoryRepository;
    
    // 1. validateInventory Method
    // Returns FALSE if it exists, TRUE if it does not exist.
    // Using .isEmpty() makes this a clean one-liner!
    public boolean validateInventory(Inventory inventory) {
        return inventoryRepository.findByProductIdAndStoreId(
                inventory.getProduct().getId(), 
                inventory.getStore().getId()
        ).isEmpty(); 
    }

    // 2. validateProduct Method
    // Returns FALSE if it exists, TRUE if it does not exist.
    public boolean validateProduct(Product product) {
        return productRepository.findByName(product.getName()).isEmpty();
    }

    // 3. validateProductId Method
    // CRITICAL FIX: Added 'boolean' return type and lowercase 'v'.
    // Returns TRUE if it exists, FALSE if it does not exist.
    public boolean validateProductId(Long id) {
        return productRepository.findById(id).isPresent();
    }

    // 4. getInventoryId Method
    // Note: It's safer to return the object directly rather than re-assigning the parameter variable.
    public Inventory getInventoryId(Inventory inventory) {
        return inventoryRepository.findByProductIdAndStoreId(
                inventory.getProduct().getId(), 
                inventory.getStore().getId()
        ).get(); 
    }
}