<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hello</title>
</head>
<body>
<header>
    <spring:message code="author"/> ${author}
    <select onchange="location = this.value;">
        <option><spring:message code="select.language"/></option>
        <option value="${pageContext.request.contextPath}?lang=ru">Ru</option>
        <option value="${pageContext.request.contextPath}?lang=en">En</option>
    </select>
</header>
<p><spring:message code="time"/> - ${time}</p>
<form method="post" action="/">
    <p><b><spring:message code="input.header"/></b><br/>
        <input name="birthDate" type="text"/>
    </p>
    <spring:message code="submit" var="submitText"/>
    <p><input type="submit" value="${submitText}"></p>
</form>
<p>
    <c:if test="${birthdate != null}">
        <spring:message code="input.birth.date"/> ${birthdate.day}/${birthdate.month}/${birthdate.year}<br/>
    </c:if>
    <c:if test="${age != null}">
        <spring:message code="input.age"/> ${age}<br/>
    </c:if>
    <c:if test="${daysUntilNextBirthday != null}">
        ${daysUntilNextBirthday} <spring:message code="input.days"/> <br/>
    </c:if>
    <c:if test="${error != null}">
        ${error}<br/>
    </c:if>
</p>
<form method="get" action="/users">
    <p><input type="submit" value="Users"></p>
</form>
<form method="get" action="/candidates">
    <p><input type="submit" value="Candidates"></p>
</form>
<form method="get" action="/vacancies">
    <p><input type="submit" value="Vacancies"></p>
</form>
<form method="get" action="/skills">
    <p><input type="submit" value="Skills"></p>
</form>
<form method="get" action="/interviews">
    <p><input type="submit" value="Interviews"></p>
</form>
<form method="get" action="/feedbacks">
    <p><input type="submit" value="Interview Feedback"></p>
</form>
<footer><spring:message code="app.version"/> ${version}</footer>
</body>
</html>

