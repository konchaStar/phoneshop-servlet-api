package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.RecentProductsHistory;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import com.es.phoneshop.service.impl.HttpSessionRecentProductsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private static final String PRODUCT_ATTRIBUTE = "product";
    private static final String QUANTITY_PARAM = "quantity";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String NEGATIVE_NUMBER_ERROR = "Quantity must be more than 0";
    private static final String NOT_A_NUMBER_ERROR = "Not a number";
    private static final String DISPATCHER_PATH = "/WEB-INF/pages/productData.jsp";

    private ArrayListProductDao productDao;
    private CartService cartService;
    private HttpSessionRecentProductsService recentProductsService;

    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
        cartService = HttpSessionCartService.getInstance();
        recentProductsService = HttpSessionRecentProductsService.getInstance();
    }
    private void addRecentProduct(HttpServletRequest request, Long id) {
        RecentProductsHistory history = recentProductsService.getProductHistory(request);
        recentProductsService.add(history, id);
        request.setAttribute("history", history.getRecentProducts());
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getPathInfo();
        addRecentProduct(request, Long.valueOf(id.substring(1)));
        request.setAttribute(PRODUCT_ATTRIBUTE, productDao.getProduct(Long.valueOf(id.substring(1))));
        request.getRequestDispatcher(DISPATCHER_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.valueOf(request.getPathInfo().substring(1));
        String stringQuantity = request.getParameter(QUANTITY_PARAM);
        int quantity = 0;
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            if(!stringQuantity.matches("\\d+((.|,)\\d+)?")) {
                throw new NumberFormatException();
            }
            format.setParseIntegerOnly(true);
            quantity = format.parse(stringQuantity).intValue();
        } catch (ParseException e) {
            request.setAttribute(ERROR_ATTRIBUTE, NOT_A_NUMBER_ERROR);
            doGet(request, response);
            return;
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE, NOT_A_NUMBER_ERROR);
            doGet(request, response);
            return;
        }
        if(quantity < 1) {
            request.setAttribute(ERROR_ATTRIBUTE, NEGATIVE_NUMBER_ERROR);
            doGet(request, response);
            return;
        }
        try {
            cartService.add(cartService.getCart(request), id, quantity);
        } catch (OutOfStockException e) {
            request.setAttribute(ERROR_ATTRIBUTE, "Out of stock, available " + e.getStock());
            doGet(request, response);
            return;
        }
        response.sendRedirect(String.format("%s%s%d%s", request.getContextPath(), "/products/",
                id, "?message=Product added to cart"));
    }
}
