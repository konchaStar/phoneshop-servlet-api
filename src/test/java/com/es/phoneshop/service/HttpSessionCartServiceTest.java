package com.es.phoneshop.service;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.PriceHistory;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class HttpSessionCartServiceTest {
    private CartService cartService;
    private ProductDao products;
    @Before
    public void setup() {
        cartService = HttpSessionCartService.getInstance();
        products = ArrayListProductDao.getInstance();
        saveDemoData();
    }
    @Test
    public void testAddNewItemToCart() throws OutOfStockException {
        Cart cart = new Cart();
        Long id = 12L;
        int quantity = 1;
        cartService.add(cart, id, quantity);
        Assert.assertEquals(1, cart.getItems().size());
    }
    @Test
    public void testAddItemToCart() throws OutOfStockException {
        Cart cart = new Cart();
        Long id = 1l;
        int quantity = 1;
        cartService.add(cart, id, quantity);
        cartService.add(cart, id, quantity);
        Assert.assertEquals(1, cart.getItems().size());
        Assert.assertEquals(cart.getItems().get(0).getQuantity(), 2);
    }
    @Test
    public void testUpdateCart() throws OutOfStockException {
        Cart cart = new Cart();
        Long id = 12L;
        int quantity = 1;
        cartService.add(cart, id, quantity);
        quantity = 10;
        cartService.update(cart, id, quantity);
        Assert.assertEquals(1, cart.getItems().size());
        Assert.assertEquals(10, cart.getItems().get(0).getQuantity());
    }
    @Test
    public void testDeleteItemFromCart() throws OutOfStockException {
        Cart cart = new Cart();
        Long id = 12L;
        int quantity = 1;
        cartService.add(cart, id, quantity);
        cartService.delete(cart, id);
        Assert.assertEquals(0, cart.getItems().size());
    }
    private void saveDemoData() {
        Currency usd = Currency.getInstance("USD");
        products.save(new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        products.save(new Product(2L, "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        products.save(new Product(3L, "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        products.save(new Product(4L, "iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        products.save(new Product(5L, "iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        products.save(new Product(6L, "htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        products.save(new Product(7L, "sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        products.save(new Product(8L, "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        products.save(new Product(9L, "nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        products.save(new Product(10L, "palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        products.save(new Product(11L, "simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        products.save(new Product(12L, "simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        products.save(new Product(13L, "simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
        products.getProduct(3L).setHistoryList(new ArrayList<>(List.of(new PriceHistory(new Date("2022/06/13"), BigDecimal.valueOf(150L), usd))));
    }
}
