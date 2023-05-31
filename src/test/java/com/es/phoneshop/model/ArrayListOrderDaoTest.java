package com.es.phoneshop.model;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArrayListOrderDaoTest {
    private OrderDao orderDao;

    @Before
    public void setup() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Test
    public void saveOrderTest() {
        Order order = new Order();
        orderDao.save(order);
        Assert.assertEquals(order, orderDao.getOrder(1L));
    }
    @Test
    public void orderNotFoundTest() {
        Order order = new Order();
        orderDao.save(order);
        boolean exception = false;
        try {
            orderDao.getOrder(2L);
        } catch (OrderNotFoundException e) {
            exception = true;
        }
        Assert.assertTrue(exception);
    }
    @Test
    public void getOrderBySecureIdTest() {
        Order order = new Order();
        order.setSecureId("aaasdd");
        orderDao.save(order);
        Assert.assertEquals(order, orderDao.getOrderBySecureId(order.getSecureId()));
    }
}
