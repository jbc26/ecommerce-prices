package com.inditex.ecommerceprices.infrastructure.outbound.repository;

import com.inditex.ecommerceprices.domain.entity.ProductPrice;
import com.inditex.ecommerceprices.domain.entity.exception.ProductPriceNotFoundException;
import com.inditex.ecommerceprices.domain.ports.outbound.FindProductPriceRepository;
import com.inditex.ecommerceprices.infrastructure.outbound.repository.jpa.ProductPriceJpaRepository;
import com.inditex.ecommerceprices.infrastructure.outbound.repository.mapper.ProductPriceDaoMapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindProductPriceRepositoryImpl implements FindProductPriceRepository {

    private final ProductPriceJpaRepository productPriceJpaRepository;
    private final ProductPriceDaoMapper productPriceDaoMapper;

    @Override
    public Mono<ProductPrice> find(
        Integer productId,
        Integer brandId,
        LocalDateTime applicationDate
    ) {

        log.info(
            "Start finding product price from repository with params productId={}, brandId={} and applicationDate={}",
            productId, brandId, applicationDate
        );

        return Mono.fromCallable(() ->
                productPriceJpaRepository.filterPrice(productId, brandId, applicationDate))
            .flatMap(optional ->
                optional.map(productPriceDaoMapper::toEntity)
                    .map(Mono::just)
                    .orElse(Mono.error(
                        new ProductPriceNotFoundException(productId, brandId, applicationDate)))
            )
            .doOnSuccess(o -> log.info("Product price found successfully from repository"))
            .doOnError(e -> log.error("Error finding product price from repository", e));

    }
}
