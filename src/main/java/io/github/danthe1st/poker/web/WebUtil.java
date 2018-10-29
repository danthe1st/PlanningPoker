package io.github.danthe1st.poker.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import io.github.danthe1st.poker.Constants;
import io.github.danthe1st.poker.Game;

public class WebUtil {
	/*
	 * sucht den Benutzernamen aus der Request
	 */
	public static String getUsername(final HttpServletRequest request){
		if(request.getParameter(Constants.SESSION_USER)!=null) 
			return request.getParameter(Constants.SESSION_USER); 
		final HttpSession session=((HttpServletRequest)request).getSession(false);
		if(session==null) return null;
		final Object username = session.getAttribute(Constants.SESSION_USER);
		if(username==null)return null;
		return  String.valueOf(username);
	}
	/*
	 * L�dt das Spiel
	 * Falls ein label in der Request bekannt
	 * 	�ndere das label des Spiels aub das label der Request
	 */
	public static Game getGameAndSetLabel(final HttpServletRequest req){
		Game g=(Game) req.getServletContext().getAttribute("GAME");
		final String label=(String)req.getAttribute("label");
		
		if(g==null)
		{
			g=newGame(req);
			req.getServletContext().setAttribute("GAME", g);
		}
		if(label!=null){
			g.setLabel(label);
			RefreshSocket.reloadAll();
		}
		return g;
	}
	/*
	 * Erstellt neues Spiel und gibt dieses zur�ck
	 */
	public static Game newGame()
	{
		final Game g=new Game();
		return g;
	}
	public static Game newGame(final HttpServletRequest req)
	{
		final Game g=newGame();
		req.getServletContext().setAttribute("GAME", g);
		final String label=(String)req.getAttribute("label");
		if(label!=null){
			g.setLabel(label);
		}
		return g;
	}
}