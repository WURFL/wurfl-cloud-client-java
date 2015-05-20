<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<link rel="stylesheet" href="main.css" type="text/css"></link>
</head>
<body>
<a href="javascript:history.go(-1)">Back</a>
<table>
<tr class="yellow"><td class="title">query time:</td><td>${info.time}</td></tr>
<tr class="yellow"><td class="title">Cloud version:</td><td>${info.cloudVersion}</td></tr>
<tr class="yellow"><td class="title">Client version:</td><td>${info.clientVersion}</td></tr>
<tr class="green"><td class="title">source:</td><td>${device.source}</td></tr>
<tr class="green"><td class="title">id:</td><td>${device.id}</td></tr>
<tr><td class="title">errors:</td><td class="errors">${device.errors}</td></tr>
<tr class="green"><td colspan=2 class="title">capabilities:</td></tr>
<c:forEach var="capab" items="${device.capabilities}" varStatus="stat">
<tr<c:if test="${stat.index %2 == 1}"> class="yellow"</c:if>><td class="titleSmall">${capab.key}</td><td>${capab.value}</td></tr>
</c:forEach>
</table>
<p>
<a href="javascript:history.go(-1)">Back</a>
</body></html>


