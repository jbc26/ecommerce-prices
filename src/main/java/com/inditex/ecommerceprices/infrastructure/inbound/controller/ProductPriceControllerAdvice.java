package com.inditex.ecommerceprices.infrastructure.inbound.controller;

import com.inditex.ecommerceprices.domain.entity.exception.ProductPriceNotFoundException;
import com.inditex.ecommerceprices.infrastructure.inbound.controller.dto.ProductPriceErrorResponseDto;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ProductPriceControllerAdvice {

    private static final Map<Class<? extends Throwable>, HttpStatus> exceptionMapping = Map.of(
        ProductPriceNotFoundException.class, HttpStatus.NOT_FOUND,
        Exception.class, HttpStatus.INTERNAL_SERVER_ERROR
    );

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProductPriceErrorResponseDto> mapExceptionToHttpStatus(Throwable e) {

        HttpStatus httpStatus = exceptionMapping.getOrDefault(e.getClass(),
            HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity(new ProductPriceErrorResponseDto(e.getMessage(), httpStatus),
            httpStatus);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProductPriceErrorResponseDto> mapInvalidDateParameter(
        MethodArgumentTypeMismatchException ex) {

        String message = String.format("Invalid format for parameter '%s'", ex.getName());

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity(
            new ProductPriceErrorResponseDto(message, httpStatus), httpStatus);
    }

}