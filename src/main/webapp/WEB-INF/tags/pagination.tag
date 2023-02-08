<%@ attribute name="command" type="java.lang.String" required="true" %>
<%@ attribute name="page" type="java.lang.Object" required="true" %>
<%@ attribute name="pages" type="java.util.List" required="true" %>
<%@ attribute name="currentPages" type="java.util.List" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<c:if test="${pages.size() > 1}">
    <c:if test="${pages.get(1) > currentPages.size()}">
        <tags:checkbutton page="${page}" number="${pages.get(0)}" command="${command}"/>
    </c:if>

    <c:forEach var="number" items="${currentPages}">
        <tags:checkbutton page="${page}" number="${number}" command="${command}"/>
    </c:forEach>

    <c:if test="${pages.get(1) > currentPages.size()}">
        <tags:checkbutton page="${page}" number="${pages.get(1)}" command="${command}"/>
    </c:if>

</c:if>