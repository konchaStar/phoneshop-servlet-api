package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart extends Entity implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<CartItem> items;
    private int totalQuantity;
    private BigDecimal totalCost;
    public Cart() {
        items = new ArrayList<>();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totallyQuantity) {
        this.totalQuantity = totallyQuantity;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "Cart[" + items + "]";
    }
}
