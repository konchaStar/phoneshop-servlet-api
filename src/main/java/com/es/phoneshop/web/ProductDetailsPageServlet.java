package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {
    private static final String PRODUCT_ATTRIBUTE = "product";
    private static final String DISPATCHER_PATH = "/WEB-INF/pages/productData.jsp";
    private ArrayListProductDao productDao;

    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getPathInfo();
        request.setAttribute(PRODUCT_ATTRIBUTE, productDao.getProduct(Long.valueOf(id.substring(1))));
        request.getRequestDispatcher(DISPATCHER_PATH).forward(request, response);
    }
}
