package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.dao.sort.SortOrder;
import com.es.phoneshop.dao.sort.SortType;
import com.es.phoneshop.service.RecentProductsService;
import com.es.phoneshop.service.impl.HttpSessionRecentProductsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {
    private static final String PRODUCT_PARAM = "product";
    private static final String SORT_PARAM = "sort";
    private static final String ORDER_PARAM = "order";
    private static final String PRODUCTS_ATTRIBUTE = "products";
    private static final String DISPATCHER_PATH = "/WEB-INF/pages/productList.jsp";
    private ArrayListProductDao productDao;
    private RecentProductsService recentProductsService;
    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
        recentProductsService = HttpSessionRecentProductsService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter(PRODUCT_PARAM);
        String sortType = request.getParameter(SORT_PARAM);
        String sortOrder = request.getParameter(ORDER_PARAM);
        request.setAttribute("history", recentProductsService.getProductHistory(request).getRecentProducts());
        request.setAttribute("products", productDao.findProducts(search,
                sortType != null ? SortType.valueOf(sortType) : null,
                sortOrder != null ? SortOrder.valueOf(sortOrder) : null));
        request.getRequestDispatcher(DISPATCHER_PATH).forward(request, response);
    }

}
