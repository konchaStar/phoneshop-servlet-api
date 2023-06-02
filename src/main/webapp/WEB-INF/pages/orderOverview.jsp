<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="order" class="com.es.phoneshop.model.order.Order" scope="request"/>
<tag:master pageTitle="Product data" recentProducts="${history}">
    <h1>Your order</h1>
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
    <table>
        <tag:orderOverviewRow label="First name" name="firstName"  order="${order}"/>
        <tag:orderOverviewRow label="Last name" name="lastName"  order="${order}"/>
        <tag:orderOverviewRow label="Phone" name="phone"  order="${order}"/>
        <tr>
            <td>Delivery date</td>
            <td>${order.deliveryDate}</td>
        </tr>
        <tag:orderOverviewRow label="Delivery address" name="deliveryAddress"  order="${order}"/>
        <tr>
            <c:set var="error" value="${errors['paymentMethod']}"/>
            <td>Payment method</td>
            <td>
                ${order.paymentMethod}
            </td>
        </tr>
    </table>

</tag:master>