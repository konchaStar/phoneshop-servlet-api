package com.es.phoneshop.model;

import java.util.ArrayList;
import java.util.List;

public class RecentProductsHistory {
    private List<Product> recentProducts;
    public RecentProductsHistory() {
        recentProducts = new ArrayList<>();
    }

    public List<Product> getRecentProducts() {
        return recentProducts;
    }
}
