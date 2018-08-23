package com.ifco.development.poker.web;
	
import java.io.IOException;
	
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
	
@WebFilter(urlPatterns="*.jsp")
public class GameFilter implements Filter {
	@Override
	public void init(final FilterConfig filter) throws ServletException {
	}
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)

	 * Lädt das Spiel und Dispatched den User zu der Index-Seite falls dieser noch nicht bekannt/registriert ist
	 */
	@Override
	public void doFilter(final ServletRequest req, final ServletResponse resp, final FilterChain filterChain) throws IOException, ServletException {
		final HttpServletRequest request=(HttpServletRequest) req;
		WebUtil.getGameAndSetLabel(request);
		filterChain.doFilter(req, resp);//nächsten filter Aufrufen.
	}
	@Override
	public void destroy() {
	}
}