<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>
<a href="?sort=${sort}&order=asc&product=${param.product}">&#8593</a>
<a href="?sort=${sort}&order=desc&product=${param.product}">&#8595</a>