package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.sort.SortOrder;
import com.es.phoneshop.dao.sort.SortType;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private List<Product> products;
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private Long maxId;
    private static class SingletonHolder {
        private static final ArrayListProductDao INSTANCE = new ArrayListProductDao();
    }
    private ArrayListProductDao() {
        products = new ArrayList<>();
        maxId = Long.valueOf(1);
    }
    public static ArrayListProductDao getInstance() {
        return SingletonHolder.INSTANCE;
    }
    @Override
    public Product getProduct(Long id) {
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
        Comparator<Product> sortComparator = getSortComparator(type, order);
        Comparator<Product> searchComparator = getSearchComparator(search);
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
    private Comparator<Product> getSearchComparator(String search) {
        return new Comparator<Product>() {
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
    }
    private Comparator<Product> getSortComparator(SortType type, SortOrder order) {
        return new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Product product1 = (Product) o1;
                Product product2 = (Product) o2;
                if(type != null && order != null) {
                    if(SortType.PRICE == type) {
                        if(product1.getPrice().compareTo(product2.getPrice()) > 0) {
                            return order == SortOrder.ASC ? 1 : -1;
                        } else if(product1.getPrice().compareTo(product2.getPrice()) < 0) {
                            return order == SortOrder.ASC ? -1 : 1;
                        }
                        return 0;
                    } else {
                        if(product1.getDescription().compareTo(product2.getDescription()) > 0) {
                            return order == SortOrder.ASC ? 1 : -1;
                        } else if(product1.getDescription().compareTo(product2.getDescription()) < 0) {
                            return order == SortOrder.ASC ? -1 : 1;
                        }
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        };
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
