package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.service.CartService;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class HttpSessionCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = HttpSessionCartService.class.getName() + ".cart";
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private ArrayListProductDao productDao;

    private static class SingletonHolder {
        private static final HttpSessionCartService INSTANCE = new HttpSessionCartService();
    }
    private HttpSessionCartService() {
        productDao = ArrayListProductDao.getInstance();
    }
    public static HttpSessionCartService getInstance() {
        return SingletonHolder.INSTANCE;
    }
    @Override
    public Cart getCart(HttpServletRequest request) {
        lock.writeLock().lock();
        try {
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void add(Cart cart, Long id, int quantity) throws OutOfStockException {
        lock.writeLock().lock();
        try {
            Product product = productDao.getProduct(id);
            CartItem item = new CartItem(product, quantity);
            int index;
            if ((index = cart.getItems().indexOf(item)) != -1) {
                CartItem existed = cart.getItems().get(index);
                if(product.getStock() < quantity + existed.getQuantity()) {
                    throw new OutOfStockException(product, quantity, product.getStock());
                }
                existed.setQuantity(existed.getQuantity() + quantity);
            } else {
                if(product.getStock() < quantity) {
                    throw new OutOfStockException(product, quantity, product.getStock());
                }
                cart.getItems().add(item);
            }
            recalculate(cart);
        } finally {
            lock.writeLock().unlock();
        }
    }
    @Override
    public void update(Cart cart, Long id, int quantity) throws OutOfStockException {
        lock.writeLock().lock();
        try {
            Product product = productDao.getProduct(id);
            CartItem item = new CartItem(product, quantity);
            int index = cart.getItems().indexOf(item);
            CartItem existed = cart.getItems().get(index);
            if(product.getStock() < quantity) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            existed.setQuantity(quantity);
            recalculate(cart);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Cart cart, Long id) {
        lock.writeLock().lock();
        try {
            cart.getItems().removeIf(item -> item.getProduct().getId().equals(id));
            recalculate(cart);
        } finally {
            lock.writeLock().unlock();
        }
    }
    private void recalculate(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity)
                .collect(Collectors.summingInt(q -> q.intValue())));
        cart.setTotalCost(cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    @Override
    public void clearCart(HttpServletRequest request) {
        lock.writeLock().lock();
        try {
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, null);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
