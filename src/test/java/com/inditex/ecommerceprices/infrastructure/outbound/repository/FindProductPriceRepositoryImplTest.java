package com.inditex.ecommerceprices.infrastructure.outbound.repository;

import static com.inditex.ecommerceprices.testutils.TestConstants.APPLICATION_DATE;
import static com.inditex.ecommerceprices.testutils.TestConstants.BRAND_ID;
import static com.inditex.ecommerceprices.testutils.TestConstants.END_DATE;
import static com.inditex.ecommerceprices.testutils.TestConstants.PRICE;
import static com.inditex.ecommerceprices.testutils.TestConstants.PRICE_LIST;
import static com.inditex.ecommerceprices.testutils.TestConstants.PRIORITY;
import static com.inditex.ecommerceprices.testutils.TestConstants.PRODUCT_ID;
import static com.inditex.ecommerceprices.testutils.TestConstants.START_DATE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.inditex.ecommerceprices.domain.entity.ProductPrice;
import com.inditex.ecommerceprices.domain.entity.exception.ProductPriceNotFoundException;
import com.inditex.ecommerceprices.domain.ports.outbound.FindProductPriceRepository;
import com.inditex.ecommerceprices.infrastructure.outbound.repository.dao.ProductPriceDao;
import com.inditex.ecommerceprices.infrastructure.outbound.repository.jpa.ProductPriceJpaRepository;
import com.inditex.ecommerceprices.infrastructure.outbound.repository.mapper.ProductPriceDaoMapper;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.test.StepVerifier;

class FindProductPriceRepositoryImplTest {

    private final ProductPriceJpaRepository jpaRepository = mock(
        ProductPriceJpaRepository.class);
    private final ProductPriceDaoMapper mapper = Mappers.getMapper(ProductPriceDaoMapper.class);

    private final FindProductPriceRepository repository = new FindProductPriceRepositoryImpl(
        jpaRepository, mapper);

    @Test
    void find_shouldReturnProductPrice_whenJpaRepositoryFindResult()
        throws ProductPriceNotFoundException {

        // given
        var productPriceDao = new ProductPriceDao(
            PRICE_LIST,
            PRODUCT_ID,
            BRAND_ID,
            PRICE.getNumber().numberValueExact(BigDecimal.class),
            PRICE.getCurrency().getCurrencyCode(),
            PRIORITY,
            START_DATE,
            END_DATE
        );

        var productPrice = new ProductPrice(
            PRICE_LIST,
            PRODUCT_ID,
            BRAND_ID,
            PRICE,
            PRIORITY,
            START_DATE,
            END_DATE
        );

        when(jpaRepository.filterPrice(PRODUCT_ID, BRAND_ID, APPLICATION_DATE))
            .thenReturn(Optional.of(productPriceDao));

        // when
        var mono = repository.find(PRODUCT_ID, BRAND_ID, APPLICATION_DATE);

        //then
        StepVerifier.create(mono)
            .expectNext(productPrice)
            .verifyComplete();

        verify(jpaRepository, times(1)).filterPrice(PRODUCT_ID, BRAND_ID, APPLICATION_DATE);
    }

    @Test
    void find_shouldThrowNotFoundException_whenJpaRepositoryDontFindResult()
        throws ProductPriceNotFoundException {

        // given
        var productPrice = new ProductPrice(
            PRICE_LIST,
            PRODUCT_ID,
            BRAND_ID,
            PRICE,
            PRIORITY,
            START_DATE,
            END_DATE
        );

        when(jpaRepository.filterPrice(PRODUCT_ID, BRAND_ID, APPLICATION_DATE))
            .thenReturn(Optional.empty());

        // when
        var mono = repository.find(PRODUCT_ID, BRAND_ID, APPLICATION_DATE);

        //then
        StepVerifier.create(mono)
            .expectError(ProductPriceNotFoundException.class)
            .verify();

        verify(jpaRepository, times(1)).filterPrice(PRODUCT_ID, BRAND_ID, APPLICATION_DATE);
    }

}