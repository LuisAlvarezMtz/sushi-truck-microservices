package com.luisalvarez.productservice.mapper;

import com.luisalvarez.productservice.dto.ProductRequestDto;
import com.luisalvarez.productservice.dto.ProductResponseDto;
import com.luisalvarez.productservice.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    Product toProduct(ProductRequestDto requestDto);

    ProductResponseDto toProductResponseDto(Product product);

    @Mapping(target = "id", ignore = true)
    void updateProductFromRequest(ProductRequestDto requestDto, @MappingTarget Product product);
}
