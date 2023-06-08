<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="products" class="java.util.ArrayList" scope="request"/>
<tag:master pageTitle="Product data" recentProducts="${history}">
    <h2>Search</h2>
    <form method="get">
        <label>Description</label>
        <input type="text" name="description" value="${not empty param["description"] ? param["description"] : ""}">
        <select name="parameter">
            <c:forEach var="searchParam" items="${searchParams}">
                <c:choose>
                    <c:when test="${param['parameter'] eq searchParam}">
                        <option selected>
                            ${searchParam}
                        </option>
                    </c:when>
                    <c:otherwise>
                        <option>
                            ${searchParam}
                        </option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </select><br>
        <label>Min price</label>
        <input type="text" name="min" value="${not empty param["min"] ? param["min"] : ""}"><br>
        <c:if test="${not empty errors['min']}">
            <span class="error">${errors['min']}</span>
            <br>
        </c:if>
        <label>Max price</label>
        <input type="text" name="max" value="${not empty param["max"] ? param["max"] : ""}"><br>
        <c:if test="${not empty errors['max']}">
            <span class="error">${errors['max']}</span>
            <br>
        </c:if>
        <c:if test="${not empty errors['minMoreMax']}">
            <span class="error">${errors['minMoreMax']}</span>
            <br>
        </c:if>
        <input type="submit" value="Search" name="search">
    </form>
    <c:if test="${not empty products}">
        <table>
            <thead>
            <td>Image</td>
            <td>Description</td>
            <td>Price</td>
            </thead>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td>
                        <img class="product-tile" src="${product.imageUrl}">
                    </td>
                    <td><a href="${pageContext.servletContext.contextPath}/products/${product.id}">${product.description}</a></td>
                    <td class="price">
                        <a href="${pageContext.servletContext.contextPath}/products/history/${product.id}">
                            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</tag:master>
