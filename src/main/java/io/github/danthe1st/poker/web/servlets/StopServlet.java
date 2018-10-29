package io.github.danthe1st.poker.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.danthe1st.poker.Constants;
import io.github.danthe1st.poker.Game;
import io.github.danthe1st.poker.PokerProperties;
import io.github.danthe1st.poker.web.RefreshSocket;

/**
 * Stop Servlet
 * @author Schmid
 *
 */
@WebServlet(urlPatterns="/Stop")
public class StopServlet extends AbstractGameServlet {	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)

	 *	Stoppe das Spiel
	 *	Redirect zu Refresh
	 */
	@Override
	protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {//~main
		final String username = assertUserPresent(req, resp);
		if(username==null){
			return;
		}		
		final Game g=getGame();
		if(!PokerProperties.getStopGameWithoutCardAllowed()) {
			if(!g.hasUserCard(username)){
				req.setAttribute(Constants.MSG_ERR, "Please select a Card before stopping the Game.");
				req.getRequestDispatcher("Refresh").forward(req, (ServletResponse) resp);
				return;
			}
			if(g.getCard(username).equals("Coffee")){
				req.setAttribute(Constants.MSG_ERR, "Please select another Card before stopping the Game.");
				req.getRequestDispatcher("Refresh").forward(req, (ServletResponse) resp);
				return;
			}
		}
		String issue=g.getLabel();
		if(issue==null){
			issue="";
		}
		else{
			issue="\""+issue+"\"";
		}
		System.out.println("\""+username+"\" is stopping the Game "+issue);
		g.stop();
		resp.sendRedirect(getServletContext().getContextPath()+"/Refresh");	
		RefreshSocket.reloadAll();
		return;
	}
}