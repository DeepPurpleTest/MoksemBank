<%--
  Created by IntelliJ IDEA.
  User: Moksem
  Date: 18.12.2022
  Time: 11:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <title>User</title>
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
                        <input class="btn btn-outline-danger" type="submit" value="<fmt:message key="button.logout"/>">
                    </form>
                </div>
                <div class="dropdown">
                    <button class="btn btn-dark dropdown-toggle" type="button" data-bs-toggle="dropdown"
                            aria-expanded="false">
                        <fmt:message key="language.label"/>
                    </button>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item"
                               href="${pageContext.request.contextPath}/controller?action=i18n&language=en&sort=natural&redirect_action=main">en</a>
                        </li>
                        <li><a class="dropdown-item"
                               href="${pageContext.request.contextPath}/controller?action=i18n&language=ua&sort=natural&redirect_action=main">ua</a>
                        </li>
                    </ul>
                </div>
            </div>

        </div>
    </div>
</nav>

<div class="rounded-3 p-3 border border-danger w-50 mx-auto my-2">
    <h1> ${user.getName()} ${user.getSurname()} ${user.getMiddleName()}</h1>

    <div class="d-flex justify-content-between align-items-end">
        <form method="post" action="${pageContext.request.contextPath}/controller?action=create">
            <input class="btn btn-primary" type="submit" value="<fmt:message key="client.main.button.create"/>">
        </form>

        <div class="dropdown text-end">
            <button class="btn btn-dark dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                <fmt:message key="label.sort"/>
            </button>
            <ul class="dropdown-menu">
                <li><a class="dropdown-item"
                       href="${pageContext.request.contextPath}/controller?action=main&sort=natural"><fmt:message
                        key="tab.sort.all"/></a>
                </li>
                <li><a class="dropdown-item"
                       href="${pageContext.request.contextPath}/controller?action=main&sort=asc"><fmt:message
                        key="client.main.tab.sort.lower"/></a>
                </li>
                <li><a class="dropdown-item"
                       href="${pageContext.request.contextPath}/controller?action=main&sort=desc"><fmt:message
                        key="client.main.tab.sort.great"/></a>
                </li>
            </ul>
        </div>
    </div>

    <%--    <form method="post" action="${pageContext.request.contextPath}/controller?action=account">--%>
    <%--        <label for="sort"><fmt:message key="label.sort"/></label>--%>
    <%--        <select name="sort" id="sort">--%>
    <%--            <option value="natural"><fmt:message key="tab.sort.all"/></option>--%>
    <%--            <option value="asc"><fmt:message key="client.main.tab.sort.lower"/></option>--%>
    <%--            <option value="desc"><fmt:message key="client.main.tab.sort.great"/></option>--%>
    <%--        </select>--%>

    <%--        <input type="submit" value="<fmt:message key="button.sort"/>">--%>
    <%--    </form>--%>

    <c:forEach var="card" items="${cards}">

        <c:if test="${card.isStatus()}">
            <div class="rounded-3 p-3 my-2 border border-3 border-success border-opacity-50 d-flex justify-content-between align-items-center">
                <p class="m-0">
                        ${card.getNumber()}
                </p>
                <div class="d-flex justify-content-around align-items-center">
                    <div>
                        <p class="m-0">
                            <fmt:message key="client.main.balance"/>:
                                ${card.getWallet()}
                        </p>
                    </div>

                    <div class="px-3">
                        <a href="${pageContext.request.contextPath}/controller?action=get_refill&card=${card.getId()}">
                            <input class="btn btn-primary" type="submit"
                                   value="<fmt:message key="client.main.button.refill"/>">
                        </a>
                    </div>

                    <form class="text-end" method="post"
                          action="${pageContext.request.contextPath}/controller?action=block&card=${card.getId()}">
                        <input class="btn btn-danger" type="submit" value="<fmt:message key="button.block"/>">
                    </form>
                </div>
            </div>
        </c:if>

        <c:if test="${card.isRequest()}">
            <div class="rounded-3 p-3 my-2 border border-3 border-danger border-opacity-50 d-flex align-items-center justify-content-between">
                <div class="d-flex-column">
                    <p class="m-0">
                            ${card.getNumber()}
                    </p>
                    <p class="text-danger m-0"><fmt:message
                            key="card.blocked"/>.&nbsp;<fmt:message
                            key="client.main.request.status"/></p>
                </div>
                <div>
                    <p class="m-0">
                        Balance:
                            ${card.getWallet()}
                    </p>
                </div>
            </div>
        </c:if>

        <c:if test="${!card.isStatus() && !card.isRequest()}">
            <div class="rounded-3 p-3 my-2 border border-3 border-danger border-opacity-50 d-flex align-items-center justify-content-between">

                <div class="d-flex flex-column">
                    <p class="m-0">${card.getNumber()}</p>
                    <p class="text-danger m-0"><fmt:message
                            key="card.blocked"/></p>

                </div>

                <div class="d-flex align-items-center">
                    <p class="px-3 m-0">
                        Balance:
                            ${card.getWallet()}
                    </p>
                    <form method="post"
                          action="${pageContext.request.contextPath}/controller?action=request_unlock&card=${card.getId()}">
                        <input class="btn btn-warning" type="submit" value="<fmt:message key="button.unlock"/>">
                    </form>
                </div>


            </div>
        </c:if>
    </c:forEach>

</div>

<div class="text-center user-select-none d-flex justify-content-center">
    <tags:pagination page="${page}" pages="${pages}" currentPages="${currentPages}"
                     command="${pageContext.request.contextPath}/controller?action=main&sort=${sort}"/>
</div>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"
        integrity="sha384-oBqDVmMz9ATKxIep9tiCxS/Z9fNfEXiDAYTujMAeBAsjFuCZSmKbSSUnQlmh/jp3"
        crossorigin="anonymous"></script>
</body>
</html>
