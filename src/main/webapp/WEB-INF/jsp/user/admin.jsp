<%--
  Created by IntelliJ IDEA.
  User: Moksem
  Date: 20.12.2022
  Time: 22:48
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
    <title>Admin</title>
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
                       href="${pageContext.request.contextPath}/controller?action=users&sort=natural"><fmt:message
                            key="client.main.home"/></a>
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

<div class="rounded-3 p-3 border border-danger w-50 mx-auto my-2">
    <div>
        <form class="d-flex align-items-end my-3" method="post"
              action="${pageContext.request.contextPath}/controller?action=users">
            <div>
                <label class="form-label" for="sort"><fmt:message key="label.sort"/>:</label>
                <select class="form-select" name="sort" id="sort">
                    <option value="natural"><fmt:message key="tab.sort.all"/></option>
                    <option value="request"><fmt:message key="admin.tab.sort.request"/></option>
                    <option value="card"><fmt:message key="admin.tab.sort.card"/></option>
                    <option value="phone"><fmt:message key="admin.main.tab.sort.phone"/></option>
                </select>
            </div>
            <div class="px-3">
                <label class="form-label text-start" for="number"><fmt:message key="admin.main.label.input"/>:</label>
                <input class="form-control" type="text" id="number" name="number" value="${cardOrPhoneNumber}">
                <c:if test="${!exception.isEmpty()}">
                    <div class="text-danger">${exception}</div>
                </c:if>
            </div>
            <div>
                <input class="btn btn-outline-primary" type="submit" value="<fmt:message key="button.sort"/>">
            </div>
        </form>
    </div>

    <c:forEach var="client" items="${clients}">
        <div class="rounded-3 p-3 my-2 border border-3 border-primary border-opacity-50 d-flex justify-content-between align-items-center">
            <p class="m-0">
                <a class="btn btn-primary"
                   href="${pageContext.request.contextPath}/controller?action=client&id=${client.getId()}">${client.getName()} ${client.getSurname()}</a>
            </p>
            <c:if test="${client.isRequest()}">
                <div class="text-danger"><fmt:message key="admin.main.user.request"/></div>
            </c:if>
            <c:if test="${client.isStatus() == false}">
                <div class="text-danger"><fmt:message key="admin.main.user.blocked"/></div>
            </c:if>
        </div>
    </c:forEach>
</div>

<div class="text-center user-select-none d-flex justify-content-center">
    <c:if test="${pages.size() > 1}">
        <c:if test="${pages.size() > currentPages.size()}">
            <c:if test="${page.equals(pages.get(0))}">
                <div class="px-1">
                    <a class="text-white"
                       href="${pageContext.request.contextPath}/controller?action=users&card=${card}&page=${pages.get(0) - 1}&sort=${sort}">
                        <input class="btn btn-danger" type="submit" value="${pages.get(0)}">
                    </a>
                </div>
            </c:if>
            <c:if test="${!page.equals(pages.get(0))}">
                <div class="px-1">
                    <a class="text-white"
                       href="${pageContext.request.contextPath}/controller?action=users&card=${card}&page=${pages.get(0) - 1}&sort=${sort}">
                        <input class="btn btn-outline-danger" type="submit" value="${pages.get(0)}">
                    </a>
                </div>
            </c:if>
        </c:if>
        <c:forEach var="number" items="${currentPages}">
            <c:if test="${number.equals(page)}">
                <div class="px-1">
                    <a class="text-white"
                       href="${pageContext.request.contextPath}/controller?action=users&card=${card}&page=${number - 1}&sort=${sort}">
                        <input class="btn btn-danger" type="submit" value="${number}">
                    </a>
                </div>
            </c:if>
            <c:if test="${!number.equals(page)}">
                <div class="px-1">
                    <a class="text-white"
                       href="${pageContext.request.contextPath}/controller?action=users&card=${card}&page=${number - 1}&sort=${sort}">
                        <input class="btn btn-outline-danger" type="submit" value="${number}">
                    </a>
                </div>
            </c:if>
        </c:forEach>
        <c:if test="${pages.size() > currentPages.size()}">

            <c:if test="${page.equals(pages.size())}">
                <div class="px-1">
                    <a class="text-white"
                       href="${pageContext.request.contextPath}/controller?action=users&card=${card}&page=${pages.size()-1}&sort=${sort}">
                        <input class="btn btn-danger" type="submit" value="${pages.size()}">
                    </a>
                </div>
            </c:if>
            <c:if test="${!page.equals(pages.size())}">
                <div class="px-1">
                    <a class="text-white"
                       href="${pageContext.request.contextPath}/controller?action=users&card=${card}&page=${pages.size()-1}&sort=${sort}">
                        <input class="btn btn-outline-danger" type="submit" value="${pages.size()}">
                    </a>
                </div>
            </c:if>
        </c:if>
    </c:if>
</div>

<%--<c:if test="${pages.size() > 1}">--%>
<%--    <c:if test="${pages.size() > currentPages.size()}">--%>
<%--        <a href="${pageContext.request.contextPath}/controller?action=users&page=${pages.get(0) - 1}&sort=${sort}">--%>
<%--            <input type="submit" value="${pages.get(0)}">--%>
<%--        </a>--%>
<%--    </c:if>--%>
<%--    <c:forEach var="number" items="${currentPages}">--%>
<%--        <a href="${pageContext.request.contextPath}/controller?action=users&page=${number - 1}&sort=${sort}">--%>
<%--            <input type="submit" value="${number}">--%>
<%--        </a>--%>
<%--    </c:forEach>--%>
<%--    <c:if test="${pages.size() > currentPages.size()}">--%>
<%--        <a href="${pageContext.request.contextPath}/controller?action=users&page=${pages.size()-1}&sort=${sort}">--%>
<%--            <input type="submit" value="${pages.size()}">--%>
<%--        </a>--%>
<%--    </c:if>--%>
<%--</c:if>--%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"
        integrity="sha384-oBqDVmMz9ATKxIep9tiCxS/Z9fNfEXiDAYTujMAeBAsjFuCZSmKbSSUnQlmh/jp3"
        crossorigin="anonymous"></script>
</body>
</html>
