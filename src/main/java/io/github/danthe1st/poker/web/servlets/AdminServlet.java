package io.github.danthe1st.poker.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import io.github.danthe1st.poker.Constants;
import io.github.danthe1st.poker.Game;
import io.github.danthe1st.poker.PokerProperties;
import io.github.danthe1st.poker.web.RefreshSocket;

/**
 * Admin Servlet
 * @author Schmid
 *
 */
@WebServlet(urlPatterns="/Admin")
public class AdminServlet extends AbstractGameServlet {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)

	 * Following doesn't work if Constants.ADMIN_ALLOWED is set to false
	 * 	An Admin Cannot be kicked easily.
	 *	 Get Admin Permissions:
	 *	 	if you are registered:
	 * 			navigate to /Admin(if you don't have Admin Permissions)
	 *	 	else
	 * 			navigate to /Admin?user=<Username>
	 * 	Put Admin Permissions down:
	 *	 	navigate to /Admin(if you have Admin Permissions)
	 *	 give Admin Permissions to another user(the other player doesn't have Admin Permissions recently!)
	 *	 	if you are registered
	 *	 		navigate to /Admin?user=<Username from the other Player>
	 * 		else
	 *	 		register
	 *	 		navigate to /Admin?user=<Username from the other Player>
	 *	 		(logout)
	 *	 take Admin Permissions from another user(the other player has Admin Permissions recently!)
	 *	 	if you are registered
	 *	 		navigate to /Admin?user=<Username from the other Player>
	 *	 	else
	 * 			register
	 * 			navigate to /Admin?user=<Username from the other Player>
	 * 			(logout)
	 * 	find out that a player has got Admin Permissions
	 * 		(register)
	 * 		If the username of the other player is yellow
	 * 			The other player has got Admin Permissions
	 * 		else(white)
	 * 			The other player hasn't got Admin Permissions
	 */
	@Override
	protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {//Anfrage: http-Post
		final HttpServletResponse response=(HttpServletResponse) resp;
		
		final String user=assertUserPresent(req, resp);
		if(user==null){
			return;
		}
		//Keine Admins erlaubt==>Refresh
		if(!PokerProperties.getAdminAllowed()){
			response.sendRedirect(getServletContext().getContextPath()+"/Refresh");
			return;
		}
		
		final Game g=getGame();
		
		if(!isRegistered(user)){//Nicht registriert-->registrieren
			g.registerUser(user);
			
			System.out.println("User "+user+"is now registered");
		}
//Wenn Admin-->Normalbenutzer
//Wenn kein Admin-->Admin
		if(g.isAdmin(user)){
			g.setAdmin(user, false);
			RefreshSocket.reloadAll();
			System.out.println("User \""+user+"\" is no longer Admin");
		}
		else{
			g.setAdmin(user, true);
			RefreshSocket.reloadAll();
			System.out.println("User \""+user+"\" is now Admin");
		}
		
		HttpSession session=req.getSession(false);
		if(session==null){//Sitzung unbekannt
			session = req.getSession(true);
		}
		if(session.getAttribute(Constants.SESSION_USER)==null){
			session.setAttribute(Constants.SESSION_USER, user);
		}
		response.sendRedirect(getServletContext().getContextPath()+"/Refresh");
		return;
	}
}
