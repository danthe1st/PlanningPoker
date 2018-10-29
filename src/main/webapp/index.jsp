<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="imgResolver" class="io.github.danthe1st.poker.web.CardImageResolver"/>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Registration</title>
		<meta charset="UTF-8">
		<link href="./design.css" type="text/css" rel="stylesheet">
	</head>
	<body>
		<div id=bg></div>
		<aside id="help"><a href="${pageContext.request.contextPath}/help.jsp">help-page</a></aside>
		<h1>Issue Schnapser</h1>
		<hr>
		<h3>Please Register</h3>
		<aside id="name"><span>Please Enter your name in the input field.</span></aside>
		
		
		<form action="./register" method="post">
			<label for="usernameIn" title="Enter username:">Enter Username:</label>
			<input name="user" id="usernameIn"/>
			<input type="submit" name="registerUsr" value="Send"/>
		</form>
		<br/>
		<c:if test="${requestScope.errmsg!=null}">
			<span><c:out value="${requestScope.errmsg}" escapeXml="false"/></span>
		</c:if>
		<br/>
		<div id="Cardlist">
			<c:forEach var="card" items="${applicationScope.GAME.allCards}">
			<c:set var="cardImg" value="${imgResolver.getCardImage(card)}"/>
				<img alt="Card" src="${pageContext.request.contextPath}/img/${cardImg}">
			</c:forEach>
		</div>
		<div id=placeHolderFooter><hr>
			(c) 2017 Daniel Schmid <br>
			<br></div>
		<footer>
			<hr>
			(c) 2017 Daniel Schmid <br>
			<br>
		</footer>
	</body>
</html>