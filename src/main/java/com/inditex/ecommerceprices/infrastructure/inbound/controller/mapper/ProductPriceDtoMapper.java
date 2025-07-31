package com.inditex.ecommerceprices.infrastructure.inbound.controller.mapper;

import com.inditex.ecommerceprices.domain.entity.ProductPrice;
import com.inditex.ecommerceprices.infrastructure.inbound.controller.dto.ProductPriceResponseDto;
import java.math.BigDecimal;
import lombok.NonNull;
import org.javamoney.moneta.Money;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductPriceDtoMapper {

    @Mapping(target = "price", source = "productPrice.price")
    @Mapping(target = "currency", source = "productPrice.price")
    ProductPriceResponseDto toResponseDto(ProductPrice productPrice);

    default BigDecimal toPriceAmount(@NonNull Money money) {
        return money.getNumber().numberValueExact(BigDecimal.class);
    }

    default String toCurrency(@NonNull Money money) {
        return money.getCurrency().getCurrencyCode();
    }
}


