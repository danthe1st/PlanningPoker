package io.github.danthe1st.poker.web;

import java.util.EventListener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import io.github.danthe1st.poker.Constants;
import io.github.danthe1st.poker.Game;

/**
 * Application Lifecycle Listener implementation class GameDestroyer
 * 
 * Remove user when session is destroyed
 * destroy the Game when no users playing
 */
@WebListener
public class GameDestroyer implements EventListener, HttpSessionListener{
	public void sessionDestroyed(final HttpSessionEvent se)  { 
		final Game g=(Game) se.getSession().getServletContext().getAttribute("GAME");
		if(g==null) {
			return;
		}
		final String user=(String) se.getSession().getAttribute(Constants.SESSION_USER);
		if(user!=null&&g.isUserRegistered(user)) {
			g.unregisterUser(user);
		}
		if(!g.hasUsers()) {
			se.getSession().getServletContext().removeAttribute("GAME");
			System.out.println("deleted Game because no users playing");
		}else {
			RefreshSocket.reloadAll();
		}
   }

	@Override
	public void sessionCreated(final HttpSessionEvent se) {
		// TODO Auto-generated method stub
		
	}
}
