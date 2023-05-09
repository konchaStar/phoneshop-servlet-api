<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="prices" class="java.util.ArrayList" scope="request"/>
<tag:master pageTitle="Price history">
  <table>
    <tr>
      <td>
        Start date
      </td>
      <td>
        Price
      </td>
    </tr>
    <c:forEach var="priceHistory" items="${prices}">
      <tr>
        <td>
          ${priceHistory.date}
        </td>
        <td>
          <fmt:formatNumber value="${priceHistory.price}" type="currency" currencySymbol="${priceHistory.currency.symbol}"/>
        </td>
      </tr>
    </c:forEach>
  </table>
</tag:master>