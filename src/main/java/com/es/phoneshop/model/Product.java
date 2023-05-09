package com.es.phoneshop.model;

import java.math.BigDecimal;
import java.util.*;

public class Product {
    private Long id;
    private String code;
    private String description;
    /** null means there is no price because the product is outdated or new */
    private BigDecimal price;
    /** can be null if the price is null */
    private Currency currency;
    private int stock;
    private String imageUrl;
    private ArrayList<PriceHistory> historyList;

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
        this.historyList = new ArrayList<>();
        this.historyList.add(new PriceHistory(new Date(System.currentTimeMillis()), price, currency));
    }

    public void setHistoryList(ArrayList<PriceHistory> historyList) {
        this.historyList = historyList;
    }
    public List<PriceHistory> getHistoryList() {
        return historyList;
    }

    public void addHistoryPrice(PriceHistory history) {
        historyList.add(history);
        historyList.sort(new Comparator<PriceHistory>() {
            @Override
            public int compare(PriceHistory o1, PriceHistory o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}