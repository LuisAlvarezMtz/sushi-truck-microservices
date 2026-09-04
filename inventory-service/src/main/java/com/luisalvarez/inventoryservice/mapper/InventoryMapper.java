package com.luisalvarez.inventoryservice.mapper;

import com.luisalvarez.inventoryservice.dto.InventoryRequestDto;
import com.luisalvarez.inventoryservice.dto.InventoryResponseDto;
import com.luisalvarez.inventoryservice.model.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    Inventory toInventory(InventoryRequestDto requestDto);

    @Mapping(target = "inStock", expression = "java(inventory.getQuantityAvailable() > 0)")
    InventoryResponseDto toInventoryResponse(Inventory inventory);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sku", ignore = true)
    void updateInventoryFromRequest(InventoryRequestDto requestDto, @MappingTarget Inventory inventory);

}
