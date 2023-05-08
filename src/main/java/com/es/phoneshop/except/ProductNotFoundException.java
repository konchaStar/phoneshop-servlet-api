package com.es.phoneshop.except;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {

    }
    public ProductNotFoundException(String message) {
        super(message);
    }
}
