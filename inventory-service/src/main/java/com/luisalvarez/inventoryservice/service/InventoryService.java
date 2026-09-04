package com.luisalvarez.inventoryservice.service;

import com.luisalvarez.inventoryservice.dto.InventoryRequestDto;
import com.luisalvarez.inventoryservice.dto.InventoryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InventoryService {

    List<InventoryResponseDto> getAllInventory();
    InventoryResponseDto createInventory(InventoryRequestDto requestDto);
    InventoryResponseDto updateInventory(Long id, InventoryRequestDto requestDto);
    boolean isInStock(String sku, Integer quantity);
    void deleteInventory(Long id);

}
