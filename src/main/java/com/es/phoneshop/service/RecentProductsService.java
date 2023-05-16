package com.es.phoneshop.service;

import com.es.phoneshop.model.RecentProductsHistory;
import jakarta.servlet.http.HttpServletRequest;

public interface RecentProductsService {
    RecentProductsHistory getProductHistory(HttpServletRequest request);
    void add(RecentProductsHistory history, Long id);
}
