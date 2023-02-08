<%@ attribute name="command" type="java.lang.String" required="true" %>
<%@ attribute name="page" type="java.lang.Object" required="true" %>
<%@ attribute name="pages" type="java.util.List" required="true" %>
<%@ attribute name="currentPages" type="java.util.List" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<c:if test="${pages.size() > 1}">
    <c:if test="${pages.get(1) > currentPages.size()}">
        <%--        Мб переделать чтобы не создавался внутренний если внешний маленький?--%>
        <tags:checkbutton page="${page}" number="${pages.get(0)}" command="${command}"/>
<%--        <c:choose>--%>
<%--            <c:when test="${page.equals(pages.get(0))}">--%>
<%--                <div class="px-1">--%>
<%--                    <a class="text-white"--%>
<%--                       href="${pageContext.request.contextPath}/controller?action=${action}&page=${pages.get(0) - 1}&sort=${sort}">--%>
<%--                        <input class="btn btn-danger" type="submit" value="${pages.get(0)}">--%>
<%--                    </a>--%>
<%--                </div>--%>
<%--            </c:when>--%>
<%--            <c:otherwise>--%>
<%--                <div class="px-1">--%>
<%--                    <a class="text-white"--%>
<%--                       href="${pageContext.request.contextPath}/controller?action=${action}&page=${pages.get(0) - 1}&sort=${sort}">--%>
<%--                        <input class="btn btn-outline-danger" type="submit" value="${pages.get(0)}">--%>
<%--                    </a>--%>
<%--                </div>--%>
<%--            </c:otherwise>--%>
<%--        </c:choose>--%>
    </c:if>

    <c:forEach var="number" items="${currentPages}">
        <tags:checkbutton page="${page}" number="${number}" command="${command}"/>
<%--        <c:choose>--%>
<%--            <c:when test="${number.equals(page)}">--%>
<%--                <div class="px-1">--%>
<%--                    <a class="text-white"--%>
<%--                       href="${pageContext.request.contextPath}/controller?action=${action}&page=${number - 1}&sort=${sort}">--%>
<%--                        <input class="btn btn-danger" type="submit" value="${number}">--%>
<%--                    </a>--%>
<%--                </div>--%>
<%--            </c:when>--%>
<%--            <c:otherwise>--%>
<%--                <div class="px-1">--%>
<%--                    <a class="text-white"--%>
<%--                       href="${pageContext.request.contextPath}/controller?action=${action}&page=${number - 1}&sort=${sort}">--%>
<%--                        <input class="btn btn-outline-danger" type="submit" value="${number}">--%>
<%--                    </a>--%>
<%--                </div>--%>
<%--            </c:otherwise>--%>
<%--        </c:choose>--%>
    </c:forEach>

    <c:if test="${pages.get(1) > currentPages.size()}">
        <tags:checkbutton page="${page}" number="${pages.get(1)}" command="${command}"/>
<%--        <c:choose>--%>
<%--            <c:when test="${page.equals(pages.get(1))}">--%>
<%--                <div class="px-1">--%>
<%--                    <a class="text-white"--%>
<%--                       href="${pageContext.request.contextPath}/controller?action=${action}&page=${pages.get(1)}&sort=${sort}">--%>
<%--                        <input class="btn btn-danger" type="submit" value="${pages.get(1)}">--%>
<%--                    </a>--%>
<%--                </div>--%>
<%--            </c:when>--%>

<%--            <c:otherwise>--%>
<%--                <div class="px-1">--%>
<%--                    <a class="text-white"--%>
<%--                       href="${pageContext.request.contextPath}/controller?action=${action}&page=${pages.get(1)-1}&sort=${sort}">--%>
<%--                        <input class="btn btn-outline-danger" type="submit" value="${pages.get(1)}">--%>
<%--                    </a>--%>
<%--                </div>--%>
<%--            </c:otherwise>--%>
<%--        </c:choose>--%>
    </c:if>

</c:if>