<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List" recentProducts="${history}">
  <br>
  <form>
    <input type="text" name="product" value="${param.product}">
    <input type="submit" value="Search">
  </form>
  <c:if test="${not empty param.message}">
    <span class="success">
      ${param.message}
    </span>
  </c:if>
<%--  <c:if test="${not empty error}">--%>
<%--    <span class="error">--%>
<%--      There was an error adding to cart--%>
<%--    </span>--%>
<%--  </c:if>--%>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>Description
          <tags:sortList sort="description"/>
        </td>
        <td class="quantity">
          Quantity
        </td>
        <td class="price">Price
          <tags:sortList sort="price"/>
        </td>
        <td></td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <c:set var="productId" value="${product.id}"/>
      <tr>
        <form method="post" action="${pageContext.servletContext.contextPath}/products">
        <td>
          <img class="product-tile" src="${product.imageUrl}">
        </td>
        <td><a href="${pageContext.servletContext.contextPath}/products/${product.id}">${product.description}</a></td>
        <td>
          <input type="hidden" name="productId" value="${product.id}">
          <input class="quantity" type="text" name="quantity" value="${not empty error && id eq productId ? param.quantity : 1}">
          <c:if test="${not empty error && id eq productId}">
            <br>
            <span class="error">
              ${error}
            </span>
          </c:if>
        </td>
        <td class="price">
          <a href="${pageContext.servletContext.contextPath}/products/history/${product.id}">
            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          </a>
        </td>
        <td>
          <input type="submit" value="Add to cart">
        </td>
        </form>
      </tr>
    </c:forEach>
  </table>

</tags:master>