<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="product" class="com.es.phoneshop.model.Product" scope="request"/>
<tags:master pageTitle="Product data" >
  <h1>${product.description}</h1>
  <c:if test="${not empty error}">
    <span class="error">
      There was an error adding product
    </span>
  </c:if>
  <c:if test="${not empty param.message && empty error}">
    <span class="success">
      ${param.message}
    </span>
  </c:if>
  <form method="post">
    <table>
      <tr>
        <td>
          Image
        </td>
        <td>
          <img src="${product.imageUrl}">
        </td>
      </tr>
      <tr>
        <td>
          Code
        </td>
        <td>
          ${product.code}
        </td>
      </tr>
      <tr>
        <td>
          Stock
        </td>
        <td>
          ${product.stock}
        </td>
      </tr>
      <tr>
        <td>
          Price
        </td>
        <td>
          <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
        </td>
      </tr>
      <tr>
        <td>Quantity</td>
        <td>
          <input class="quantity" type="text" name="quantity" value="${not empty param.quantity ? param.quantity : '1'}">
          <c:if test="${not empty error}">
            <div class="error">
              ${error}
            </div>
          </c:if>
        </td>
      </tr>
    </table>
    <input type="submit" value="Add to cart">
  </form>
</tags:master>
