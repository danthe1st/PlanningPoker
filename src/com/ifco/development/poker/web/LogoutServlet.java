package com.ifco.development.poker.web;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ifco.development.poker.Constants;
import com.ifco.development.poker.Game;
/**
 * main Registration game Servlet
 * @author Schmid
 *
 */
@WebServlet(urlPatterns="/logout")
public class LogoutServlet extends AbstractGameServlet {
	private static final long serialVersionUID = 1L;
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)

	 * Logged den Spieler aus und zerstört die Sitzung
	 */
	@Override
	public void service(final ServletRequest req, final ServletResponse resp) throws ServletException, IOException {
		final HttpServletRequest request=(HttpServletRequest) req;
		final HttpServletResponse response=(HttpServletResponse) resp;
		final String username = assertUserPresent(request, response);
		if(username==null)return;
		final Game g=getGame();
		g.unregisterUser(username);
		final HttpSession session = request.getSession(false);
		if(session!=null)session.invalidate();		
		request.setAttribute(Constants.MSG_ERR, "You are logged out successfully.");
		request.getRequestDispatcher("index.jsp").forward(request, response);
		return;
	}
}
