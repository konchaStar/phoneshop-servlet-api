package com.es.phoneshop.dao;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.Entity;
import com.es.phoneshop.model.order.Order;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class EntityAbstractDao<T extends Entity> {
    protected Long maxId;
    protected List<T> entities;
    protected ReadWriteLock lock = new ReentrantReadWriteLock();
    public T getEntity(Long id) {
        if(id != null) {
            lock.readLock().lock();
            try {
                return entities.stream()
                        .filter(entity -> id.equals(entity.getId()))
                        .findAny()
                        .orElseThrow(() -> new OrderNotFoundException("Product with id " + id + " not found"));
            } finally {
                lock.readLock().unlock();
            }
        } else {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }
    public void save(T entity) {
        try {
            lock.writeLock().lock();
            if (entity.getId() == null) {
                entity.setId(maxId++);
                entities.add(entity);
            } else {
                Optional<T> element = entities.stream().filter(ent -> entity.getId().equals(ent.getId())).findAny();
                if (element.isPresent()) {
                    entities.set(entities.indexOf(element.get()), entity);
                } else {
                    entities.add(entity);
                    maxId++;
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
