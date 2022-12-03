package de.hswt.fi.ui.vaadin;

import com.vaadin.server.*;
import com.vaadin.spring.server.SpringVaadinServlet;
import de.hswt.fi.common.spring.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;

@Component(value = "vaadinServlet")
@Profile(Profiles.NOT_TEST)
public class CustomVaadinServlet extends SpringVaadinServlet
		implements SessionInitListener, SessionDestroyListener {

	private static final long serialVersionUID = 1L;

	private final RequestParameterHandler requestHandler;

	@Value("${de.hswt.fi.ui.title}")
	public String uiTitle;

	@Autowired
	public CustomVaadinServlet(RequestParameterHandler requestHandler) {
		this.requestHandler = requestHandler;
	}

	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();
		getService().addSessionInitListener(this);
		getService().addSessionDestroyListener(this);
	}

	@Override
	public void sessionDestroy(SessionDestroyEvent event) {
		event.getSession().removeRequestHandler(requestHandler);
	}

	@Override
	public void sessionInit(SessionInitEvent event) {
		event.getSession().addRequestHandler(requestHandler);

		event.getSession().addBootstrapListener( new BootstrapListener() {

			@Override
			public void modifyBootstrapFragment(
					BootstrapFragmentResponse response) {
				// Do not modify
			}

			@Override
			public void modifyBootstrapPage(BootstrapPageResponse response) {
				response.getDocument().head().prependElement("meta")
						.attr("name", "description")
						.attr("content", uiTitle + ": improvement in the identification of " +
								"organic trace substances: merging of resources and standardization of " +
								"suspected- and non-target analysis");
			}}
		);
	}

}