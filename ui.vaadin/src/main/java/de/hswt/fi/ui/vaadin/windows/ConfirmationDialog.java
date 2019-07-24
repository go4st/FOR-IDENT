package de.hswt.fi.ui.vaadin.windows;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.i18n.I18N;

@SpringComponent
@PrototypeScope
public class ConfirmationDialog<T> extends AbstractWindow {

	private static final long serialVersionUID = 6091438103779826497L;

	private Label descriptionLabel;

	private T dataObject;

	@Autowired
	protected ConfirmationDialog(ComponentFactory componentFactory, I18N i18N) {
		super(componentFactory, i18N, false);
	}

	public void initDialog(String caption, String description) {
		setWindowCaption(caption);
		descriptionLabel.setValue(description);
	}

	@Override
	protected String getWindowCaption() {
		return "";
	}

	@Override
	protected Component getContentComponent() {
		CssLayout contentLayout = new CssLayout();
		contentLayout.setSizeFull();
		contentLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);
		descriptionLabel = new Label();
		descriptionLabel.setWidth("100%");
		descriptionLabel.setStyleName(CustomValoTheme.MARGIN_HALF);

		contentLayout.addComponent(descriptionLabel);

		return contentLayout;
	}

	@Override
	protected void handleOk() {
		// Nothing special to do here
	}

	public T getDataObject() {
		return dataObject;
	}

	public void setDataObject(T dataObject) {
		this.dataObject = dataObject;
	}
}
