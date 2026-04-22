package com.ecommerce.inventory_service.service.impl;

import com.ecommerce.inventory_service.dto.InventoryRequestDto;
import com.ecommerce.inventory_service.dto.InventoryResponseDto;
import com.ecommerce.inventory_service.exception.ResourceNotFoundException;
import com.ecommerce.inventory_service.mapper.InventoryMapper;
import com.ecommerce.inventory_service.model.Inventory;
import com.ecommerce.inventory_service.repository.InventoryRepository;
import com.ecommerce.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional(readOnly = true)
    public boolean isInStock(String sku, Integer quantity) {
        return inventoryRepository.findBySku(sku)
                .map(inventory -> inventory.getQuantity() >= quantity)
                .orElse(false);
    }

    @Override
    @Transactional
    public InventoryResponseDto createInventory(InventoryRequestDto inventoryRequestDto) {
        boolean exists = inventoryRepository.existsBySku(inventoryRequestDto.getSku());
        if (exists) {
            throw new RuntimeException("El inventario para el SKU " + inventoryRequestDto.getSku() + " ya existe");
        }

        Inventory inventory = inventoryMapper.toModel(inventoryRequestDto);

        Inventory savedInventory = inventoryRepository.save(inventory);

        log.info("Inventario creado para el SKU: {}", savedInventory.getSku());

        return inventoryMapper.toResponse(savedInventory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponseDto> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(inventoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public InventoryResponseDto updateInventory(Long id, InventoryRequestDto inventoryRequestDto) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario", "id", id));

        // Aquí podrías usar un mapper.updateFromDto(dto, entity) si quisieras
        // Pero setearlo manualmente también es válido y explícito:
        inventory.setSku(inventoryRequestDto.getSku());
        inventory.setQuantity(inventoryRequestDto.getQuantity());

        Inventory updatedInventory = inventoryRepository.save(inventory);

        log.info("Inventario actualizado para el ID: {}", id);

        return inventoryMapper.toResponse(updatedInventory);
    }

    @Override
    @Transactional
    public void deleteInventory(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inventario", "id", id);
        }
        inventoryRepository.deleteById(id);
        log.info("Inventario eliminado con ID: {}", id);
    }

    @Override
    @Transactional
    public void reduceStock(String sku, Integer quantity) {
        var inventory = inventoryRepository.findBySku(sku)
                .orElseThrow(
                        () -> new RuntimeException("Producto no encontrado: " + sku)
                );
        if(inventory.getQuantity() < quantity){
            throw new RuntimeException("Stock insuficiente para: " + sku);
        }

        inventory.setQuantity(inventory.getQuantity()-quantity);
        inventoryRepository.save(inventory);
    }
}
