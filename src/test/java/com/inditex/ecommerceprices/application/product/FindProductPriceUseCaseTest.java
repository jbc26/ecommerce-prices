package com.inditex.ecommerceprices.application.product;

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
import com.inditex.ecommerceprices.domain.ports.inbound.FindProductPrice;
import com.inditex.ecommerceprices.domain.ports.outbound.FindProductPriceRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class FindProductPriceUseCaseTest {

    private final FindProductPriceRepository findProductPriceRepository = mock(
        FindProductPriceRepository.class);

    private final FindProductPrice useCase = new FindProductPriceUseCase(
        findProductPriceRepository);

    @Test
    void find_shouldReturnProductPrice_whenRepositoryFoundResult()
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

        when(findProductPriceRepository.find(PRODUCT_ID, BRAND_ID, APPLICATION_DATE))
            .thenReturn(Mono.just(productPrice));

        // when
        var mono = useCase.find(PRODUCT_ID, BRAND_ID, APPLICATION_DATE);

        //then
        StepVerifier.create(mono)
            .expectNext(productPrice)
            .verifyComplete();

        verify(findProductPriceRepository, times(1)).find(PRODUCT_ID, BRAND_ID, APPLICATION_DATE);
    }

    @Test
    void find_shouldReturnError_whenRepositoryDontFindProductPrice()
        throws ProductPriceNotFoundException {
        // given
        when(findProductPriceRepository.find(PRODUCT_ID, BRAND_ID, APPLICATION_DATE))
            .thenReturn(Mono.error(
                new ProductPriceNotFoundException(PRODUCT_ID, BRAND_ID, APPLICATION_DATE)));

        // when
        var mono = useCase.find(PRODUCT_ID, BRAND_ID, APPLICATION_DATE);

        //then
        StepVerifier.create(mono)
            .expectError(ProductPriceNotFoundException.class)
            .verify();

        verify(findProductPriceRepository, times(1)).find(PRODUCT_ID, BRAND_ID, APPLICATION_DATE);
    }
}