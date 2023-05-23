<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="cart" class="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tag:master pageTitle="Product data" recentProducts="${history}">
  <c:if test="${not empty errors}">
    <span class="error">
      There were errors updating cart
    </span>
  </c:if>
  <c:if test="${not empty param.message}">
    <span class="success">
      ${param.message}
    </span>
  </c:if>
  <p></p>
  <form method="post" action="${pageContext.servletContext.contextPath}/cart">
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
        <td></td>
      </tr>
      </thead>
      <c:forEach var="item" items="${cart.items}" varStatus="status">
        <tr>
          <td>
            <img class="product-tile" src="${item.product.imageUrl}">
          </td>
          <td><a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">${item.product.description}</a></td>
          <td>
            <input type="hidden" name="productId" value="${item.product.id}">
            <c:set var="error" value="${errors[item.product.id]}"/>
            <input type="text" name="quantity" value="${not empty error ? paramValues["quantity"][status.index] : item.quantity }" class="quantity">
            <c:if test="${not empty error}">
              <br>
              <span class="error">
                ${error}
              </span>
            </c:if>
          </td>
          <td class="price">
            <a href="${pageContext.servletContext.contextPath}/products/history/${item.product.id}">
              <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
            </a>
          </td>
          <td>
            <button form="deleteCartItem" formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">Delete</button>
          </td>
        </tr>
      </c:forEach>
      <td></td>
      <td></td>
      <td>Total price</td>
      <td><fmt:formatNumber value="${cart.totalCost}" type="currency" currencySymbol="${item.product.currency.symbol}"/></td>
    </table>
    <input type="submit" value="Update">
  </form>
  <form method="post" id="deleteCartItem">
  </form>
</tag:master>