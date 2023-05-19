package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.Product;

public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if(!obj.getClass().equals(CartItem.class)) {
            return false;
        }
        CartItem item = (CartItem) obj;
        if(this == item) {
            return true;
        }
        return item.getProduct().equals(product);
    }

    @Override
    public String toString() {
        return product.getCode() + ", " + quantity;
    }
}
