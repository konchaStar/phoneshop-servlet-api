package com.es.phoneshop.service;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.security.impl.DefaultDosProtectionService;
import com.es.phoneshop.service.impl.DefaultOrderService;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class DefaultOrderServiceTest {
    private OrderService orderService;
    private CartService cartService;
    private OrderDao orderDao;
    @Before
    public void init() {
        cartService = HttpSessionCartService.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
        orderService = DefaultOrderService.getInstance();
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
        Order order = new Order();
        Cart cart = new Cart();
        Product product = new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), Currency.getInstance("USD"), 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        product.setPrice(new BigDecimal(100));

        orderService.getOrder(cart);
        Assert.assertEquals(order.getSubTotal(), BigDecimal.valueOf(200));
    }
}
