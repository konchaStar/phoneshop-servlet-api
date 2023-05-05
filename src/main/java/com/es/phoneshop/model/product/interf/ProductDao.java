package com.es.phoneshop.model.product.interf;

import com.es.phoneshop.model.product.Product;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id);
    List<Product> findProducts();
    void save(Product product);
    void delete(Long id);
}
