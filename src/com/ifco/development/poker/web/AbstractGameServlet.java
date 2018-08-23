package com.ifco.development.poker.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ifco.development.poker.Constants;
import com.ifco.development.poker.Game;
import com.ifco.development.poker.PokerProperties;

public abstract class AbstractGameServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * prüft ob Benutzer bekannt ist:
	 * 	ja: return username
	 * 	nein: redirect zu login
	 * 	return null
	 */
	public String assertUserPresent(final HttpServletRequest request, final HttpServletResponse response)throws ServletException, IOException{
		getGame();
		final String username=getUsername(request);
		if(username==null) {//user ist unbekannt
			/*if(request.getSession(false)==null){ //sitzung und user unbekannt
				if(getCard(request)==null){//karte unbekannt(1)
					response.sendRedirect(getServletContext().getContextPath()+"/index.jsp");
					return null;
				}
				else{//Karte bekannt(2)
					response.sendRedirect(getServletContext().getContextPath()+"/index.jsp");
					return null;
				}
				//request.setAttribute(Constants.MSG_ERR, "You need to register your user or login!");//unrelevant becouse of dispatcher
			}
			else{//user unbekannt, Sitzung bekannt
				//sowohl bei bekannter als auch bei unbekannter Karte*/
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return null;
			//}
		}	
		return username;
	}
	/*
	 * Prüft den Status des Benutzers bzw des Spiels
	 * 
	 * Wenn kein Benutzer: Fehler(Exception)
	 * ==>bekannt
	 * Wenn nicht registriert: logout(zum zerstören der Sitzung...)
	 * ==>bekannt, registriert
	 * Wenn keine bekannte Sitzung vorhanden: neue Sitzung mit aktuellem Benutzer
	 * ==>bekannt, registriert, Sitzung vorhanden
	 * Wenn Spiel läuft
	 * 	Wenn Karte gerade ausgewählt
	 * 		Wenn Karte gültig: Spiele Karte
	 * 		Sonst(Karte ungültig): Fehler: konnte Karte nicht setzen, redirect Kartenauswahl
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
	 * Führt Methoden assertUserPresent und assertGameStatus aus

	 * assertUserPresent:
	 * 	prüft ob Benutzer bekannt ist
	 * assertGameStatus
	 * 	Prüft den Status des Benutzers bzw des Spiels
	 */
	public void play(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
	{
		
		final String username=assertUserPresent(request, response);
		if(username==null)return;
		assertGameStatus(request, response);
	}
	
	/*
	 * gibt gerade ausgewählte Karte(falls(in request) vorhanden, sonst null) zurück
	 */
	public String getRequestedCard(final HttpServletRequest request){
		if(request.getParameter(Constants.REQUEST_CARD)!=null){			
			return request.getParameter(Constants.REQUEST_CARD);			
		}
		return null;
	}
	/*
	 * gibt zurzeit ausgewählte Karte zurück
	 * 
	 * Wenn gerade ausgewählt
	 * 	gebe diese zurück
	 * Wenn kein Benutzername vorhanden oder dieser nicht registriert ist
	 * 	gebe null turück
	 * Sonst
	 * 	gebe (im Spiel) gespeicherte Karte zurück
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
	 * Gibt das Spiel zurück

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
	 * Gibt das bekannte Spiel zurück

	 * Setzt in diesem ein übergebenes label
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
			System.out.println("label set: \""+label+"\"");
		}
		return g;
	}
	/*
	 * Gibt das bekannte Spiel zurück

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
	 * Lädt das Label aus der Request
	 */
	public String getLabel(final HttpServletRequest req){
		String label=req.getParameter(Constants.REQ_LABEL);
		if(label==null){
			label=(String) req.getAttribute(Constants.REQ_LABEL);
		}
		
		return label;
	}
	
	/*
	 * prüft ob Sitzung bekannt ist
	 * Wenn bekannt
	 * 	gebe true zurück
	 * Sonst
	 * 	gebe false zurück
	 */
	public boolean isSessionKnown(final HttpServletRequest request)
	{
		final HttpSession session=((HttpServletRequest)request).getSession(false);
		
		if(session!=null)
			return true;
		return false;
	}
	
	/*
	 * gibt Benutzername zurück
	 * 
	 * Wenn Benutzername in request vorhanden
	 * 	gebe diesen zurück
	 * Wenn Sitzung unbekannt oder kein Benutzername in dieser vorhanden
	 * 	gebe null zurück
	 * Sonst
	 * 	gebe Benutzername zurück
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
	 * Prüft ob Benutzer registriert
	 * 
	 * Wenn registriert
	 * 	gebe true zurück
	 * Sonst
	 * 	gebe false zurück
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
	 * Prüft ob der Benutzer eine Karte gesetzt hat
	 * 
	 * Wenn Karte in Request vorhanden
	 * 	Speichere diese im Spiel
	 * 	gebe true turück
	 * Wenn Karte gespeichert
	 * 	gebe true zurück
	 * Sonst
	 * 	gebe false zurück
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
			return true;
		}
		if(g.getCard(username)!=null)
		{
			return true;
		}
		return false;
		
	}
}
