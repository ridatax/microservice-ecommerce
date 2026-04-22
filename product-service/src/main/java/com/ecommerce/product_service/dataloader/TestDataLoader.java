package com.ecommerce.product_service.dataloader;

import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {

        Product product1 = Product.builder().name("Samsung Galaxy S24").description("Smartphone con IA").price(BigDecimal.valueOf(680.00)).build();
        Product product2 = Product.builder().name("Iphone 17").description("Smartphone Apple").price(BigDecimal.valueOf(19.99)).build();
        productRepository.save(product1);
        productRepository.save(product2);

    }
}
