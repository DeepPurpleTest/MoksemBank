<%--
  Created by IntelliJ IDEA.
  User: Moksem
  Date: 21.12.2022
  Time: 17:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <title>Error</title>
</head>
<body>

<h1>Something go wrong</h1>
<c:if test="${!errorMessage.isEmpty()}">
    <div style="color: red">${errorMessage}</div>
</c:if>

</body>
</html>
