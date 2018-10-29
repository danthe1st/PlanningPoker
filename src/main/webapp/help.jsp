<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="imgResolver" class="io.github.danthe1st.poker.web.CardImageResolver"/>
<!DOCTYPE html>
<html lang="de">
	<head>
		<meta charset="UTF-8">
		<title>Issue-Schnapser-help</title>
		<link href="./design.css" type="text/css" rel="stylesheet"/>
	</head>
	<body>
		<div id="bg"></div>
		<aside id="help"><a href="./Refresh">back to main-page</a></aside>
		<h1>Issue Schnapser</h1>
		<h2>Help-Page</h2>
		<hr>
		<h3>Links</h3>
		<ul>
		<li><a href="${pageContext.request.contextPath}/doc/Manual.pdf" target="_blank">Manual(english)</a></li>
			<li><a href="${pageContext.request.contextPath}/doc/Benutzerbeschreibung.pdf" target="_blank">Manual(german)</a></li>
			<li><a href="${pageContext.request.contextPath}/doc/Mindestanforderungen.jsp">minimum standards</a></li>
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