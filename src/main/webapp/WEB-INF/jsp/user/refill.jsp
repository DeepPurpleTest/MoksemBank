<%--
  Created by IntelliJ IDEA.
  User: Moksem
  Date: 19.12.2022
  Time: 16:30
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
    <title>Title</title>
</head>
<body>
<nav class="navbar navbar-expand-lg bg-body-tertiary mb-5">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/controller?action=profile"><fmt:message
                key="button.profile"/></a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-between" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page"
                       href="${pageContext.request.contextPath}/controller?action=account&sort=natural">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/controller?action=payments&sort=natural"><fmt:message
                            key="client.main.button.payments"/></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/controller?action=transfer&sort=natural"><fmt:message
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
                               href="${pageContext.request.contextPath}/controller?action=i18n&language=en&sort=natural">en</a>
                        </li>
                        <li><a class="dropdown-item"
                               href="${pageContext.request.contextPath}/controller?action=i18n&language=ua&sort=natural">ua</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</nav>

<c:if test="${card.isStatus() == false}">
    <div class="rounded-3 p-3 border border-danger w-25 mx-auto my-5">
        <p class="text-danger"><fmt:message key="card.blocked"/></p>
    </div>
</c:if>


<c:if test="${dto.isStatus() == true}">
    <form class="rounded-3 p-3 border border-danger w-25 mx-auto my-5" method="post"
          action="${pageContext.request.contextPath}/controller?action=refill&card=${dto.getId()}">

        <div class="m-3">
            <p>Card: ${dto.getNumber()}</p>
            <p>Balance: ${dto.getWallet()}</p>
            <label class="form-label text-start" for="refill"><fmt:message key="client.refill.label"/>:</label>
            <input class="form-control" type="text" name="amount" id="refill" value="${dto.getAmount()}"/>
            <c:forEach var="error" items="${dto.getErrors()}">
                <c:if test="${error.errorName().equals('amount')}">
                    <p class="text-danger">${error.message()}</p>
                </c:if>
            </c:forEach>
        </div>
        <div class="text-center">
            <input class="btn btn-outline-danger" type="submit"
                   value="<fmt:message key="client.transfer.button.submit"/>"/>
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
