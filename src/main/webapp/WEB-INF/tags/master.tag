<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>
<%@ attribute name="recentProducts" type="java.util.ArrayList" %>
<html>
<head>
  <title>${pageTitle}</title>
  <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
</head>
<body class="product-list">
  <header>
    <a href="${pageContext.servletContext.contextPath}">
      <img src="${pageContext.servletContext.contextPath}/images/logo.svg"/>
      PhoneShop
    </a>
    <jsp:include page="/cart/minicart"/>
  </header>
  <main>
    <jsp:doBody/>
  </main>
  <footer>
    <h1>Recently viewed</h1>
    <div class="wrap">
      <c:forEach var="product" items="${recentProducts}">
        <div class="recent">
          <img src="${product.imageUrl}" style="width: 50px; height: 60px;"><br>
          <a href="${pageContext.servletContext.contextPath}/products/${product.id}">${product.description}</a>
        </div>
      </c:forEach>
    </div>
    Expert soft
  </footer>
</body>
</html>