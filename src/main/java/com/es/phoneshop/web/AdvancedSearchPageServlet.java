package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.dao.search.SearchParameter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdvancedSearchPageServlet extends HttpServlet {
    private static final String SEARCH_JSP = "/WEB-INF/pages/searchPage.jsp";
    private static final String SEARCH_PARAMETER = "search";
    private static final String SEARCH_PARAM = "parameter";
    private static final String DESCRIPTION_PARAMETER = "description";
    private static final String MIN_PRICE_PARAMETER = "min";
    private static final String MAX_PRICE_PARAMETER = "max";
    private static final String PRODUCTS_ATTRIBUTE = "products";
    private static final String ERRORS_ATTRIBUTE = "errors";
    private static final String SEARCH_PARAMS_ATTRIBUTE = "searchParams";
    private static final String NOT_A_NUMBER_ERROR = "Not a number";
    private static final String NEGATIVE_NUMBER_ERROR = "Price must be more than 0";
    private static final String MIN_MORE_MAX_KEY = "minMoreMax";
    private static final String MIN_MORE_THAN_MAX_ERROR = "Max price must be more than min price";
    private ProductDao productDao;
    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(SEARCH_PARAMS_ATTRIBUTE, Arrays.asList(SearchParameter.values()));
        String search = request.getParameter(SEARCH_PARAMETER);
        if(search != null) {
            String description = request.getParameter(DESCRIPTION_PARAMETER);
            String strMinPrice = request.getParameter(MIN_PRICE_PARAMETER);
            String strMaxPrice = request.getParameter(MAX_PRICE_PARAMETER);
            SearchParameter parameter = SearchParameter.valueOf(request.getParameter(SEARCH_PARAM));
            Map<String, String> errors = new HashMap<>();
            BigDecimal minPrice = validateNumber(strMinPrice, errors, MIN_PRICE_PARAMETER);
            BigDecimal maxPrice = validateNumber(strMaxPrice, errors, MAX_PRICE_PARAMETER);
            if(minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
                errors.put(MIN_MORE_MAX_KEY, MIN_MORE_THAN_MAX_ERROR);
            }
            if(errors.isEmpty()) {
                request.setAttribute(PRODUCTS_ATTRIBUTE, productDao.findAdvancedProducts(description,
                        parameter, minPrice, maxPrice));
            } else {
                request.setAttribute(ERRORS_ATTRIBUTE, errors);
            }
        }
        request.getRequestDispatcher(SEARCH_JSP).forward(request, response);
    }
    private BigDecimal validateNumber(String strNumber, Map<String, String> errors, String errorAttribute) {
        try {
            if(strNumber.equals("")) {
                return null;
            }
            Long longNumber = Long.valueOf(strNumber);
            if(longNumber < 0) {
                errors.put(errorAttribute, NEGATIVE_NUMBER_ERROR);
                return null;
            }
            return BigDecimal.valueOf(Long.valueOf(strNumber));
        } catch (NumberFormatException e) {
            errors.put(errorAttribute, NOT_A_NUMBER_ERROR);
        }
        return null;
    }
}
