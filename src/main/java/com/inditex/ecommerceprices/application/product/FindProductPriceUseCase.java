package com.inditex.ecommerceprices.application.product;

import com.inditex.ecommerceprices.domain.entity.ProductPrice;
import com.inditex.ecommerceprices.domain.entity.exception.ProductPriceNotFoundException;
import com.inditex.ecommerceprices.domain.ports.inbound.FindProductPrice;
import com.inditex.ecommerceprices.domain.ports.outbound.FindProductPriceRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindProductPriceUseCase implements FindProductPrice {

    private final FindProductPriceRepository findProductPriceRepository;

    @Override
    public Mono<ProductPrice> find(
        Integer productId,
        Integer brandId,
        LocalDateTime applicationDate
    ) throws ProductPriceNotFoundException {

        log.info(
            "Start finding product price with params productId={}, brandId={} and applicationDate={}",
            productId, brandId, applicationDate
        );

        return findProductPriceRepository.find(productId, brandId, applicationDate)
            .doOnSuccess(productPrice -> log.info("Product price found successfully"))
            .doOnError(e -> log.error("Error finding product price", e));
    }
}
