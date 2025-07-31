package com.inditex.ecommerceprices.infrastructure.inbound.controller;

import static com.inditex.ecommerceprices.testutils.TestConstants.APPLICATION_DATE;
import static com.inditex.ecommerceprices.testutils.TestConstants.BRAND_ID;
import static com.inditex.ecommerceprices.testutils.TestConstants.PRODUCT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.inditex.ecommerceprices.domain.entity.exception.ProductPriceNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

class ProductPriceControllerAdviceTest {

    private final ProductPriceControllerAdvice advice = new ProductPriceControllerAdvice();

    @ParameterizedTest
    @MethodSource("exceptionProvider")
    void mapExceptionToHttpStatus_shouldReturnNotFoundStatus_whenExceptionReceivedProductPriceNotFoundException(
        Throwable exception,
        HttpStatus expectedStatus,
        String expectedMessage
    ) {
        // when
        var result = advice.mapExceptionToHttpStatus(exception);

        //then
        assertEquals(expectedMessage, result.getBody().message());
        assertEquals(expectedStatus, result.getBody().status());
        assertEquals(expectedStatus, result.getStatusCode());
    }

    @Test
    void mapInvalidDateParameter_shouldReturnBadRequestStatus_whenExceptionReceivedMethodArgumentTypeMismatchException() {
        // given
        var parameterName = "applicationDate";
        var exception = new MethodArgumentTypeMismatchException(
            parameterName,
            LocalDateTime.class,
            parameterName,
            null,
            new Exception());

        // when
        var result = advice.mapInvalidDateParameter(exception);

        // then
        assertEquals(String.format("Invalid format for parameter '%s'", parameterName),
            result.getBody().message());
        assertEquals(HttpStatus.BAD_REQUEST, result.getBody().status());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    private static Stream<Arguments> exceptionProvider() {
        return Stream.of(
            Arguments.of(
                new ProductPriceNotFoundException(PRODUCT_ID, BRAND_ID, APPLICATION_DATE),
                HttpStatus.NOT_FOUND,
                String.format(
                    "Product price not found with params productId=%d, brandId=%d and applicationDate=%s",
                    PRODUCT_ID, BRAND_ID, APPLICATION_DATE.format(DateTimeFormatter.ISO_DATE_TIME))
            ),
            Arguments.of(
                new RuntimeException("Runtime error"),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Runtime error"
            ),
            Arguments.of(
                new Exception("Generic exception"),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Generic exception"
            )
        );
    }
}