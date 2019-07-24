package de.hswt.fi.ui.vaadin.components;

import de.hswt.fi.security.service.api.SecurityService;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.SideBarItemDescriptor;
import org.vaadin.spring.sidebar.components.AbstractSideBar;

import java.util.Arrays;

public class SecurityItemFilter implements AbstractSideBar.ItemFilter {

	private SecurityService securityService;

	public SecurityItemFilter(SecurityService securityService) {
		this.securityService = securityService;
	}

	@Override
	public boolean passesFilter(SideBarItemDescriptor descriptor) {
		Secured secured = descriptor.findAnnotationOnBean(Secured.class);
		if (secured != null) {

			if (secured.value().length > 1) {
				return securityService.currentUserHasRole(Arrays.asList(secured.value()));
			}
			return securityService.currentUserHasRole(secured.value()[0]);
		}
		return true;
	}

}
