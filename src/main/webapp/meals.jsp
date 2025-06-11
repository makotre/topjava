<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table cellpadding="7" width="80%" align="center" rules="none">
    <tr>
        <td><b>Дата/Время</b></td>
        <td><b>Описание</b></td>
        <td><b>Калории</b></td>
    </tr>
    <c:forEach items="${meals}" var="meal" varStatus="status">
        <c:set var="text_color" value="${meal.excess ? 'red' : 'green'}" />
        <tr style="background-color: ${status.index % 2 == 0 ? '#f2f2f2' : '#ffffff'}; color: ${text_color};">
            <br>
            <c:set var="formattedDate" value="${fn:replace(meal.dateTime.toString(), 'T', ' ')}" />
            <td>${formattedDate}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>