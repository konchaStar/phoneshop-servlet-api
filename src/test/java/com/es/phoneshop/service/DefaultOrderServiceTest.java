package com.es.phoneshop.service;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.impl.DefaultOrderService;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

public class DefaultOrderServiceTest {
    private OrderService orderService;
    private CartService cartService;
    private OrderDao orderDao;
    private ProductDao productDao;
    @Before
    public void init() {
        cartService = HttpSessionCartService.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
        orderService = DefaultOrderService.getInstance();
        productDao = ArrayListProductDao.getInstance();
        productDao.save(new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), Currency.getInstance("USD"), 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
    }
    @Test
    public void placeOrderTest() {
        Order order = new Order();
        orderService.placeOrder(order);
        Assert.assertEquals(order, orderDao.getOrder(order.getId()));
        Assert.assertEquals(order, orderDao.getOrderBySecureId(order.getSecureId()));
    }
    @Test
    public void getOrderTest() {
        Cart cart = new Cart();
        try {
            cartService.add(cart, 1L, 2);
        } catch (OutOfStockException e) {
            throw new RuntimeException(e);
        }
        Order order = orderService.getOrder(cart);
        Assert.assertEquals(order.getSubTotal(), BigDecimal.valueOf(200));
    }
}