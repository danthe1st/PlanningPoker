package io.github.danthe1st.poker.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Admin Servlet
 * @author Schmid
 *
 */
@WebServlet(urlPatterns="/CheckOtherUsers")
public class OutdatedUsersServlet extends AbstractGameServlet {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String user=assertUserPresent(req, resp);
		if(user==null) {
			return;
		}
		getGame().kickOutdatedUsers();
		resp.sendRedirect(getServletContext().getContextPath()+"/Refresh");	
	}
}
