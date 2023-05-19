package com.es.phoneshop.exception;

import com.es.phoneshop.model.Product;

public class OutOfStockException extends Exception {
    private Product product;
    private int quantity;
    private int stock;

    public OutOfStockException(Product product, int quantity, int stock) {
        this.product = product;
        this.quantity = quantity;
        this.stock = stock;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getStock() {
        return stock;
    }
}
