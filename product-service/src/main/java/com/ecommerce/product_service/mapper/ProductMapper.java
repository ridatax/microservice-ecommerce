package com.ecommerce.product_service.mapper;

import com.ecommerce.product_service.dto.ProductRequestDto;
import com.ecommerce.product_service.dto.ProductResponseDto;
import com.ecommerce.product_service.model.Product;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    Product toProduct(ProductRequestDto productRequestDto);

    ProductResponseDto toProductResponseDto(Product product);

    List<ProductResponseDto> toProductResponseDtos(List<Product> products);

    @Mapping(target = "id", ignore = true)
    void updateProduct(ProductRequestDto productRequestDto, @MappingTarget  Product product);
}
