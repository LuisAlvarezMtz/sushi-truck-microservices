package com.luisalvarez.productservice.controller;

import com.luisalvarez.productservice.dto.ProductRequestDto;
import com.luisalvarez.productservice.dto.ProductResponseDto;
import com.luisalvarez.productservice.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@RefreshScope
public class ProductController {

    private final ProductService productService;
    @Value("${app.maintenance.message: Operative System}")
    private String maintenanceMessage;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto createProduct(@Valid @RequestBody ProductRequestDto requestDto){
        return productService.createProduct(requestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getAllProducts(HttpServletResponse response){
        response.addHeader("X-Maintenance-Message", maintenanceMessage);
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getProductById(@PathVariable String id){
        return productService.getProductById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String id){
        productService.deleteProduct(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto updateProduct(@PathVariable String id,
                                     @Valid @RequestBody ProductRequestDto requestDto){
        return productService.updateProduct(id, requestDto);
    }
}
