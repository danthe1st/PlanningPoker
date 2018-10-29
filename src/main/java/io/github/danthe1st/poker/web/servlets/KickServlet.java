package io.github.danthe1st.poker.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.danthe1st.poker.Game;
import io.github.danthe1st.poker.PokerProperties;
import io.github.danthe1st.poker.web.RefreshSocket;
/**
 * main Registration game Servlet
 * @author Schmid
 *
 */
@WebServlet(urlPatterns="/kick/*")
public class KickServlet extends AbstractGameServlet {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)

	 * unregistriert einen gew�nschten Spieler und redirected auf /Refresh
	 */
	@Override
	protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {//~main
		if(assertUserPresent(req, resp)==null)return;
		final String user=req.getParameter("kick_user");
		
		final boolean Admin=(getGame().isAdmin(user))&&PokerProperties.getAdminAllowed();
		
		final Game g=getGame();
		if(isRegistered(getUsername(req))&&!Admin){
			g.unregisterUser(user);
		}
		resp.sendRedirect("Refresh");
		RefreshSocket.reloadAll();
		return;
	}
}
