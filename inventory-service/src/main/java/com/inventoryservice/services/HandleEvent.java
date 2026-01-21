package com.inventoryservice.services;

import com.inventoryservice.event.consumer.product.ProductCreatedEvent;
import com.inventoryservice.models.Inventory;
import com.inventoryservice.repositories.InventoryHistoryRepository;
import com.inventoryservice.repositories.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HandleEvent {

    private final InventoryRepository inventoryRepository;

    @Transactional
    @KafkaListener(topics = "productCreated", groupId = "inventoryGroup")
    public void createRecordInventory(ProductCreatedEvent productCreatedEvent){

        System.out.println(productCreatedEvent);
        Inventory inventory = new Inventory();
        inventory.initInventory(productCreatedEvent.productId(), productCreatedEvent.variantId());
        inventoryRepository.save(inventory);
    }

}
