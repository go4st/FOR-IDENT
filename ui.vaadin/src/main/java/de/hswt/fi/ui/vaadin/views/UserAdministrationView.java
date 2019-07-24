package de.hswt.fi.ui.vaadin.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.ContainerComponent;
import de.hswt.fi.ui.vaadin.handler.security.UserAdministrationViewHandler;
import de.hswt.fi.ui.vaadin.views.components.UserAdministrationComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import java.util.Objects;

@SideBarItem(sectionId = Sections.ADMINISTRATION, captionCode = "USER_ADMINSTRATION_VIEW_CAPTION",
		order = 1)
@VaadinFontIcon(VaadinIcons.USERS)
@SpringView(name = UserAdministrationView.VIEW_NAME)
@Secured("ROLE_ADMIN")
public class UserAdministrationView extends AbstractView {

	private static final long serialVersionUID = 446648980988715063L;

	static final String VIEW_NAME = "useradministration";

	// Do not remove. In order to be created by Spring, this bean needs to wired
	// here. This is same for alle controller classes
	@SuppressWarnings("unused")
	private final UserAdministrationViewHandler handler;

	private final UserAdministrationComponent userAdministrationComponent;

	private final I18N i18n;

	@Autowired
	public UserAdministrationView(UserAdministrationViewHandler handler,
								  UserAdministrationComponent userAdministrationComponent, I18N i18n) {
		this.handler = handler;
		this.userAdministrationComponent = userAdministrationComponent;
		this.i18n = i18n;
	}

	@Override
	protected void postConstruct() {

		setSizeFull();
		addStyleName(CustomValoTheme.RELATIVE);
		addStyleName(CustomValoTheme.BLOCK);

		initHeader();
		initContent();
	}

	private void initHeader() {
		CssLayout headerLayout = new CssLayout();
		headerLayout.setWidth("100%");
		headerLayout.addStyleName(CustomValoTheme.BACKGROUND_COLOR_DEFAULT);
		headerLayout.addStyleName(CustomValoTheme.PADDING);
		headerLayout.addStyleName(CustomValoTheme.CSS_SHADOW_BORDER);
		headerLayout.setHeight(LayoutConstants.HEADER_HEIGHT_VIEW);
		addComponent(headerLayout);

		Label headerLabel = new Label(i18n.get(UIMessageKeys.USER_VIEW_HEADER_LABEL));
		headerLabel.addStyleName(CustomValoTheme.COLOR_ALT1);
		headerLabel.addStyleName(ValoTheme.LABEL_LARGE);
		headerLabel.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
		headerLabel.setWidthUndefined();
		headerLayout.addComponent(headerLabel);
	}

	private void initContent() {
		CssLayout viewWrapper = new CssLayout();
		viewWrapper.addStyleName(CustomValoTheme.SKIP_HEADER_VIEW);
		viewWrapper.addStyleName(CustomValoTheme.BLOCK);
		viewWrapper.addStyleName(CustomValoTheme.RELATIVE);
		viewWrapper.setSizeFull();
		addComponent(viewWrapper);

		CssLayout paddingWrapper = new CssLayout();
		paddingWrapper.addStyleName(CustomValoTheme.PADDING);
		paddingWrapper.setSizeFull();
		viewWrapper.addComponent(paddingWrapper);

		ContainerComponent container = new ContainerComponent();
		container.setSizeFull();
		paddingWrapper.addComponent(container);

		userAdministrationComponent.setSizeFull();

		container.addContentComponent(userAdministrationComponent);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		UserAdministrationView that = (UserAdministrationView) o;
		return Objects.equals(handler, that.handler) &&
				Objects.equals(userAdministrationComponent, that.userAdministrationComponent) &&
				Objects.equals(i18n, that.i18n);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), handler, userAdministrationComponent, i18n);
	}
}