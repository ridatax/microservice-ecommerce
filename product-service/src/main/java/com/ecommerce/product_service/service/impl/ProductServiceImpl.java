package com.ecommerce.product_service.service.impl;

import com.ecommerce.product_service.dto.ProductRequestDto;
import com.ecommerce.product_service.dto.ProductResponseDto;
import com.ecommerce.product_service.exception.ResourceNotFoundException;
import com.ecommerce.product_service.mapper.ProductMapper;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import com.ecommerce.product_service.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {

        Product product = productMapper.toProduct(productRequestDto);

        var savedProduct = productRepository.save(product);
        log.info("[createProduct] Product created with ID: [{}]", savedProduct.getId());

        return productMapper.toProductResponseDto(savedProduct);
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {

        var products = productRepository.findAll();
        return productMapper.toProductResponseDtos(products);
    }

    @Override
    public ProductResponseDto getProductById(String id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id));
        return productMapper.toProductResponseDto(product);
    }

    @Override
    public ProductResponseDto updateProduct(String id, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id));
         productMapper.updateProduct(productRequestDto, product);

         Product updatedProduct = productRepository.save(product);
         log.info("[updateProduct] Product updated with ID: [{}]", updatedProduct.getId());
         return productMapper.toProductResponseDto(updatedProduct);
    }

    @Override
    public void deleteProductById(String id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id);
        }

        productRepository.deleteById(id);
        log.info("[deleteProductById] Product deleted with ID: [{}]", id);
    }
}
