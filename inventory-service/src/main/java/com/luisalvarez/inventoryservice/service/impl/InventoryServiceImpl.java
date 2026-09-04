package com.luisalvarez.inventoryservice.service.impl;

import com.luisalvarez.inventoryservice.dto.InventoryRequestDto;
import com.luisalvarez.inventoryservice.dto.InventoryResponseDto;
import com.luisalvarez.inventoryservice.exception.ResourceAlreadyExistsException;
import com.luisalvarez.inventoryservice.exception.ResourceNotFoundException;
import com.luisalvarez.inventoryservice.mapper.InventoryMapper;
import com.luisalvarez.inventoryservice.model.Inventory;
import com.luisalvarez.inventoryservice.repository.InventoryRepository;
import com.luisalvarez.inventoryservice.service.InventoryService;
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
    private final InventoryMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponseDto> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(mapper::toInventoryResponse)
                .toList();
    }

    @Override
    @Transactional
    public InventoryResponseDto createInventory(InventoryRequestDto requestDto) {

        if(inventoryRepository.existsBySku(requestDto.sku())) {
            throw new ResourceAlreadyExistsException("Inventory", "sku", requestDto.sku());
        }

        Inventory inventory = mapper.toInventory(requestDto);
        Inventory savedInventory = inventoryRepository.save(inventory);

        log.info("Inventory created with SKU: {}", savedInventory.getSku());
        return mapper.toInventoryResponse(savedInventory);
    }

    @Override
    @Transactional
    public InventoryResponseDto updateInventory(Long id, InventoryRequestDto requestDto) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", id));

        mapper.updateInventoryFromRequest(requestDto, inventory);

        Inventory inventoryUpdated = inventoryRepository.save(inventory);

        log.info("Inventory updated with ID: {}", inventoryUpdated.getId());
        return mapper.toInventoryResponse(inventoryUpdated);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isInStock(String sku, Integer quantity) {
        Inventory inventory = inventoryRepository.findBySku(sku).orElseThrow(()->
                new ResourceNotFoundException("Inventory", "sku", sku));

        return inventory.getQuantityAvailable() >= quantity;
    }

    @Override
    @Transactional
    public void deleteInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", id));

        inventoryRepository.delete(inventory);
        log.info("Inventory deleted with ID: {}", inventory.getId());
    }
}
