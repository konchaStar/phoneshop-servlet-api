package com.es.phoneshop.service;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.model.cart.Cart;
import jakarta.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);
    void add(Cart cart, Long id, int quantity) throws OutOfStockException;
    void update(Cart cart, Long id, int quantity) throws OutOfStockException;
    void delete(Cart cart, Long id);
    void clearCart(Cart cart);
}
