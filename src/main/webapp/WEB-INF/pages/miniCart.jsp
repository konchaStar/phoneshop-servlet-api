<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="cart" class="com.es.phoneshop.model.cart.Cart" scope="request"/>
<a href="${pageContext.servletContext.contextPath}/cart">
    Cart: ${cart.totalQuantity}
</a>
