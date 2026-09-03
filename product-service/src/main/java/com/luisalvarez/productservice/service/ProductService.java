package com.luisalvarez.productservice.service;

import com.luisalvarez.productservice.dto.ProductRequestDto;
import com.luisalvarez.productservice.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto requestDto);
    List<ProductResponseDto> getAllProducts();
    ProductResponseDto getProductById(String id);
    ProductResponseDto updateProduct(String id, ProductRequestDto requestDto);
    void deleteProduct(String id);
}
