package com.es.phoneshop.dao;

import com.es.phoneshop.dao.search.SearchParameter;
import com.es.phoneshop.dao.sort.SortOrder;
import com.es.phoneshop.dao.sort.SortType;
import com.es.phoneshop.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    Product getProduct(Long id);
    List<Product> findProducts(String search, SortType type, SortOrder order);
    List<Product> findAdvancedProducts(String search, SearchParameter parameter, BigDecimal minPrice, BigDecimal maxPrice);
    void save(Product product);
    void delete(Long id);
}
