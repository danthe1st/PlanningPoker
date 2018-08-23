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
import com.ifco.development.poker.PokerProperties;
/**	
 * main Registration game Servlet
 * @author Schmid
 */
@WebServlet(urlPatterns="/register")
public class RegistrationServlet extends AbstractGameServlet {
	private static final long serialVersionUID = 1L;
/*	
 * (non-Javadoc)
 * @see javax.servlet.http.HttpServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	
 * Registriert einen Benutzer
 * 	
 * Wenn ungültige Zeichen im Benutzernamen vorkommen
 * 	Dispatch zur Login-Seite mit Fehlermeldung: Username not valid!
 * Sonst
 * 	Wenn der Benutzer der erste Benutzer ist
 * 		Starte das Spiel
 * 	Benutzername wird in neuer Sitzung gespeichert
 */	
	@Override
	public void service(final ServletRequest req, final ServletResponse resp) throws ServletException, IOException  {
		final HttpServletRequest request=(HttpServletRequest) req;
		final HttpServletResponse response=(HttpServletResponse) resp;
		final String user = assertUserPresent(request, response);
		if(user==null)return;
		final Game g=getGame();
		final HttpSession session = ((HttpServletRequest)request).getSession(true);				
		if((!user.matches(Constants.ALLOWED_IN))||user.length()>PokerProperties.getMaxCharactarsAllowedIn()){
//			is not allowed
			request.setAttribute(Constants.MSG_ERR, "Username not valid!");
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}
		else if(g.getNumUsers()>=PokerProperties.getMaxUsers()) {
//too many Users
			request.setAttribute(Constants.MSG_ERR, "Overflow Error<br>Too many Users!");
			System.err.println("Overflow Error:");
			System.err.println("\tToo many Users");
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}
		else{//allowed
			System.out.println();
			System.out.println("-----------------------------------------------------");
			System.out.println("User registered:");
			System.out.println("\tUsername: \""+String.valueOf(user)+"\"");
			System.out.println("-----------------------------------------------------");
			System.out.println();
			
			g.registerUser(user);
			
			session.setAttribute(Constants.SESSION_USER, user);
			response.sendRedirect(getServletContext().getContextPath()+"/Refresh");
			return;
		}
	}
}