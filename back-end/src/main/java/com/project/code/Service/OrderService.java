package com.project.code.Service;

import com.project.code.Model.*;
import com.project.code.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired private ProductRepository productRepository;
    @Autowired private InventoryRepository inventoryRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private StoreRepository storeRepository;
    @Autowired private OrderDetailsRepository orderDetailsRepository;
    @Autowired private OrderItemRepository orderItemRepository;

    @Transactional
    public void saveOrder(PlaceOrderRequestDTO request) {

        // 1. Retrieve or Create Customer
        Customer customer = customerRepository.findByEmail(request.getCustomerEmail())
            .orElseGet(() -> {
                Customer newCustomer = new Customer();
                newCustomer.setName(request.getCustomerName());
                newCustomer.setEmail(request.getCustomerEmail());
                newCustomer.setPhone(request.getCustomerPhone());
                return customerRepository.save(newCustomer);
            });

        // 2. Retrieve Store
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        // ==========================================
        // 3. THE MAGIC TRICK: Save the Order FIRST
        // ==========================================
        OrderDetails order = new OrderDetails();
        order.setCustomer(customer);
        order.setStore(store);
        order.setDate(LocalDateTime.now());
        
        // Save it immediately so MySQL gives it an ID!
        order = orderDetailsRepository.save(order);

        double calculatedTotalPrice = 0.0;

        // 4. Process the Items
        for (PurchaseProductDTO itemDto : request.getPurchaseProduct()) {
            
            // Notice: itemDto.getId() is the Product ID in this specific DTO
            Product product = productRepository.findById(itemDto.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Inventory inventory = inventoryRepository.findByProductIdAndStoreId(product.getId(), store.getId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found"));

            // Check and update stock
            if (inventory.getStockLevel() < itemDto.getQuantity()) {
                throw new RuntimeException("Not enough stock for: " + product.getName());
            }
            inventory.setStockLevel(inventory.getStockLevel() - itemDto.getQuantity());
            inventoryRepository.save(inventory);

            // ==========================================
            // 5. Link the Item to the Order
            // ==========================================
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);       // <-- THIS LINKS THEM TOGETHER
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            
            double linePrice = product.getPrice() * itemDto.getQuantity();
            orderItem.setPrice(linePrice);
            
            calculatedTotalPrice += linePrice;

            // Save the child item
            orderItemRepository.save(orderItem);
        }

        // 6. Update the final price on the parent and save one last time
        order.setTotalPrice(calculatedTotalPrice);
        orderDetailsRepository.save(order);
    }
}