package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.InventoryRequestDto;
import com.ecommerce.inventory_service.dto.InventoryResponseDto;
import java.util.List;

public interface InventoryService {

    boolean isInStock(String sku, Integer quantity);

    InventoryResponseDto createInventory(InventoryRequestDto inventoryRequestDto);

    List<InventoryResponseDto> getAllInventory();

    InventoryResponseDto updateInventory(Long id, InventoryRequestDto inventoryRequestDto);

    void deleteInventory(Long id);

    void reduceStock(String sku, Integer quantity);
}
