<%--
  Created by IntelliJ IDEA.
  User: Moksem
  Date: 20.12.2022
  Time: 17:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <title>Registration</title>
</head>
<body>

<form class="rounded-3 p-3 border border-danger w-25 mx-auto my-5" method="post"
      action="${pageContext.request.contextPath}/controller?action=registration">
    <h1 class="h3 mb-5 font-weight-normal text-center"><fmt:message key="client.registration.label.registration"/></h1>

    <div class="mb-3">
        <label for="name"><fmt:message key="client.registration.label.name"/>:</label>
        <input class="form-control" type="text" name="name" id="name" value="${dto.getName()}">

        <c:forEach var="error" items="${dto.getErrors()}">
            <c:if test="${error.errorName().equals('name')}">
                <p class="text-danger">${error.message()}</p>
            </c:if>
        </c:forEach>
    </div>


    <div class="mb-3">
        <label for="surname"><fmt:message key="client.registration.label.surname"/>:</label>
        <input class="form-control" type="text" name="surname" id="surname" value="${dto.getSurname()}">
        <c:forEach var="error" items="${dto.getErrors()}">
            <c:if test="${error.errorName().equals('surname')}">
                <p class="text-danger">${error.message()}</p>
            </c:if>
        </c:forEach>
    </div>

    <div class="mb-3">
        <label for="middle"><fmt:message key="client.registration.label.middle"/>:</label>
        <input class="form-control" type="text" name="middle-name" id="middle"
               value="${dto.getMiddleName()}">
        <c:forEach var="error" items="${dto.getErrors()}">
            <c:if test="${error.errorName().equals('middleName')}">
                <p class="text-danger">${error.message()}</p>
            </c:if>
        </c:forEach>
    </div>

    <div class="mb-3">
        <label for="password"><fmt:message key="client.registration.label.password"/>:</label>
        <input class="form-control" type="password" name="password" id="password">
        <c:forEach var="error" items="${dto.getErrors()}">
            <c:if test="${error.errorName().equals('pass')}">
                <p class="text-danger">${error.message()}</p>
            </c:if>
        </c:forEach>
    </div>

    <div class="mb-3">
        <label for="phone"><fmt:message key="client.registration.label.phone"/>:</label>
        <input class="form-control" type="text" name="phone-number" id="phone"
               value="${dto.getPhoneNumber()}">
        <c:forEach var="error" items="${dto.getErrors()}">
            <c:if test="${error.errorName().equals('phone')}">
                <p class="text-danger">${error.message()}</p>
            </c:if>
        </c:forEach>
    </div>


    <div class="d-flex flex-column">
        <input class="btn btn-outline-danger" type="submit" value="<fmt:message key="client.transfer.button.submit"/>"/>
        <div class="text-center">
            <a href="${pageContext.request.contextPath}/">
                <input class="btn btn-outline-danger mt-3 text-center" type="button"
                       value="<fmt:message key="login.button.login"/>">
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
