package com.luisalvarez.productservice.service.impl;

import com.luisalvarez.productservice.dto.ProductRequestDto;
import com.luisalvarez.productservice.dto.ProductResponseDto;
import com.luisalvarez.productservice.exception.ResourceNotFoundException;
import com.luisalvarez.productservice.mapper.ProductMapper;
import com.luisalvarez.productservice.model.Product;
import com.luisalvarez.productservice.repository.ProductRepository;
import com.luisalvarez.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    @Override
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {

        Product product = mapper.toProduct(requestDto);
        Product savedProduct = productRepository.save(product);
        log.info("Product {} saved", savedProduct.getName());

        return mapper.toProductResponseDto(savedProduct);
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {

        return productRepository.findAll().stream()
                .map(mapper::toProductResponseDto)
                .toList();
    }

    @Override
    public ProductResponseDto getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product", "id", id));

        return mapper.toProductResponseDto(product);
    }

    @Override
    public ProductResponseDto updateProduct(String id, ProductRequestDto requestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product", "id", id));

        mapper.updateProductFromRequest(requestDto, product);

        Product updatedProduct =  productRepository.save(product);

        log.info("Product {} updated", updatedProduct.getName());

        return mapper.toProductResponseDto(updatedProduct);
    }

    @Override
    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(product);
        log.info("Product {} was deleted", product.getName());
    }
}
