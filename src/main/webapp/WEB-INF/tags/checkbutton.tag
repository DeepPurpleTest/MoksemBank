<%@ attribute name="page" type="java.lang.Object" required="true" %>
<%@ attribute name="number" type="java.lang.Object" required="true" %>
<%@ attribute name="command" type="java.lang.String" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<c:choose>
    <c:when test="${page.equals(number)}">
        <div class="px-1">
            <a class="text-white"
               href="${command}&page=${number - 1}">
                <input class="btn btn-danger" type="submit" value="${number}">
            </a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="px-1">
            <a class="text-white"
               href="${command}&page=${number - 1}">
                <input class="btn btn-outline-danger" type="submit" value="${number}">
            </a>
        </div>
    </c:otherwise>
</c:choose>