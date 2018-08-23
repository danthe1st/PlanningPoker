<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="imgResolver" class="com.ifco.development.poker.web.CardImageResolver"/>
<!DOCTYPE html>
<html lang="de">
	<head>
		<meta charset="UTF-8">
		<title>minimum standards</title>
		<link href="../design.css" type="text/css" rel="stylesheet">
	</head>
	<body>
		<aside id="help">
			<a href="${pageContext.request.contextPath}/Refresh">back to main-page</a>
			<br>
			<a href="${pageContext.request.contextPath}/help.jsp">back to help-page</a>
		</aside>
		<h1>Issue Schnapser</h1>
		<h2>Minimum Standards</h2>
		<hr>
		<ul>
			<li>minimum-size of the Browserwindow(px): 320px * 480px </li>
			<li>HTML5 supporting Browser</li>
			<li>Javaskript activated</li>
			<li>Cookies activated(Session-Cookie)</li>
		</ul>
		<div id="Cardlist">
			<c:forEach var="card" items="${applicationScope.GAME.allCards}">
			<c:set var="cardImg" value="${imgResolver.getCardImage(card)}"/>
				<img alt="Card" src="${pageContext.request.contextPath}/img/${cardImg}">
			</c:forEach>
		</div>
		<div id=placeHolderFooter><hr>
			(c) 2017 Daniel Schmid <br>
			<br>
		</div>
		<footer>
			<hr>
			(c) 2017 Daniel Schmid <br>
			<br>
		</footer>
	</body>
</html>