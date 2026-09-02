package com.luisalvarez.productservice.dataloader;

import com.luisalvarez.productservice.model.Product;
import com.luisalvarez.productservice.model.ProductCategory;
import com.luisalvarez.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {

        Product product = Product.builder()
                .name("California Roll")
                .category(ProductCategory.ROLLS)
                .description("California Roll with cheese")
                .price(BigDecimal.valueOf(15))
                .build();
        productRepository.save(product);

        System.out.println("Product added");
    }
}
