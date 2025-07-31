package com.inditex.ecommerceprices.infrastructure.inbound.controller;

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

import com.inditex.ecommerceprices.application.product.FindProductPriceUseCase;
import com.inditex.ecommerceprices.domain.entity.ProductPrice;
import com.inditex.ecommerceprices.domain.entity.exception.ProductPriceNotFoundException;
import com.inditex.ecommerceprices.infrastructure.inbound.controller.dto.ProductPriceResponseDto;
import com.inditex.ecommerceprices.infrastructure.inbound.controller.mapper.ProductPriceDtoMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class FindProductPriceControllerTest {

    private final ProductPriceDtoMapper productPriceDtoMapper = Mappers.getMapper(
        ProductPriceDtoMapper.class);
    private final FindProductPriceUseCase useCase = mock(
        FindProductPriceUseCase.class);

    private final FindProductPriceController controller = new FindProductPriceController(
        productPriceDtoMapper, useCase);

    @Test
    void getPricesByFilter_shouldReturnProductPrice_whenUseCaseFindResult()
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

        var productPriceResponseDto = new ProductPriceResponseDto(
            PRODUCT_ID,
            BRAND_ID,
            PRICE.getNumber().numberValueExact(BigDecimal.class),
            PRICE.getCurrency().getCurrencyCode(),
            START_DATE,
            END_DATE
        );

        when(useCase.find(PRODUCT_ID, BRAND_ID, APPLICATION_DATE))
            .thenReturn(Mono.just(productPrice));

        // when
        var mono = controller.getPricesByFilter(PRODUCT_ID, BRAND_ID, APPLICATION_DATE);

        //then
        StepVerifier.create(mono)
            .expectNext(ResponseEntity.ok(productPriceResponseDto))
            .verifyComplete();

        verify(useCase, times(1)).find(PRODUCT_ID, BRAND_ID, APPLICATION_DATE);
    }

    @Test
    void find_shouldThrowNotFoundException_whenJpaRepositoryDontFindResult()
        throws ProductPriceNotFoundException {

        // given
        when(useCase.find(PRODUCT_ID, BRAND_ID, APPLICATION_DATE))
            .thenReturn(
                Mono.error(
                    new ProductPriceNotFoundException(PRODUCT_ID, BRAND_ID, APPLICATION_DATE)));

        // when
        var mono = controller.getPricesByFilter(PRODUCT_ID, BRAND_ID, APPLICATION_DATE);

        //then
        StepVerifier.create(mono)
            .expectError(ProductPriceNotFoundException.class)
            .verify();

        verify(useCase, times(1)).find(PRODUCT_ID, BRAND_ID, APPLICATION_DATE);
    }
}