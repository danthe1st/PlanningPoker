package io.github.danthe1st.poker.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import io.github.danthe1st.poker.Constants;
import io.github.danthe1st.poker.Game;
import io.github.danthe1st.poker.PokerProperties;
import io.github.danthe1st.poker.web.RefreshSocket;
import io.github.danthe1st.poker.web.WebUtil;

public abstract class AbstractGameServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * pr�ft ob Benutzer bekannt ist:
	 * 	ja: return username
	 * 	nein: redirect zu login
	 * 	return null
	 */
	public String assertUserPresent(final HttpServletRequest request, final HttpServletResponse response)throws ServletException, IOException{
		getGame();
		final String username=getUsername(request);
		if(username==null) {//user ist unbekannt
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return null;
			//}
		}	
		return username;
	}
	/*
	 * Pr�ft den Status des Benutzers bzw des Spiels
	 * 
	 * Wenn kein Benutzer: Fehler(Exception)
	 * ==>bekannt
	 * Wenn nicht registriert: logout(zum zerst�ren der Sitzung...)
	 * ==>bekannt, registriert
	 * Wenn keine bekannte Sitzung vorhanden: neue Sitzung mit aktuellem Benutzer
	 * ==>bekannt, registriert, Sitzung vorhanden
	 * Wenn Spiel l�uft
	 * 	Wenn Karte gerade ausgew�hlt
	 * 		Wenn Karte g�ltig: Spiele Karte
	 * 		Sonst(Karte ung�ltig): Fehler: konnte Karte nicht setzen, redirect Kartenauswahl
	 * 
	 */
	public void assertGameStatus(final HttpServletRequest request, final HttpServletResponse response)throws ServletException, IOException{
		
		final String username=getUsername(request);
		if(username==null)throw new ServletException("cannot assert game status without user present");
		if(PokerProperties.getTimeoutEnabled()) {
			getGame().kickOutdatedUsers();
		}
		getGame().setLastActionTime(username);
		if(!isRegistered(username)){//user bekannt, nicht registriert
			final HttpSession session = request.getSession(false);
			if(session!=null)session.invalidate();
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}
		//hier ist sicher, der Benutzer ist bekannt und registriert					
		if(request.getSession(false)==null){//Sitzung unbekannt
			final HttpSession session = request.getSession(true);
			session.setAttribute(Constants.SESSION_USER, username);
		}
		//hier ist nun sicher, Sitzung vorhanden ,user registriert, user bekannt
		//IM SPIEL
		if(getGame().isRunning()){
			if(getGame().isRecentlyRegistered(username)) {
				request.setAttribute(Constants.MSG_ERR, "Welcome "+username+"!");
				getGame().setRecentlyRegistered(username, false);
			}
			final String card=getRequestedCard(request);
			if(card!=null){//Karte bekannt, spiele sie jetzt
			 if(!getGame().playCard(username, card)){
				 request.setAttribute(Constants.MSG_ERR, "Cannot set Card \""+String.valueOf(card)+"\"");
			 }
			 RefreshSocket.reloadAll();
			}
			//not relevant as forwarding to poker.jsp anyway:
			//request.getRequestDispatcher(getServletContext().getContextPath()+"/poker.jsp").forward(request, response);	
		}
		//not relevant as forwarding to poker.jsp anyway
//		else if(getGame().isCompleted()){
//			request.getRequestDispatcher(getServletContext().getContextPath()+"/poker.jsp").forward(request, response);	
//		}
		request.getRequestDispatcher("/poker.jsp").forward(request, response);
		
		
	}
	/*
	 * F�hrt Methoden assertUserPresent und assertGameStatus aus

	 * assertUserPresent:
	 * 	pr�ft ob Benutzer bekannt ist
	 * assertGameStatus
	 * 	Pr�ft den Status des Benutzers bzw des Spiels
	 */
	public void play(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
	{
		
		final String username=assertUserPresent(request, response);
		if(username==null)return;
		assertGameStatus(request, response);
	}
	
	/*
	 * gibt gerade ausgew�hlte Karte(falls(in request) vorhanden, sonst null) zur�ck
	 */
	public String getRequestedCard(final HttpServletRequest request){
		if(request.getParameter(Constants.REQUEST_CARD)!=null){			
			return request.getParameter(Constants.REQUEST_CARD);			
		}
		return null;
	}
	/*
	 * gibt zurzeit ausgew�hlte Karte zur�ck
	 * 
	 * Wenn gerade ausgew�hlt
	 * 	gebe diese zur�ck
	 * Wenn kein Benutzername vorhanden oder dieser nicht registriert ist
	 * 	gebe null tur�ck
	 * Sonst
	 * 	gebe (im Spiel) gespeicherte Karte zur�ck
	 */
	public String getCard(final HttpServletRequest request){
		final String requestedCard = getRequestedCard(request);
		if(requestedCard!=null)return requestedCard;
		if(getUsername(request)==null)return null;
		final String user=getUsername(request);
		if(!getGame().isUserRegistered(user))return null;
		return getGame().getCard(user);
	}
	
	/*
	 * Gibt das Spiel zur�ck

	 * Wenn keines vorhanden
	 * 	neues Spiel
	 */
	public Game getGame()
	{
		Game g=(Game) this.getServletContext().getAttribute("GAME");
		if(g==null)
		{
			g=WebUtil.newGame();
			this.getServletContext().setAttribute("GAME", g);
		}
		return g;
		
	}

	/*
	 * Gibt das bekannte Spiel zur�ck

	 * Setzt in diesem ein �bergebenes label
	 * Wenn keines vorhanden
	 * 	neues Spiel
	 */
	public Game getGameAndSetLabel(final String label)
	{
		final Game g=getGame();
		if(!(label == null || (label.equals(""))))
		{
			if((!label.matches(Constants.ALLOWED_IN))||label.length()>PokerProperties.getMaxCharactarsAllowedIn()){//public static void main(String[]args){return;}
//				is not allowed
				return g;
			}
			g.setLabel(label);
			RefreshSocket.reloadAll();
			System.out.println("label set: \""+label+"\"");
		}
		return g;
	}
	/*
	 * Gibt das bekannte Spiel zur�ck

	 * Setzt in diesem ein in der Reguest gespeichertes label in das Spiel
	 * Wenn keines vorhanden
	 * 	neues Spiel
	 */
	public Game getGameAndSetLabel(final HttpServletRequest req)
	{
		final String label=getLabel(req);
		
		return getGameAndSetLabel(label);
	}
	
	/*
	 * L�dt das Label aus der Request
	 */
	public String getLabel(final HttpServletRequest req){
		String label=req.getParameter(Constants.REQ_LABEL);
		if(label==null){
			label=(String) req.getAttribute(Constants.REQ_LABEL);
		}
		
		return label;
	}
	
	/*
	 * pr�ft ob Sitzung bekannt ist
	 * Wenn bekannt
	 * 	gebe true zur�ck
	 * Sonst
	 * 	gebe false zur�ck
	 */
	public boolean isSessionKnown(final HttpServletRequest request)
	{
		final HttpSession session=((HttpServletRequest)request).getSession(false);
		
		if(session!=null)
			return true;
		return false;
	}
	
	/*
	 * gibt Benutzername zur�ck
	 * 
	 * Wenn Benutzername in request vorhanden
	 * 	gebe diesen zur�ck
	 * Wenn Sitzung unbekannt oder kein Benutzername in dieser vorhanden
	 * 	gebe null zur�ck
	 * Sonst
	 * 	gebe Benutzername zur�ck
	 */
	public String getUsername(final HttpServletRequest request){
		if(request.getParameter(Constants.SESSION_USER)!=null) 
			return request.getParameter(Constants.SESSION_USER); 
		final HttpSession session=((HttpServletRequest)request).getSession(false);
		if(session==null) {
			((HttpServletRequest)request).getSession(true);
			return null;
		}
		final String username = (String) session.getAttribute(Constants.SESSION_USER);
		if(username==null)return null;
		return String.valueOf(username);
	}
	
	/*
	 * Pr�ft ob Benutzer registriert
	 * 
	 * Wenn registriert
	 * 	gebe true zur�ck
	 * Sonst
	 * 	gebe false zur�ck
	 */
	public boolean isRegistered(final String username)
	{
		final Game g=getGame();
		if(g.isUserRegistered(username))
			return true;
		else
			return false;
	}
	
	/*
	 * Pr�ft ob der Benutzer eine Karte gesetzt hat
	 * 
	 * Wenn Karte in Request vorhanden
	 * 	Speichere diese im Spiel
	 * 	gebe true tur�ck
	 * Wenn Karte gespeichert
	 * 	gebe true zur�ck
	 * Sonst
	 * 	gebe false zur�ck
	 */
	public boolean isCardSet(final String username, final HttpServletRequest request)
	{
		final Game g=getGame();
		
		
		HttpSession session = (request).getSession(false);
		if(session==null)
		{
			session = (request).getSession(true);
		}
		//String card=null;
		if(request.getAttribute(Constants.REQUEST_CARD)!=null){
			g.playCard(username, (String)request.getAttribute(Constants.REQUEST_CARD));
			RefreshSocket.reloadAll();
			return true;
		}
		if(g.getCard(username)!=null)
		{
			return true;
		}
		return false;
		
	}
}
