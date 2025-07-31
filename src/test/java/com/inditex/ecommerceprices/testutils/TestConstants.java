package com.inditex.ecommerceprices.testutils;

import java.time.LocalDateTime;
import org.javamoney.moneta.Money;

public class TestConstants {

    public static final Integer PRODUCT_ID = 1234;
    public static final Integer BRAND_ID = 1;
    public static final Integer PRICE_LIST = 123;
    public static final Integer PRIORITY = 1;
    public static final Money PRICE = Money.of(345.12, "EUR");
    public static final LocalDateTime APPLICATION_DATE = LocalDateTime.of(2020, 8, 15, 0, 0, 0);
    public static final LocalDateTime START_DATE = LocalDateTime.of(2020, 6, 15, 0, 0, 0);
    public static final LocalDateTime END_DATE = LocalDateTime.of(2020, 10, 15, 0, 0, 0);

}
