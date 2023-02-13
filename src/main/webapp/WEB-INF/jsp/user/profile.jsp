<%--
  Created by IntelliJ IDEA.
  User: Moksem
  Date: 16.01.2023
  Time: 21:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <title>Profile</title>
</head>
<body>

<c:if test="${role.equals('user')}">
    <nav class="navbar navbar-expand-lg bg-body-tertiary mb-5" aria-label="Site menu">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/controller?action=profile"><fmt:message
                    key="button.profile"/></a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-between" id="navbarNav1">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page"
                           href="${pageContext.request.contextPath}/controller?action=main&sort=natural"><fmt:message
                                key="client.main.home"/></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link"
                           href="${pageContext.request.contextPath}/controller?action=client_payments&sort=natural"><fmt:message
                                key="client.main.button.payments"/></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link"
                           href="${pageContext.request.contextPath}/controller?action=transfer"><fmt:message
                                key="client.main.button.transfer"/></a>
                    </li>
                </ul>
                <div class="d-inline-flex">
                    <div class="mx-2">
                        <form method="post" action="${pageContext.request.contextPath}/controller?action=log_out">
                            <input class="btn btn-outline-danger" type="submit"
                                   value="<fmt:message key="button.logout"/>">
                        </form>
                    </div>
                    <div class="dropdown">
                        <button class="btn btn-dark dropdown-toggle" type="button" data-bs-toggle="dropdown"
                                aria-expanded="false">
                            <fmt:message key="language.label"/>
                        </button>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/controller?action=i18n&language=en&sort=natural&redirect_action=profile">en</a>
                            </li>
                            <li><a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/controller?action=i18n&language=ua&sort=natural&redirect_action=profile">ua</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </nav>

    <form class="rounded-3 p-3 border border-danger w-25 mx-auto my-5" method="post"
          action="${pageContext.request.contextPath}/controller?action=change_user">

        <div>
            <p>${user.getName()}</p>
        </div>

        <div class="mb-3">
            <label class="form-label text-start" for="name"><fmt:message key="client.profile.label.name"/></label>
            <input class="form-control" type="text" name="name" id="name" value="${dto.getName()}">
            <c:forEach var="error" items="${dto.getErrors()}">
                <c:if test="${error.errorName().equals('name')}">
                    <p class="text-danger">${error.message()}</p>
                </c:if>
            </c:forEach>
        </div>

        <div>
            <p>${user.getSurname()}</p>
        </div>

        <div class="mb-3">
            <label class="form-label text-start" for="surname"><fmt:message key="client.profile.label.surname"/></label>
            <input class="form-control" type="text" name="surname" id="surname" value="${dto.getSurname()}">
            <c:forEach var="error" items="${dto.getErrors()}">
                <c:if test="${error.errorName().equals('surname')}">
                    <p class="text-danger">${error.message()}</p>
                </c:if>
            </c:forEach>
        </div>

        <div>
            <p>${user.getMiddleName()}</p>
        </div>

        <div class="mb-3">
            <label class="form-label text-start" for="middle_name"><fmt:message
                    key="client.profile.label.middle"/></label>
            <input class="form-control" type="text" name="middle_name" id="middle_name" value="${dto.getMiddleName()}">
            <c:forEach var="error" items="${dto.getErrors()}">
                <c:if test="${error.errorName().equals('middleName')}">
                    <p class="text-danger">${error.message()}</p>
                </c:if>
            </c:forEach>
        </div>

        <div>
            <p>${user.getPhoneNumber()}</p>
        </div>

        <div class="mb-3">
            <label class="form-label text-start" for="phone_number"><fmt:message
                    key="client.profile.label.phone"/></label>
            <input class="form-control" type="text" name="phone_number" id="phone_number"
                   value="${dto.getPhoneNumber()}">
            <c:forEach var="error" items="${dto.getErrors()}">
                <c:if test="${error.errorName().equals('phone')}">
                    <p class="text-danger">${error.message()}</p>
                </c:if>
            </c:forEach>
        </div>

        <div class="mb-3">
            <label class="form-label text-start" for="password"><fmt:message key="label.password"/></label>
            <input class="form-control" type="password" name="password" id="password">
            <c:forEach var="error" items="${dto.getErrors()}">
                <c:if test="${error.errorName().equals('pass')}">
                    <p class="text-danger">${error.message()}</p>
                </c:if>
            </c:forEach>
        </div>

        <div class="text-center">
            <input class="btn btn-outline-danger" type="submit" value="<fmt:message key="button.submit"/>">
        </div>
    </form>
</c:if>

<c:if test="${role.equals('admin')}">
    <nav class="navbar navbar-expand-lg bg-body-tertiary mb-5" aria-label="Site menu">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/controller?action=profile"><fmt:message
                    key="button.profile"/></a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-between" id="navbarNav2">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page"
                           href="${pageContext.request.contextPath}/controller?action=users&sort=natural">
                            <fmt:message key="client.main.home"/></a>
                    </li>
                </ul>
                <div class="d-inline-flex">
                    <div class="mx-2">
                        <form method="post" action="${pageContext.request.contextPath}/controller?action=log_out">
                            <input class="btn btn-outline-danger" type="submit"
                                   value="<fmt:message key="button.logout"/>">
                        </form>
                    </div>
                    <div class="dropdown">
                        <button class="btn btn-dark dropdown-toggle" type="button" data-bs-toggle="dropdown"
                                aria-expanded="false">
                            <fmt:message key="language.label"/>
                        </button>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/controller?action=i18n&language=en&sort=natural&redirect_action=profile">en</a>
                            </li>
                            <li><a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/controller?action=i18n&language=ua&sort=natural&redirect_action=profile">ua</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </nav>

    <form class="rounded-3 p-3 border border-danger w-25 mx-auto my-5" method="post"
          action="${pageContext.request.contextPath}/controller?action=change_user">

        <div>
            <p>${admin.getLogin()}</p>
        </div>

        <div class="mb-3">
            <label class="form-label text-start" for="login"><fmt:message key="admin.profile.label.login"/></label>
            <input class="form-control" type="text" name="login" id="login" value="${dto.getLogin()}">
            <c:forEach var="error" items="${dto.getErrors()}">
                <c:if test="${error.errorName().equals('login')}">
                    <p class="text-danger">${error.message()}</p>
                </c:if>
            </c:forEach>
        </div>

        <div class="mb-3">
            <label class="form-label text-start" for="password"><fmt:message key="label.password"/></label>
            <input class="form-control" type="password" name="password" id="password">
            <c:forEach var="error" items="${dto.getErrors()}">
                <c:if test="${error.errorName().equals('pass')}">
                    <p class="text-danger">${error.message()}</p>
                </c:if>
            </c:forEach>
        </div>
        <div class="text-center">
            <input class="btn btn-outline-danger" type="submit" value="<fmt:message key="button.submit"/>">
        </div>
    </form>
</c:if>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"
        integrity="sha384-oBqDVmMz9ATKxIep9tiCxS/Z9fNfEXiDAYTujMAeBAsjFuCZSmKbSSUnQlmh/jp3"
        crossorigin="anonymous"></script>

</body>
</html>
