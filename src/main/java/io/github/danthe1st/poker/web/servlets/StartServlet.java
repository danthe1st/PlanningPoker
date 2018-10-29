package io.github.danthe1st.poker.web.servlets;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.danthe1st.poker.Game;
import io.github.danthe1st.poker.web.RefreshSocket;
/**
 * Start Servlet
 * @author Schmid
 *
 */
@WebServlet(urlPatterns="/Start")
public class StartServlet extends AbstractGameServlet {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)

	 *	Starte das Spiel
	 *	Redirect zu Refresh
	 */
	@Override
	protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {//~main
		final String username = assertUserPresent(req, resp);
		if(username==null){
			return;
		}
		final Game g=getGameAndSetLabel(req);
		String issue=g.getLabel();
		
		if(issue==null){
			issue="";
		}
		else{
			issue="\""+issue+"\"";
		}
		System.out.println("\""+username+"\" is starting the Game "+issue);
		g.start(username);
		resp.sendRedirect(getServletContext().getContextPath()+"/Refresh");	
		RefreshSocket.reloadAll();
		return;
	}
}
