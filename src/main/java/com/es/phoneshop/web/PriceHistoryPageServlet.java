package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class PriceHistoryPageServlet extends HttpServlet {
    private static final String PRICES_ATTRIBUTE = "prices";
    private static final String DISPATCHER_PATH = "/WEB-INF/pages/priceHistoryPage.jsp";
    private ProductDao productDao;
    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getPathInfo().substring(1);
        request.setAttribute(PRICES_ATTRIBUTE, productDao.getProduct(Long.valueOf(id)).getHistoryList());
        request.getRequestDispatcher(DISPATCHER_PATH).forward(request, response);
    }
}
