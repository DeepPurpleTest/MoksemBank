<%--
  Created by IntelliJ IDEA.
  User: Moksem
  Date: 08.01.2023
  Time: 19:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <title>Admin</title>
</head>
<body>

<div class="dropdown text-end mx-3 my-3">
    <button class="btn btn-dark dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
        <fmt:message key="language.label"/>
    </button>
    <ul class="dropdown-menu">
        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/controller?action=i18n&language=en&redirect_action=admin_login_page">en</a>
        </li>
        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/controller?action=i18n&language=ua&redirect_action=admin_login_page">ua</a>
        </li>
    </ul>
</div>

<form class="rounded-3 p-3 border border-danger w-25 mx-auto my-5" method="post"
      action="${pageContext.request.contextPath}/controller?action=login_admin">
    <h1 class="h3 mb-5 font-weight-normal text-center"><fmt:message key="login.label.authorization"/></h1>
    <div class="mb-3">
        <label class="form-label text-start" for="login"><fmt:message key="login.admin.label.login"/>:</label>
        <input class="form-control" type="text" name="login" id="login"/>
    </div>

    <div class="mb-3">
        <label class="form-label text-start" for="password"><fmt:message key="login.label.password"/></label>
        <input class="form-control" type="password" name="password" id="password">
        <c:if test="${errorMessage!=null}">
            <div class="text-danger"><fmt:message key="${errorMessage}"/></div>
        </c:if>
    </div>

    <div class="d-flex flex-column">
        <input class="btn btn-outline-danger" type="submit" value="<fmt:message key="login.button.login"/>">
    </div>
</form>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"
        integrity="sha384-oBqDVmMz9ATKxIep9tiCxS/Z9fNfEXiDAYTujMAeBAsjFuCZSmKbSSUnQlmh/jp3"
        crossorigin="anonymous"></script>

</body>
</html>
