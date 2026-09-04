package com.luisalvarez.inventoryservice.controller;

import com.luisalvarez.inventoryservice.dto.InventoryRequestDto;
import com.luisalvarez.inventoryservice.dto.InventoryResponseDto;
import com.luisalvarez.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponseDto> getAllInventory(){
        return inventoryService.getAllInventory();
    }

    @GetMapping("/{sku}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable String sku, @RequestParam Integer quantity){
        return inventoryService.isInStock(sku, quantity);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponseDto createInventory(@Valid @RequestBody InventoryRequestDto requestDto){
        return inventoryService.createInventory(requestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponseDto updateInventory
            (@PathVariable Long id, @Valid @RequestBody InventoryRequestDto requestDto){
        return inventoryService.updateInventory(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInventory(@PathVariable Long id){
        inventoryService.deleteInventory(id);
    }

}
