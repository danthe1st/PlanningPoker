package io.github.danthe1st.poker.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.danthe1st.poker.Constants;
import io.github.danthe1st.poker.Game;
import io.github.danthe1st.poker.PokerProperties;

/**
 * main Registration game Servlet
 * @author Schmid
 *
 */
@WebServlet(urlPatterns="/chat")
public class ChatServlet extends AbstractGameServlet {
	private static final long serialVersionUID = 1L;
/*
 * (non-Javadoc)
 * @see javax.servlet.http.HttpServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)

 * Schreibe eine Chat-Nachricht, falls der Chat erlaubt ist
 */
	@Override
	public void service(final ServletRequest req, final ServletResponse resp) throws ServletException, IOException {
		final HttpServletRequest request=(HttpServletRequest) req;
		final HttpServletResponse response=(HttpServletResponse) resp;
		final String user=assertUserPresent(request, response);
		if(user==null){
			return;
		}
		final Game g=getGame();
		if(PokerProperties.getChatEnabled()){
			final String msg=req.getParameter("chatMsg");
			if(msg==null||!msg.matches(Constants.ALLOWED_IN)||msg.length()>PokerProperties.getMaxCharactarsAllowedIn()){
//				is not allowed
				request.setAttribute(Constants.MSG_ERR, "Message not valid!");
				request.getRequestDispatcher("poker.jsp").forward(request, response);
				return;
			}
			g.addChatMsg("["+user+"]: "+msg);
			System.out.println("\""+user+"\" wrote a new Chat message: \""+msg+"\"");
		}
		response.sendRedirect(getServletContext().getContextPath()+"/Refresh");
	}
}
