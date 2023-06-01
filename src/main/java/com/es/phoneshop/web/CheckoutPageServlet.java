package com.es.phoneshop.web;

import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.DefaultOrderService;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {
    private static final String ERROR_MESSAGE = "Value is required";
    private static final String CHECKOUT_JSP = "/WEB-INF/pages/checkout.jsp";
    private static final String FIRST_NAME_PARAMETER = "firstName";
    private static final String LAST_NAME_PARAMETER = "lastName";
    private static final String PHONE_PARAMETER = "phone";
    private static final String DELIVERY_ADDRESS_PARAMETER = "deliveryAddress";
    private static final String DELIVERY_DATE_PARAMETER = "deliveryDate";
    private static final String PAYMENT_METHOD_PARAMETER = "paymentMethod";
    private static final String ERRORS_ATTRIBUTE = "errors";
    private static final String ORDER_ATTRIBUTE = "order";
    private static final String PAYMENT_METHODS_ATTRIBUTE = "paymentMethods";
    private static final String INVALID_DATE_ERROR = "Invalid date";
    private static final String INVALID_PHONE_ERROR = "Invalid phone";

    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init() {
        cartService = HttpSessionCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(ORDER_ATTRIBUTE, orderService.getOrder(cartService.getCart(request)));
        request.setAttribute(PAYMENT_METHODS_ATTRIBUTE, orderService.getPaymentMethods());
        request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Order order = orderService.getOrder(cartService.getCart(request));
        Map<String, String> errors = new HashMap<>();
        setRequiredParameter(request, FIRST_NAME_PARAMETER, errors, order::setFirstName);
        setRequiredParameter(request, LAST_NAME_PARAMETER, errors, order::setLastName);
        setPhone(request, errors, order);
        setRequiredParameter(request, DELIVERY_ADDRESS_PARAMETER, errors, order::setDeliveryAddress);
        setDeliveryDate(request, errors, order);
        setPaymentMethod(request, errors, order);
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clearCart(request);
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute(ERRORS_ATTRIBUTE, errors);
            request.setAttribute(ORDER_ATTRIBUTE, order);
            request.setAttribute(PAYMENT_METHODS_ATTRIBUTE, orderService.getPaymentMethods());
            request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
        }
    }
    private void setRequiredParameter(HttpServletRequest request, String parameter, Map<String, String> errors,
                                      Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
           errors.put(parameter, ERROR_MESSAGE);
        } else {
            consumer.accept(value);
        }
    }
    private void setPhone(HttpServletRequest request, Map<String, String> errors, Order order) {
        String phone = request.getParameter(PHONE_PARAMETER);
        if (phone == null || phone.isEmpty()) {
            errors.put(PHONE_PARAMETER, ERROR_MESSAGE);
        } else {
            if (!phone.matches("\\+\\d+")) {
                errors.put(PHONE_PARAMETER, INVALID_PHONE_ERROR);
            } else {
                order.setPhone(phone);
            }
        }
    }
    private void setDeliveryDate(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter(DELIVERY_DATE_PARAMETER);
        if (value == null || value.isEmpty()) {
            errors.put(DELIVERY_DATE_PARAMETER, ERROR_MESSAGE);
        } else {
            LocalDate date = LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if(LocalDate.now().compareTo(date) > 0) {
                errors.put(DELIVERY_DATE_PARAMETER, INVALID_DATE_ERROR);
            } else {
                order.setDeliveryDate(date);
            }
        }
    }
    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter(PAYMENT_METHOD_PARAMETER);
        if (value == null || value.isEmpty()) {
            errors.put(PAYMENT_METHOD_PARAMETER, ERROR_MESSAGE);
        } else {
            order.setPaymentMethod(PaymentMethod.valueOf(value));
        }
    }
}
