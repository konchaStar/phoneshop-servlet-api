package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.model.RecentProductsHistory;
import com.es.phoneshop.service.RecentProductsService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HttpSessionRecentProductsService implements RecentProductsService {
    private static final int MAX_SIZE = 3;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final String RECENT_PRODUCTS_SESSION_ATTRIBUTE = HttpSessionCartService.class.getName() + ".recentProducts";
    private ArrayListProductDao productDao;
    private static class SingletonHolder {
        private static final HttpSessionRecentProductsService INSTANCE = new HttpSessionRecentProductsService();
    }

    private HttpSessionRecentProductsService() {
        productDao = ArrayListProductDao.getInstance();
    }
    public static HttpSessionRecentProductsService getInstance() {
        return SingletonHolder.INSTANCE;
    }
    @Override
    public RecentProductsHistory getProductHistory(HttpServletRequest request) {
        RecentProductsHistory history = (RecentProductsHistory) request.getSession()
                .getAttribute(RECENT_PRODUCTS_SESSION_ATTRIBUTE);
        if (history == null) {
            request.getSession().setAttribute(RECENT_PRODUCTS_SESSION_ATTRIBUTE,
                    history = new RecentProductsHistory());
        }
        return history;
    }

    @Override
    public void add(RecentProductsHistory history, Long id) {
        lock.writeLock().lock();
        try {
            Product product = productDao.getProduct(id);
            history.getRecentProducts().removeIf(p -> id.equals(p.getId()));
            history.getRecentProducts().add(0, product);
            if (history.getRecentProducts().size() > MAX_SIZE) {
                history.getRecentProducts().remove(MAX_SIZE);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
