package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.dao.sort.SortOrder;
import com.es.phoneshop.dao.sort.SortType;
import com.es.phoneshop.exception.NegativeNumberException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.RecentProductsService;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import com.es.phoneshop.service.impl.HttpSessionRecentProductsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.security.KeyPair;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class ProductListPageServlet extends HttpServlet {
    private static final String PRODUCT_PARAM = "product";
    private static final String SORT_PARAM = "sort";
    private static final String ORDER_PARAM = "order";
    private static final String PRODUCTS_ATTRIBUTE = "products";
    private static final String DISPATCHER_PATH = "/WEB-INF/pages/productList.jsp";
    private static final String PRODUCT_ID_PARAMETER = "productId";
    private static final String QUANTITY_PARAMETER = "quantity";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String NEGATIVE_NUMBER_ERROR = "Quantity must be more than 0";
    private static final String NOT_A_NUMBER_ERROR = "Not a number";
    private ArrayListProductDao productDao;
    private CartService cartService;
    private RecentProductsService recentProductsService;
    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
        recentProductsService = HttpSessionRecentProductsService.getInstance();
        cartService = HttpSessionCartService.getInstance();
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter(PRODUCT_ID_PARAMETER);
        String strQuantity = request.getParameter(QUANTITY_PARAMETER);
        Long id = Long.valueOf(productId);
        int quantity = 0;
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            if(!strQuantity.matches("-?\\d+((.|,)\\d+)?")) {
                throw new NumberFormatException();
            }
            format.setParseIntegerOnly(true);
            quantity = format.parse(strQuantity).intValue();
            if(quantity < 1) {
                throw new NegativeNumberException();
            }
            cartService.add(cartService.getCart(request), id, quantity);
        } catch (ParseException | NumberFormatException
                 | OutOfStockException | NegativeNumberException e) {
            handleErrors(request, id, e);
            request.setAttribute("id", id);
            doGet(request, response);
            return;
        }
        response.sendRedirect(String.format("%s%s", request.getContextPath(),
                "/products?message=Product added to cart"));
    }
    private void handleErrors(HttpServletRequest request, Long id, Exception e) {
        Class<?> errorClass = e.getClass();
        if(errorClass.equals(NumberFormatException.class) || errorClass.equals(ParseException.class)) {
            request.setAttribute(ERROR_ATTRIBUTE, NOT_A_NUMBER_ERROR);
        } else if(errorClass.equals(OutOfStockException.class)) {
            request.setAttribute(ERROR_ATTRIBUTE, "Out of stock, available " + ((OutOfStockException) e).getStock());
        } else if(errorClass.equals(NegativeNumberException.class)) {
            request.setAttribute(ERROR_ATTRIBUTE, NEGATIVE_NUMBER_ERROR);
        }
    }
}
