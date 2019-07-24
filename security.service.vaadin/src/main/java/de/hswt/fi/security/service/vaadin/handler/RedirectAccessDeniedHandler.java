package de.hswt.fi.security.service.vaadin.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This is simple implementation of {@link AccessDeniedHandler} redirecting to
 * the root of the URL. This is a workround to prevent the 403 error when an
 * already logged in user access again the Vaadin login form. This becomes
 * obsolete when https://github.com/peholmst/vaadin4spring/issues/189 has been
 * marked as resolved
 * 
 *
 */
@Component
public class RedirectAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		RequestDispatcher dispatcher = request.getRequestDispatcher("/");
		dispatcher.forward(request, response);

	}

}
