<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>
<a href="?sort=${sort}&order=asc&product=${param.product}" style="${param.sort eq sort and param.order eq 'asc' ? 'font-weight: bold' : ''}">&#8593</a>
<a href="?sort=${sort}&order=desc&product=${param.product}" style="${param.sort eq sort and param.order eq 'desc' ? 'font-weight: bold' : ''}">&#8595</a>