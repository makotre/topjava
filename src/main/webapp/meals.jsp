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
        <c:set var = "text_color" value = "green"/>
        <c:if test = "${meal.isExcess()}">
            <c:set var = "text_color" value = "red"/>
        </c:if>
        <tr style="background-color: ${status.index % 2 == 0 ? '#f2f2f2' : '#ffffff'};">
            <br>
            <c:set var="formattedDate" value="${fn:replace(meal.getDateTime().toString(), 'T', ' ')}" />
            <td><font color="${text_color}">
                   ${formattedDate}
                </font></td>
            <td><font color="${text_color}">${meal.getDescription()}</font></td>
            <td><font color="${text_color}">${meal.getCalories()}</font></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>