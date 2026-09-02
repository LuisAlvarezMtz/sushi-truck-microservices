package com.luisalvarez.productservice.repository;

import com.luisalvarez.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
