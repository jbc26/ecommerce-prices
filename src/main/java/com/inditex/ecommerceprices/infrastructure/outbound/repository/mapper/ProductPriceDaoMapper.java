package com.inditex.ecommerceprices.infrastructure.outbound.repository.mapper;

import com.inditex.ecommerceprices.domain.entity.ProductPrice;
import com.inditex.ecommerceprices.infrastructure.outbound.repository.dao.ProductPriceDao;
import org.javamoney.moneta.Money;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductPriceDaoMapper {

    @Mapping(target = "price", source = ".", qualifiedByName = "toMoney")
    ProductPrice toEntity(ProductPriceDao productPriceDao);

    @Named("toMoney")
    default Money toMoney(ProductPriceDao dao) {
        if (dao.getPrice() == null || dao.getCurrency() == null) {
            return null;
        }
        return Money.of(dao.getPrice(), dao.getCurrency());
    }
}
