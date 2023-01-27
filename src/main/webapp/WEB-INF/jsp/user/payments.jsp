<%--
  Created by IntelliJ IDEA.
  User: Moksem
  Date: 18.12.2022
  Time: 15:29
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
    <title>Payments</title>
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
    <div class="d-flex">
        <div>
            <a href="${pageContext.request.contextPath}/controller?action=payments&sort=natural">
                <input class="btn btn-primary" type="submit" value="<fmt:message key="client.payments.button.all"/>">
            </a>
        </div>
        <div class="dropdown px-3">
            <button class="btn btn-dark dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                <fmt:message key="client.payments.button.card.sort"/>
            </button>
            <ul class="dropdown-menu">
                <c:forEach var="card" items="${cards}">
                    <li><a class="dropdown-item"
                           href="${pageContext.request.contextPath}/controller?action=payments&sort=natural&card=${card.getId()}">${card.getNumber()}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>

        <div class="dropdown text-end">
            <button class="btn btn-dark dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                <fmt:message key="label.sort"/>
            </button>
            <ul class="dropdown-menu">
                <li><a class="dropdown-item"
                       href="${pageContext.request.contextPath}/controller?action=payments&card=${card}&sort=asc"><fmt:message
                        key="client.payments.tab.sort.old"/></a>
                </li>
                <li><a class="dropdown-item"
                       href="${pageContext.request.contextPath}/controller?action=payments&card=${card}&sort=desc"><fmt:message
                        key="client.payments.tab.sort.new"/></a>
                </li>
            </ul>
        </div>
    </div>

    <%--    <form method="post" action="${pageContext.request.contextPath}/controller?action=payments&sort=natural">--%>
    <%--        <label for="cards"><fmt:message key="client.payments.label.card.sort"/></label>--%>
    <%--        <select name="card" id="cards">--%>
    <%--            <c:forEach var="card" items="${cards}">--%>
    <%--                <option value="${card.getId()}">${card.getNumber()}</option>--%>
    <%--            </c:forEach>--%>
    <%--        </select>--%>

    <%--        <input type="submit" value="<fmt:message key="client.payments.button.card.sort"/>">--%>
    <%--    </form>--%>

    <%--    <form method="post" action="${pageContext.request.contextPath}/controller?action=payments&card=${card}">--%>
    <%--        <label for="all_cards"><fmt:message key="label.sort"/></label>--%>
    <%--        <select name="sort" id="all_cards">--%>
    <%--            <option value="asc"><fmt:message key="client.payments.tab.sort.old"/></option>--%>
    <%--            <option value="desc"><fmt:message key="client.payments.tab.sort.new"/></option>--%>
    <%--        </select>--%>
    <%--        <input type="submit" value="<fmt:message key="button.sort"/>">--%>
    <%--    </form>--%>

    <hr>
    <c:if test="${card.isEmpty()}">
        <c:forEach var="payment" items="${payments}">
            <%--            PaymentId:${payment.getId()}<br>--%>
            <%--            Sender:${payment.getSenderCardNumber()}<br>--%>
            <%--            Receiver:${payment.getReceiverCardNumber()}<br>--%>
            <div class="rounded-3 p-3 my-2 border border-3 border-primary border-opacity-50 d-flex justify-content-between align-items-center">
                <c:out value="${payment.getTime()}"/>
                <c:if test="${payment.getSenderId().equals(user.getId()) &&
          payment.getReceiverId().equals(user.getId())}">
                    <div class="d-flex align-items-center">
                        <p class="m-0"><fmt:message key="client.payments.between"/>:&nbsp;</p>
                    </div>
                    <div class="d-flex align-items-center">
                        <p class="m-0 px-5">
                            <c:out value="${payment.getAmount()}"/>
                        </p>
                        <a target="_blank"
                           href="${pageContext.request.contextPath}/controller?action=createPDF&id=${payment.getId()}">
                            <input class="btn btn-primary" type="submit" value="PDF">
                        </a>
                    </div>
                </c:if>

                <c:if test="${payment.getSenderId().equals(user.getId()) &&
            !payment.getReceiverId().equals(user.getId())}">
                    <div class="d-flex align-items-center">
                        <p class="m-0"><fmt:message
                                key="client.payments.recipient"/>:&nbsp;${payment.getReceiverName()} ${payment.getReceiverSurname()}</p>
                    </div>
                    <div class="d-flex align-items-center">
                        <p class="text-danger m-0 px-5">
                            <c:out value="-${payment.getAmount()}"/>
                        </p>
                        <a target="_blank"
                           href="${pageContext.request.contextPath}/controller?action=createPDF&id=${payment.getId()}">
                            <input class="btn btn-primary" type="submit" value="PDF">
                        </a>
                    </div>
                </c:if>

                <c:if test="${!payment.getSenderId().equals(user.getId()) &&
          payment.getReceiverId().equals(user.getId())}">
                    <div class="d-flex align-items-center">
                        <p class="m-0"><fmt:message
                                key="client.payments.sender"/>:&nbsp;${payment.getSenderName()} ${payment.getSenderSurname()}</p>
                    </div>
                    <div class="d-flex align-items-center">
                        <p class="text-success m-0 px-5">
                            <c:out value="+${payment.getAmount()}"/>
                        </p>
                        <a target="_blank"
                           href="${pageContext.request.contextPath}/controller?action=createPDF&id=${payment.getId()}">
                            <input class="btn btn-primary" type="submit" value="PDF">
                        </a>
                    </div>
                </c:if>
            </div>
        </c:forEach>

    </c:if>

    <c:if test="${!card.isEmpty()}">

        <c:forEach var="payment" items="${payments}">
            <%--            PaymentId:${payment.getId()}<br>--%>
            <%--            Card:${card}<br>--%>
            <%--            Sender:${payment.getSenderCardNumber()}<br>--%>
            <%--            Receiver:${payment.getReceiverCardNumber()}<br>--%>
            <%--            SenderCardId:${payment.getSenderCardId()}<br>--%>
            <%--            ReceiverCardId:${payment.getReceiverCardId()}<br>--%>
            <div class="rounded-3 p-3 my-2 border border-3 border-primary border-opacity-50 d-flex justify-content-between align-items-center">
                <c:out value="${payment.getTime()}"/>
                <c:if test="${(payment.getSenderCardId()).equals(card)}">
                    <div class="d-flex align-items-center">
                        <p class="m-0"><fmt:message key="client.payments.recipient"/>:&nbsp;
                                ${payment.getReceiverName()} ${payment.getReceiverSurname()}</p>
                    </div>
                    <div class="d-flex align-items-center">
                        <p class="text-danger m-0 px-5"><c:out value="-${payment.getAmount()}"/></p>
                        <a target="_blank"
                           href="${pageContext.request.contextPath}/controller?action=createPDF&id=${payment.getId()}">
                            <input class="btn btn-primary" type="submit" value="PDF">
                        </a>
                    </div>
                </c:if>
                <c:if test="${!(payment.getSenderCardId()).equals(card)}">
                    <div class="d-flex align-items-center">
                        <p class="m-0"><fmt:message key="client.payments.sender"/>:&nbsp;
                                ${payment.getSenderName()} ${payment.getSenderSurname()}</p>
                    </div>
                    <div class="d-flex align-items-center">
                        <p class="text-success m-0 px-5"><c:out value="+${payment.getAmount()}"/></p>
                        <a target="_blank"
                           href="${pageContext.request.contextPath}/controller?action=createPDF&id=${payment.getId()}">
                            <input class="btn btn-primary" type="submit" value="PDF">
                        </a>
                    </div>
                </c:if>
            </div>
        </c:forEach>
    </c:if>
