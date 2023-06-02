package com.es.phoneshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class Product extends Entity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String description;
    /** null means there is no price because the product is outdated or new */
    private BigDecimal price;
    /** can be null if the price is null */
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<PriceHistory> historyList = new ArrayList<>();

    public Product() {
    }

    public Product(Long id, String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.historyList.add(new PriceHistory(new Date(System.currentTimeMillis()), price, currency));
    }

    public void setHistoryList(ArrayList<PriceHistory> historyList) {
        this.historyList.addAll(historyList);
    }
    public List<PriceHistory> getHistoryList() {
        return historyList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if(!obj.getClass().equals(Product.class)) {
            return false;
        }
        Product product = (Product) obj;
        if(this == product) {
            return true;
        }
        return this.id.equals(product.id) && this.historyList.equals(product.historyList)
                && this.stock == product.stock && this.code.equals(product.code) && this.currency.equals(product.currency)
                && this.description.equals(product.description) && this.imageUrl.equals(product.imageUrl)
                && this.price.equals(product.price);
    }
}