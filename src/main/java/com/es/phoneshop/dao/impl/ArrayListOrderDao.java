package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.EntityAbstractDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;

public class ArrayListOrderDao extends EntityAbstractDao<Order> implements OrderDao {

    private static class SingletonHelper {
        private static final ArrayListOrderDao INSTANCE = new ArrayListOrderDao();
    }
    private ArrayListOrderDao() {
        entities = new ArrayList<>();
        maxId = Long.valueOf(1);
    }
    public static ArrayListOrderDao getInstance() {
        return SingletonHelper.INSTANCE;
    }


    @Override
    public Order getOrder(Long id) {
        return super.getEntity(id);
    }

    @Override
    public Order getOrderBySecureId(String secureId) {
        if(secureId != null) {
            lock.readLock().lock();
            try {
                return entities.stream()
                        .filter(order -> secureId.equals(order.getSecureId()))
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
