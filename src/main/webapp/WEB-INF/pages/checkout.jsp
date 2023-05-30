<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="order" class="com.es.phoneshop.model.order.Order" scope="request"/>
<tag:master pageTitle="Product data" recentProducts="${history}">
    <c:if test="${not empty errors}">
        <span class="error">
            Errors occurred while placing order
        </span>
    </c:if>
    <c:if test="${not empty param.message}">
        <span class="success">
            ${param.message}
        </span>
    </c:if>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description
            </td>
            <td class="quantity">
                Quantity
            </td>
            <td class="price">Price
            </td>
        </tr>
        </thead>
        <c:forEach var="item" items="${order.items}" varStatus="status">
            <tr>
                <td>
                    <img class="product-tile" src="${item.product.imageUrl}">
                </td>
                <td><a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">${item.product.description}</a></td>
                <td>
                    ${item.quantity}
                </td>
                <td class="price">
                    <a href="${pageContext.servletContext.contextPath}/products/history/${item.product.id}">
                        <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
                    </a>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td></td>
            <td></td>
            <td>Subtotal</td>
            <td><fmt:formatNumber value="${order.subTotal}" type="currency" currencySymbol="${order.items[0].product.currency.symbol}"/></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td>Delivery cost</td>
            <td><fmt:formatNumber value="${order.deliveryCost}" type="currency" currencySymbol="${order.items[0].product.currency.symbol}"/></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td>Total cost</td>
            <td><fmt:formatNumber value="${order.totalCost}" type="currency" currencySymbol="${order.items[0].product.currency.symbol}"/></td>
        </tr>
    </table>
    <h2>Your details</h2>
    <form method="post">
        <table>
            <tag:orderRowForm label="First name" name="firstName" errors="${errors}" order="${order}"/>
            <tag:orderRowForm label="Last name" name="lastName" errors="${errors}" order="${order}"/>
            <tag:orderRowForm label="Phone" name="phone" errors="${errors}" order="${order}"/>
            <tr>
                <c:set var="error" value="${error['deliveryAddress']}"/>
                <td>Delivery date</td>
                <td><input name="deliveryDate" type="date" value="${not empty error ? error['deliveryAddress'] : order.deliveryDate}"></td>
            </tr>
            <tag:orderRowForm label="Delivery address" name="deliveryAddress" errors="${errors}" order="${order}"/>
            <tr>
                <c:set var="error" value="${errors['paymentMethod']}"/>
                <td>Payment method</td>
                <td>
                    <select name="paymentMethod">
                        <option></option>
                        <c:forEach var="paymentMethod" items="${paymentMethods}">
                            <c:choose>
                                <c:when test="${empty error && paymentMethod eq order.paymentMethod}">
                                    <option selected="selected">
                                            ${paymentMethod}
                                    </option>
                                </c:when>
                                <c:otherwise>
                                    <option>
                                            ${paymentMethod}
                                    </option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty error}">
                        <div class="error">
                            ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <br><input type="submit" value="Place order">
    </form>
</tag:master>