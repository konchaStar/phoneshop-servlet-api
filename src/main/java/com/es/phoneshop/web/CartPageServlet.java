package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.NegativeNumberException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.Product;
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
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private static final String PRODUCT_ID_PARAMETER = "productId";
    private static final String QUANTITY_PARAMETER = "quantity";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String NEGATIVE_NUMBER_ERROR = "Quantity must be more than 0";
    private static final String NOT_A_NUMBER_ERROR = "Not a number";

    private ArrayListProductDao productDao;
    private CartService cartService;
    private HttpSessionRecentProductsService recentProductsService;

    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
        cartService = HttpSessionCartService.getInstance();
        recentProductsService = HttpSessionRecentProductsService.getInstance();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart(request));
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues(PRODUCT_ID_PARAMETER);
        String[] quantities = request.getParameterValues(QUANTITY_PARAMETER);
        Map<Long, String> errors = new HashMap<>();
        for(int i = 0; i < productIds.length; i++) {
            Long id = Long.valueOf(productIds[i]);
            int quantity = 0;
            try {
                NumberFormat format = NumberFormat.getInstance(request.getLocale());
                if(!quantities[i].matches("-?\\d+((.|,)\\d+)?")) {
                    throw new NumberFormatException();
                }
                format.setParseIntegerOnly(true);
                quantity = format.parse(quantities[i]).intValue();
                if(quantity < 1) {
                    throw new NegativeNumberException();
                }
                cartService.update(cartService.getCart(request), id, quantity);
            } catch (ParseException | NumberFormatException
                     | OutOfStockException | NegativeNumberException e) {
                handleErrors(errors, id, e);
            }
        }
        if(errors.isEmpty()) {
            response.sendRedirect(String.format("%s%s", request.getContextPath(),
                    "/cart?message=Cart updated successfully"));
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }
    private void handleErrors(Map<Long, String>errors , Long id, Exception e) {
        Class<?> errorClass = e.getClass();
        if(errorClass.equals(NumberFormatException.class) || errorClass.equals(ParseException.class)) {
            errors.put(id, NOT_A_NUMBER_ERROR);
        } else if(errorClass.equals(OutOfStockException.class)) {
            errors.put(id, "Out of stock, available " + ((OutOfStockException) e).getStock());
        } else if(errorClass.equals(NegativeNumberException.class)) {
            errors.put(id, NEGATIVE_NUMBER_ERROR);
        }
    }
}