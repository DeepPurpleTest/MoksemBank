<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <title>Authorization</title>
</head>
<body>

<div class="dropdown text-end mx-3 my-3">
    <button class="btn btn-dark dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
        <fmt:message key="language.label"/>
    </button>
    <ul class="dropdown-menu">
        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/controller?action=i18n&language=en">en</a>
        </li>
        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/controller?action=i18n&language=ua">ua</a>
        </li>
    </ul>
</div>
<%--<form class="text-center" method="post" action="${pageContext.request.contextPath}/controller?action=i18n">--%>
<%--    <label for="language"><fmt:message key="language.label"/></label>--%>
<%--    <select name="language" id="language">--%>
<%--        <option value="en">en</option>--%>
<%--        <option value="ua">ua</option>--%>
<%--    </select>--%>

<%--    <input type="submit" class="btn btn-primary" value="<fmt:message key="language.change"/>">--%>
<%--</form>--%>

<form class="rounded-3 p-3 border border-danger w-25 mx-auto my-5" method="post"
      action="${pageContext.request.contextPath}/controller?action=login">
    <h1 class="h3 mb-5 font-weight-normal text-center"><fmt:message key="login.label.authorization"/></h1>
    <div class="mb-3">
        <label class="form-label text-start" for="phone_number"><fmt:message
                key="login.client.placeholder.phone_number"/></label>
        <input class="form-control" type="text" name="phone_number" id="phone_number"
               value="${userDto.getPhoneNumber()}">
    </div>
    <div class="mb-3">
        <label class="form-label text-start" for="password"><fmt:message key="login.placeholder.password"/></label>
        <input class="form-control" type="password" name="password" id="password">
        <c:if test="${errorMessage != null}">
            <div class="text-danger"><fmt:message key="login.error"/></div>
        </c:if>
    </div>

    <div class="d-flex flex-column">
        <input class="btn btn-outline-danger" type="submit" value="<fmt:message key="login.button.login"/>"/>
        <div class="text-center">
            <a href="${pageContext.request.contextPath}/controller?action=registration_page">
                <input class="btn btn-outline-danger mt-3 text-center" type="button"
                       value="<fmt:message key="login.button.registration"/>">
            </a>
        </div>
        <div class="text-center">
            <a href="${pageContext.request.contextPath}/controller?action=admin">
                <input class="btn btn-outline-danger mt-3 text-center" type="button"
                       value="<fmt:message key="login.button.admin"/>">
            </a>
        </div>
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