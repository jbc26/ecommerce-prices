package com.inditex.ecommerceprices.application.product;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FindProductPriceUseCaseIT {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("validProductPrice")
    void find_shouldReturnProductPrice_whenRepositoryFoundResult(
        Integer productId,
        Integer brandId,
        LocalDateTime applicationDate,
        Money price
    ) throws Exception {

        // given

        // when
        var result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/prices")
                    .param("productId", productId.toString())
                    .param("brandId", brandId.toString())
                    .param("applicationDate", applicationDate.toString())
            )
            .andExpect(MockMvcResultMatchers.request().asyncStarted())
            .andReturn();

        // then
        mockMvc.perform(asyncDispatch(result))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productId", is(productId)))
            .andExpect(jsonPath("$.brandId", is(brandId)))
            .andExpect(jsonPath("$.price", is(price.getNumber().doubleValue())))
            .andExpect(jsonPath("$.currency", is(price.getCurrency().getCurrencyCode())))
            .andExpect(jsonPath("$.startDate").exists())
            .andExpect(jsonPath("$.endDate").exists());
    }

    @Test
    void find_shouldReturnNotFound_whenRepositoryNotFoundResult() throws Exception {

        // given product price not exist
        Integer productId = 10;
        Integer brandId = 10;
        var applicationDate = LocalDateTime.of(2025, 6, 14, 10, 0, 0);

        // when
        var result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/prices")
                    .param("productId", productId.toString())
                    .param("brandId", brandId.toString())
                    .param("applicationDate", applicationDate.toString())
            )
            .andExpect(MockMvcResultMatchers.request().asyncStarted())
            .andReturn();

        // then
        mockMvc.perform(asyncDispatch(result))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
            .andExpect(jsonPath("$.message", containsString("Product price not found with params")))
            .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.name())));
    }

    // Tests for the REST endpoint to validate the requests to the service using the provided sample data
    private static Stream<Arguments> validProductPrice() {
        return Stream.of(
            Arguments.of(35455, 1, LocalDateTime.of(2020, 6, 14, 10, 0, 0), Money.of(35.50, "EUR")),
            Arguments.of(35455, 1, LocalDateTime.of(2020, 6, 14, 16, 0, 0), Money.of(25.45, "EUR")),
            Arguments.of(35455, 1, LocalDateTime.of(2020, 6, 14, 21, 0, 0), Money.of(35.50, "EUR")),
            Arguments.of(35455, 1, LocalDateTime.of(2020, 6, 15, 10, 0, 0), Money.of(30.50, "EUR")),
            Arguments.of(35455, 1, LocalDateTime.of(2020, 6, 16, 21, 0, 0), Money.of(38.95, "EUR"))
        );
    }
}