</div>

<div class="text-center user-select-none d-flex justify-content-center">
    <c:if test="${pages.size() > 1}">
        <c:if test="${pages.size() > currentPages.size()}">
            <c:if test="${page.equals(pages.get(0))}">
                <div class="px-1">
                    <a class="text-white"
                       href="${pageContext.request.contextPath}/controller?action=payments&card=${card}&page=${pages.get(0) - 1}&sort=${sort}">
                        <input class="btn btn-danger" type="submit" value="${pages.get(0)}">
                    </a>
                </div>
            </c:if>
            <c:if test="${!page.equals(pages.get(0))}">
                <div class="px-1">
                    <a class="text-white"
                       href="${pageContext.request.contextPath}/controller?action=payments&card=${card}&page=${pages.get(0) - 1}&sort=${sort}">
                        <input class="btn btn-outline-danger" type="submit" value="${pages.get(0)}">
                    </a>
                </div>
            </c:if>
        </c:if>
        <c:forEach var="number" items="${currentPages}">
            <c:if test="${number.equals(page)}">
                <div class="px-1">
                    <a class="text-white"
                       href="${pageContext.request.contextPath}/controller?action=payments&card=${card}&page=${number - 1}&sort=${sort}">
                        <input class="btn btn-danger" type="submit" value="${number}">
                    </a>
                </div>
            </c:if>
            <c:if test="${!number.equals(page)}">
                <div class="px-1">
                    <a class="text-white"
                       href="${pageContext.request.contextPath}/controller?action=payments&card=${card}&page=${number - 1}&sort=${sort}">
                        <input class="btn btn-outline-danger" type="submit" value="${number}">
                    </a>
                </div>
            </c:if>
        </c:forEach>
        <c:if test="${pages.size() > currentPages.size()}">

            <c:if test="${page.equals(pages.size())}">
                <div class="px-1">
                    <a class="text-white"
                       href="${pageContext.request.contextPath}/controller?action=payments&card=${card}&page=${pages.size()-1}&sort=${sort}">
                        <input class="btn btn-danger" type="submit" value="${pages.size()}">
                    </a>
                </div>
            </c:if>
            <c:if test="${!page.equals(pages.size())}">
                <div class="px-1">
                    <a class="text-white"
                       href="${pageContext.request.contextPath}/controller?action=payments&card=${card}&page=${pages.size()-1}&sort=${sort}">
                        <input class="btn btn-outline-danger" type="submit" value="${pages.size()}">
                    </a>
                </div>
            </c:if>
        </c:if>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"
        integrity="sha384-oBqDVmMz9ATKxIep9tiCxS/Z9fNfEXiDAYTujMAeBAsjFuCZSmKbSSUnQlmh/jp3"
        crossorigin="anonymous"></script>

</body>
</html>
