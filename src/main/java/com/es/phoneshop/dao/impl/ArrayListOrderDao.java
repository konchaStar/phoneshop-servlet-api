package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao implements OrderDao {
    private Long maxId;
    private List<Order> orders;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private static class SingletonHelper {
        private static final ArrayListOrderDao INSTANCE = new ArrayListOrderDao();
    }
    private ArrayListOrderDao() {
        orders = new ArrayList<>();
        maxId = Long.valueOf(1);
    }
    public static ArrayListOrderDao getInstance() {
        return SingletonHelper.INSTANCE;
    }
    @Override
    public Order getOrder(Long id) {
        if(id != null) {
            lock.readLock().lock();
            try {
                return orders.stream()
                        .filter(product -> id.equals(product.getId()))
                        .findAny()
                        .orElseThrow(() -> new OrderNotFoundException("Product with id " + id + " not found"));
            } finally {
                lock.readLock().unlock();
            }
        } else {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }

    @Override
    public void save(Order order) {
        try {
            lock.writeLock().lock();
            if (order.getId() == null) {
                order.setId(maxId++);
                orders.add(order);
            } else {
                Optional<Order> element = orders.stream().filter(prod -> order.getId().equals(prod.getId())).findAny();
                if (element.isPresent()) {
                    orders.set(orders.indexOf(element.get()), order);
                } else {
                    orders.add(order);
                    maxId++;
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Order getOrderBySecureId(String secureId) {
        if(secureId != null) {
            lock.readLock().lock();
            try {
                return orders.stream()
                        .filter(product -> secureId.equals(product.getSecureId()))
                        .findAny()
                        .orElseThrow(() -> new OrderNotFoundException("Product with id " + secureId + " not found"));
            } finally {
                lock.readLock().unlock();
            }
        } else {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }
}
