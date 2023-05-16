package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private static final String PRODUCT_ATTRIBUTE = "product";
    private static final String DISPATCHER_PATH = "/WEB-INF/pages/productData.jsp";
    private ArrayListProductDao productDao;
    private CartService cartService;

    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
        cartService = HttpSessionCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getPathInfo();
        request.setAttribute("cart", cartService.getCart(request));
        request.setAttribute(PRODUCT_ATTRIBUTE, productDao.getProduct(Long.valueOf(id.substring(1))));
        request.getRequestDispatcher(DISPATCHER_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.valueOf(request.getPathInfo().substring(1));
        String stringQuantity = request.getParameter("quantity");
        int quantity = 0;
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(stringQuantity).intValue();
        } catch (ParseException e) {
            request.setAttribute("error", "Not a number");
            doGet(request, response);
            return;
        }
        if(quantity < 1) {
            request.setAttribute("error", "Quantity must be more than 0");
            doGet(request, response);
            return;
        }
        try {
            cartService.add(cartService.getCart(request), id, quantity);
        } catch (OutOfStockException e) {
            request.setAttribute("error", "Out of stock, available " + e.getStock());
            doGet(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products/" + id + "?message=Product added to cart");
    }
}
