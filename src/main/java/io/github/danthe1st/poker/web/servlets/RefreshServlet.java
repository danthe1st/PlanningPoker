package io.github.danthe1st.poker.web.servlets;


import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * main Registration game Servlet
 * @author Schmid
 *
 */
@WebServlet(urlPatterns="/Refresh")//TODO uses: RefreshSocket.reloadAll();
public class RefreshServlet extends AbstractGameServlet {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)

	 * Refresh(f�hrt play von AbstactGameServlet aus)
	 */
	@Override
	protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {//~main	
		play(req, resp);		
	}
}
