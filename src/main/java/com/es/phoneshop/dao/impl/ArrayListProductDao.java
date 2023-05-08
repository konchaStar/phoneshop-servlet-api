package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.sort.SortOrder;
import com.es.phoneshop.dao.sort.SortType;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.except.ProductNotFoundException;
import com.es.phoneshop.dao.ProductDao;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListProductDao implements ProductDao {
    private static ArrayListProductDao instance;
    public static ArrayListProductDao getInstance() {
        if(instance == null) {
            return instance = new ArrayListProductDao();
        }
        return instance;
    }
    private List<Product> products;
    private ReadWriteLock lock;
    private Long maxId;
    private ArrayListProductDao() {
        products = new ArrayList<>();
        lock = new ReentrantReadWriteLock();
        maxId = Long.valueOf(1);
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
    public List<Product> findProducts(String search, SortType type, SortOrder order) {
        Comparator<Product> sortComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Product product1 = (Product) o1;
                Product product2 = (Product) o2;
                if(type != null && order != null) {
                    if(SortType.price == type) {
                        if(product1.getPrice().compareTo(product2.getPrice()) > 0) {
                            return order == SortOrder.asc ? 1 : -1;
                        } else if(product1.getPrice().compareTo(product2.getPrice()) < 0) {
                            return order == SortOrder.asc ? -1 : 1;
                        }
                        return 0;
                    } else {
                        if(product1.getDescription().compareTo(product2.getDescription()) > 0) {
                            return order == SortOrder.asc ? 1 : -1;
                        } else if(product1.getDescription().compareTo(product2.getDescription()) < 0) {
                            return order == SortOrder.asc ? -1 : 1;
                        }
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        };
        Comparator<Product> searchComparator = new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                if(!(search == null || search.equals(""))) {
                    Product product1 = (Product) o1;
                    Product product2 = (Product) o2;
                    int count1 = 0;
                    int count2 = 0;
                    for (String desc : search.toLowerCase().split("\\s")) {
                        if (product1.getDescription().toLowerCase().contains(desc)) {
                            count1++;
                        }
                        if (product2.getDescription().toLowerCase().contains(desc)) {
                            count2++;
                        }
                    }
                    return count2 - count1;
                }
                return 0;
            }
        };
        try {
            lock.readLock().lock();
            return products.stream()
                    .filter(product -> {
                        if(!(search == null || search.equals(""))) {
                            for(String desc : search.toLowerCase().split("\\s")) {
                                if(product.getDescription().toLowerCase().contains(desc)) {
                                    return true;
                                }
                            }
                            return false;
                        }
                        return true;
                    })
                    .sorted(searchComparator)
                    .filter(product -> product.getId() != null)
                    .filter(product -> product.getStock() > 0)
                    .sorted(sortComparator)
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
}
