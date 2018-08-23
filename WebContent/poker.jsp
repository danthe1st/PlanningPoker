<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="javax.activation.ActivationDataFlavor"%>
<%@page import="com.ifco.development.poker.web.WebUtil"%>
<%@page import="com.ifco.development.poker.Game"%>
<%@page import="com.ifco.development.poker.Constants"%>
<%@page import="com.ifco.development.poker.PokerProperties"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<jsp:useBean id="imgResolver" class="com.ifco.development.poker.web.CardImageResolver" />
<%
	final String contextRoot = request.getContextPath();
%>
<!DOCTYPE html>
<html lang=en>
	<head>
		<meta charset="UTF-8" />
		<title>Issue Schnapser</title>
		<link href="${pageContext.request.contextPath}/design.css" type="text/css" rel="stylesheet" />
		<script src="Skripts.js" type="text/javascript"></script>
	</head>
	<body>
		<div id=bg></div>
		<header>
			<div id=buttonsIn>
				<c:if test="${applicationScope.GAME.running }">
					<form action="${pageContext.request.contextPath}/Stop" method="post" id="StopForm">
						<input type="submit" value=stop id="stop" />
					</form>
				</c:if>
				<form action="${pageContext.request.contextPath}/Refresh" method="post" id="RefreshForm">
					<input type="submit" value=refresh id="refresh" />
				</form>
				Auto-Refresh in:
            	<span id="lbltime" ></span>
       	 		seconds
				<br>
				<c:choose>
					<c:when test="${!applicationScope.GAME.running }">
						<form action="${pageContext.request.contextPath}/Start" method="post" id="StartForm">
							<label for="labelIn" title="Issue:">Issue:</label>
							<input name="label" id="labelIn" onkeyup="setStorage(this); InitializeTimer(${PokerProperties.getRefreshTime()})">
							<input type="submit" value=start id="start" />
						</form>
					</c:when>
					<c:otherwise>
						<form action="${pageContext.request.contextPath}/Label" method="post" id="StartForm">
						<label for="labelIn" title="Issue:">Issue:</label>
							<input name="label" id="labelIn" onkeyup="setStorage(this); InitializeTimer(${PokerProperties.getRefreshTime()})">
							<input type="submit" value="set Label" id="start" />
						</form>
					</c:otherwise>
				</c:choose>
			</div>
			<aside id="name">
				<span
					<c:if test="${applicationScope.GAME.isAdmin(sessionScope.user)}">
						class="adminName"
						</c:if>
											><c:out value="${sessionScope.user}"/>
				</span>
				<br/><br/>
				<form action="${pageContext.request.contextPath}/logout" method="post" id="logoutForm">
					<input type="submit" value=logout id="logout" />
				</form>
			</aside>
			<br>
			<h1>Issue Schnapser</h1>
			<c:choose>
				<c:when test="${applicationScope.GAME.isRunning()}">
					<h2>
						How difficult is
						<c:choose>
							<c:when test="${applicationScope.GAME.label==null||applicationScope.GAME.label=='' }">
					 						 it?
					 	</c:when>
							<c:otherwise>
								<c:out value="${applicationScope.GAME.label}" /> ?
					 	</c:otherwise>
						</c:choose>
					</h2>
				</c:when>
				<c:otherwise>
					<h2>Results
						<c:if test="${!(applicationScope.GAME.label==null||applicationScope.GAME.label=='')}">
							 	of ${applicationScope.GAME.label}
						</c:if>
					</h2>
				</c:otherwise>
			</c:choose>
			<c:if test="${requestScope.errmsg!=null}">
				<span><c:out value="${requestScope.errmsg}"/></span>
			</c:if>
			<hr>
		</header>
		<aside id="users"<c:if test="${applicationScope.GAME.completed}">class="left"</c:if>>
			<form action="<%=contextRoot%>/kick" method="post" id="kickForm" accept-charset="UTF-8">
				<table>
					<tbody>
						<c:forEach var="usr" items="${applicationScope.GAME.usernames}">
							<%//You can't kick an Admin and you can't kick yourself%>
							<c:set var="Admin" value="0" />
							<%//Adminprüfung%>
							<c:if test="${applicationScope.GAME.isAdmin(usr)}">
								<c:set var="Admin" value="1" />
							</c:if>
							<c:set var="canKick" value="0" />
							<%//Kickprüfung%>
							<c:if test="${((Admin==0)&&(usr!=sessionScope.user))}">
								<c:set var="canKick" value="1" />
							</c:if>
							<tr>
								<td class="kick-td">
									<c:if test="${canKick==1}">
										<input type="hidden" name="kick_user" value="?" id="kickInput" />
										<input type="image" name="kick_user" src="${pageContext.request.contextPath}/img/kick-img.jpg" alt="kick" class="kick-img" onclick="document.getElementById('kickInput').value='${usr}';">
									</c:if>
								</td>
								<td class="name-td"><span
									<c:if test="${applicationScope.GAME.isAdmin(usr)}">
															class="adminName"</c:if>
																				><c:out value="${usr}" /> </span>
								</td><c:set var="userCard">${imgResolver.getUserCardImage(usr, sessionScope.user,applicationScope.GAME)}</c:set>
								<td class="card-td">
									<img alt="${userCard}" title="${userCard }" src="${pageContext.request.contextPath}/img/${userCard}" class="minicard
										<c:if test="${(userCard== imgResolver.getCardImage(applicationScope.GAME.getResultMax()) )||(userCard==imgResolver.getCardImage(applicationScope.GAME.getResultMin()))} "> problemCard</c:if>
										<c:if test="${(userCard==imgResolver.getCardImage(applicationScope.GAME.getResultAvg())) }"> goodCard</c:if>
										">
								</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr><td></td><td>Number of users:</td><td><c:out value="${applicationScope.GAME.numUsers }"/></td></tr>
						<tr><td></td><td>Number of active users:<br>
							
						</td><td><c:out value="${applicationScope.GAME.numUsers-applicationScope.GAME.getNumUsersWithCard(1001) }"/></td></tr>
					</tfoot>
				</table>
			</form>
		</aside>
		<c:choose>
			<c:when test="${applicationScope.GAME.isRunning() }">
				<form action="${pageContext.request.contextPath}/Refresh" method="post" id="CardSelector">
					<input type="hidden" name="card" value="?" id="cardInput" />
					<c:forEach var="card" items="${applicationScope.GAME.allCards}">
						<c:set var="cardImg" value="${imgResolver.getCardImage(card)}" />
						<input type="image" name="card" title="${card}" src="${pageContext.request.contextPath}/img/${cardImg}" class="card" alt="${card}" onclick="document.getElementById('cardInput').value='${card}';" />
					</c:forEach>
				</form>
			</c:when>
		</c:choose>
		<!-- Result Table -->
		<c:if test="${applicationScope.GAME.completed}">
			<table id="result">
				<tbody>
					<c:choose>
						<c:when test="${applicationScope.GAME.hasUserWithValidCard() }">
							<c:choose>
								<c:when test="${applicationScope.GAME.resultMin==applicationScope.GAME.resultMax }">
									<c:choose>
										<c:when
											test="${applicationScope.GAME.isInfinity(applicationScope.GAME.resultMin)}">
											<%//schlimmster Fall%>
											<tr class="impossible">
												<td>All:</td>
												<td>Infinity</td><td/>
											</tr>
											<tr class="impossible">
												<td colspan="3">The project is impossible!</td>
											</tr>
										</c:when>
										<c:otherwise>
											<%//Bestfall%>
											<tr class="completed">
												<td>All:</td>
												<td><c:out value="${applicationScope.GAME.getResultMax() }" /></td>
												<td/>
											</tr>
											<tr class="completed">
												<td>The project gets</td>
												<td><c:out value="${applicationScope.GAME.resultMax }" />
												<td>Story Points</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${!applicationScope.GAME.isInfinity(applicationScope.GAME.resultMax)}">
											<% //Normalfall%>
											<tr>
												<td>Most:</td>
												<td><c:out value="${applicationScope.GAME.resultMax}" />
												</td>
												<td><c:out value="${applicationScope.GAME.getUsersWithCard(applicationScope.GAME.getResultMax(), \",<br/><br/>\")}" escapeXml='false'/></td>
											</tr>
											<tr>
												<td>Least:</td>
												<td><c:out value="${applicationScope.GAME.resultMin}" />
												</td>
												<td><c:out value="${applicationScope.GAME.getUsersWithCard(applicationScope.GAME.getResultMin(), \",<br/><br/>\")}" escapeXml='false'/></td>
											</tr>
											<tr>
												<td>Average:</td>
												<td><c:out value="${applicationScope.GAME.getResultAvg()}" /></td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr>
												<td>Most:</td>
												<td>INFINITY:</td>
												<td><c:out value="${applicationScope.GAME.getUsersWithCard(applicationScope.GAME.getResultMax(), \",<br/><br/>\")}" escapeXml='false'/></td>
											</tr>
											<tr>
												<td>Least:</td>
												<td><c:out value="${applicationScope.GAME.resultMin}" />
												</td>
												<td><c:out value="${applicationScope.GAME.getUsersWithCard(applicationScope.GAME.getResultMin(), \",<br/><br/>\")}" escapeXml='false'/></td>
											</tr>
											<tr>
												<td>Average without Infinity:</td>
												<td><c:out value="${applicationScope.GAME.getResultAvg()}" /></td>
												<td><c:out value="${applicationScope.GAME.getUsersWithCard(applicationScope.GAME.getResultAvg(), \",<br/><br/>\") }" escapeXml='false'  /></td>
											</tr>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="3"><b><i>ERROR</i></b></td>
							</tr>
							<tr>
								<td colspan="3">No Users with a valid card found</td>
							</tr>
							<c:if test="${((applicationScope.GAME.getNumUsersWithCard(1000)==(applicationScope.GAME.numUsers-applicationScope.GAME.getNumUsersWithCard(1001)))&&(applicationScope.GAME.getNumUsersWithCard(1000)!=0))}">
								<tr class="impossible"><td colspan="3">Nobody knows anything about the Project: Forget it!</td></tr>
							</c:if>
						</c:otherwise>
					</c:choose>
					<c:if test="${applicationScope.GAME.hasUserWithCard(1000) }">
						<tr><td>?</td><td><c:out value="${applicationScope.GAME.getNumUsersWithCard(1000)}"/></td><td><c:out value="${applicationScope.GAME.getUsersWithCard(1000,\",<br/><br/>\")}" escapeXml='false'/></td></tr>
					</c:if>
					<c:if test="${applicationScope.GAME.hasUserWithCard(1001) }">
						<tr><td>Users doing something else</td><td><c:out value="${applicationScope.GAME.getNumUsersWithCard(1001)}" /></td><td><c:out value="${applicationScope.GAME.getUsersWithCard(1001,\",<br/><br/>\")}" escapeXml='false'/></td></tr>
					</c:if>
					<tr><td>valid Cards: </td><td><c:out value="${applicationScope.GAME.numUsersWithNumericCard }"/></td><td class="toLast">of <c:out value="${applicationScope.GAME.numUsers }"/></td></tr>
				</tbody>
			</table>
		</c:if>
			<c:if test="${PokerProperties.getChatEnabled() }">
			<div id=chatFull>
				<div id="chatDisplay"><c:forEach var="chatMsg" items="${applicationScope.GAME.chat }">
					<c:out value="${chatMsg}"/><br>
				</c:forEach></div>
				<form action="<%=contextRoot%>/chat" method="post">
					<label for="chatIn" title="Enter Message:">Enter Message:</label>
					<input name="chatMsg" id="chatIn" onkeyup="InitializeTimer(${PokerProperties.getRefreshTime()})" autocomplete="off"/>
					<input type="submit" name="chat-msg" value="submit"/>
				</form>
			</div>
		</c:if>
		<div id=placeHolderFooter><hr>
			(c) 2017 Daniel Schmid <br>
			<br></div>
		<footer>
			<hr>
			(c) 2017 Daniel Schmid <br>
			<br>
		</footer>
		<script type="text/javascript">
			var func=window.onload;
			window.onload=function(){func();InitializeTimer(${PokerProperties.getRefreshTime()});}
		</script>
	</body>
</html>