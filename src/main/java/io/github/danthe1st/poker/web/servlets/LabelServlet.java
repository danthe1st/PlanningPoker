package io.github.danthe1st.poker.web.servlets;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.danthe1st.poker.Game;
/**
 * Start Servlet
 * @author Schmid
 *
 */
@WebServlet(urlPatterns="/Label")
public class LabelServlet extends AbstractGameServlet {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)

	 *	Setzt ein Label
	 *	Redirect zu Refresh
	 */
	@Override
	protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {//~main
		
		assertUserPresent(req, resp);
		
		final Game g=getGameAndSetLabel(req);
		String issue=g.getLabel();
		
		if(issue==null){
			issue="";
		}
		else{
			issue="\""+issue+"\"";
		}
		resp.sendRedirect(getServletContext().getContextPath()+"/Refresh");	
		return;
	}
}
