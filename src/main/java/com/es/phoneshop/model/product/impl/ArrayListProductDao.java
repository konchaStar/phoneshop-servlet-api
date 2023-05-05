package com.es.phoneshop.model.product.impl;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.except.ProductNotFoundException;
import com.es.phoneshop.model.product.interf.ProductDao;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListProductDao implements ProductDao {
    private List<Product> products;
    private ReadWriteLock lock;
    private Long maxId;
    public ArrayListProductDao() {
        products = new ArrayList<>();
        saveSampleProducts();
        lock = new ReentrantReadWriteLock();
    }
    @Override
    public Product getProduct(Long id) throws IllegalArgumentException, ProductNotFoundException {
        if(id != null) {
            lock.readLock().lock();
            try {
                return products.stream()
                        .filter(product -> id.equals(product.getId()))
                        .findAny()
                        .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
            } finally {
                lock.readLock().unlock();
            }
        } else {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }

    @Override
    public List<Product> findProducts() {
        try {
            lock.readLock().lock();
            return products.stream()
                    .filter(product -> product.getId() != null)
                    .filter(product -> product.getStock() > 0)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void save(Product product) {
        try {
            lock.writeLock().lock();
            if (product.getId() == null) {
                product.setId(maxId++);
                products.add(product);
            } else {
                Optional<Product> element = products.stream().filter(prod -> product.getId().equals(prod.getId())).findAny();
                if (element.isPresent()) {
                    products.set(products.indexOf(element.get()), product);
                } else {
                    products.add(product);
                    maxId++;
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        try {
            lock.writeLock().lock();
            if (id != null) {
                products.removeIf(product -> id.equals(product.getId()));
            } else {
                throw new IllegalArgumentException("Id cannot be null");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    private void saveSampleProducts() {
        Currency usd = Currency.getInstance("USD");
        save(new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        save(new Product(2L, "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        save(new Product(3L, "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        save(new Product(4L, "iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        save(new Product(5L, "iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        save(new Product(6L, "htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        save(new Product(7L, "sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        save(new Product(8L, "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        save(new Product(9L, "nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        save(new Product(10L, "palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        save(new Product(11L, "simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        save(new Product(12L, "simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        save(new Product(13L, "simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
    }
}
