package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.interf.ProductDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertTrue;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsResults() {
        assertTrue(!productDao.findProducts().isEmpty());
    }
    @Test
    public void testProductDaoSaveNewElement() {
        Product product = new Product(14L, "iphonese", "Iphone Se 2020", new BigDecimal(600),
                Currency.getInstance("USD"), 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product);
        Assert.assertEquals(product, productDao.getProduct(14L));
    }
    @Test
    public void testProductDaoReplaceElement() {
        Product product = new Product(13L, "iphonese", "Iphone Se 2020", new BigDecimal(600),
                Currency.getInstance("USD"), 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product);
        Product actual = productDao.getProduct(13L);
        Assert.assertEquals(product, productDao.getProduct(13L));
    }
    @Test
    public void testProductDaoDeleteElement() {
        productDao.delete(13L);
        int expectedSize = 11;
        int actualSize = productDao.findProducts().size();
        Assert.assertEquals(expectedSize, actualSize);
    }
}
