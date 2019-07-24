package de.hswt.fi.ui.vaadin.windows;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author Marco Luthardt
 */
public abstract class AbstractWindow extends Window {

	private static final long serialVersionUID = -480753909774511137L;

	public enum CloseType {
		OK, CANCEL
	}

	protected final ComponentFactory componentFactory;

	protected final I18N i18n;

	private CssLayout layout;

	private Button closeButton;

	private Button okButton;

	private Button cancelButton;

	private CssLayout footerLayout;

	private Label captionLabel;

	private CloseType closeType = CloseType.CANCEL;

	private CssLayout footerComponent;

	protected abstract String getWindowCaption();

	protected abstract Component getContentComponent();

	protected abstract void handleOk();

	@Autowired
	protected AbstractWindow(ComponentFactory componentFactory, I18N i18N, boolean initContent) {

		this.componentFactory = componentFactory;
		this.i18n = i18N;

		center();
		addCloseShortcut(KeyCode.ESCAPE);
		super.setClosable(false);
		setResizable(false);
		setModal(true);

		layout = new CssLayout();
		layout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);

		if (initContent) {
			postConstruct();
		}

		setContent(layout);
	}

	@PostConstruct
	private void postConstruct() {
		// init header and footer before init the child components
		initHeader();
		initFooter();
		initContent();

		// spacer before bottom buttons
		layout.addComponent(componentFactory.createSpacer("1rem"));

		// add footer layout after content
		layout.addComponent(footerLayout);
	}

	private void initHeader() {
		CssLayout headerLayout = new CssLayout();
		headerLayout.setWidth("100%");
		headerLayout.addStyleName(CustomValoTheme.BACKGROUND_COLOR_ALT3);
		headerLayout.addStyleName(CustomValoTheme.PADDING_HALF);
		layout.addComponent(headerLayout);

		captionLabel = new Label(getWindowCaption());
		captionLabel.setWidthUndefined();
		captionLabel.addStyleName(CustomValoTheme.MARGIN_HALF_LEFT);
		captionLabel.addStyleName(ValoTheme.LABEL_BOLD);
		captionLabel.addStyleName(ValoTheme.LABEL_LARGE);
		captionLabel.addStyleName(CustomValoTheme.COLOR_WHITE);
		headerLayout.addComponent(captionLabel);

		closeButton = new Button();
		closeButton.setIcon(VaadinIcons.CLOSE);
		closeButton.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		closeButton.addStyleName(CustomValoTheme.BORDER_NONE);
		closeButton.addStyleName(CustomValoTheme.BACKGROUND_COLOR_ALT3);
		closeButton.addClickListener(e -> handleCancelClicked());
		headerLayout.addComponent(closeButton);
	}

	private void initContent() {
		CssLayout contentLayout = new CssLayout();
		contentLayout.setSizeFull();
		contentLayout.addStyleName(CustomValoTheme.PADDING_HALF);
		layout.addComponent(contentLayout);

		contentLayout.addComponent(getContentComponent());
	}

	private void initFooter() {
		footerLayout = new CssLayout();

		footerComponent = new CssLayout();
		footerComponent.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		footerComponent.addStyleName(CustomValoTheme.MARGIN_BOTTOM);
		footerComponent.addStyleName(CustomValoTheme.MARGIN_RIGHT);
		footerLayout.addComponent(footerComponent);

		okButton = new Button(i18n.get(UIMessageKeys.ABSTRACT_WINDOW_OK_BUTTON_CAPTION));
		okButton.addStyleName(CustomValoTheme.BACKGROUND_COLOR_ALT3);
		okButton.addStyleName(CustomValoTheme.BORDER_NONE);
		okButton.addClickListener(okClickListener);
		okButton.setClickShortcut(KeyCode.ENTER);
		footerComponent.addComponent(okButton);

		cancelButton = new Button(i18n.get(UIMessageKeys.ABSTRACT_WINDOW_CLOSE_BUTTON_CAPTION));
		cancelButton.addStyleName(CustomValoTheme.BACKGROUND_COLOR_ALT3);
		cancelButton.addStyleName(CustomValoTheme.BORDER_NONE);
		cancelButton.addStyleName(CustomValoTheme.MARGIN_LEFT);
		cancelButton.addClickListener(e -> handleCancelClicked());
		footerComponent.addComponent(cancelButton);
	}

	private void handleOkClicked() {
		closeType = CloseType.OK;
		handleOk();
		close();
	}

	private void handleCancelClicked() {
		closeType = CloseType.CANCEL;
		handleCancel();
		close();
	}

	protected void handleCancel() {
	}

	void setOkButtonCaption(String caption) {
		okButton.setCaption(caption);
	}

	void setOkButtonVisible(boolean visible) {
		okButton.setVisible(visible);
	}

	void setCancelButtonCaption(String caption) {
		cancelButton.setCaption(caption);
	}

	void setCancelButtonVisible(boolean visible) {
		cancelButton.setVisible(visible);
	}

	protected void setCanFinish(boolean canFinish) {
		okButton.setEnabled(canFinish);
	}

	protected void setWindowCaption(String caption) {
		captionLabel.setValue(caption);
	}

	protected void setOkButton(Button button) {
		Objects.requireNonNull(button);
		styleExternalButton(button);
		button.addClickListener(okClickListener);
		footerComponent.replaceComponent(okButton, button);
		okButton = button;
	}

	void replaceOkButton(Button newButton) {
		Objects.requireNonNull(newButton);
		styleExternalButton(newButton);
		footerComponent.replaceComponent(okButton, newButton);
		okButton = newButton;
	}

	private void styleExternalButton(Button newButton) {
		newButton.addStyleName(CustomValoTheme.BACKGROUND_COLOR_ALT3);
		newButton.addStyleName(CustomValoTheme.BORDER_NONE);
	}

	@Override
	public void setClosable(boolean closable) {
		if (closeButton != null) {
			closeButton.setVisible(closable);
		}
		if (closable) {
			addCloseShortcut(KeyCode.ESCAPE);
		} else {
			removeCloseShortcut(KeyCode.ESCAPE);
		}
	}

	public CloseType getCloseType() {
		return closeType;
	}

	@Override
	public void setCaption(String caption) {
		if (captionLabel == null) {
			return;
		}
		captionLabel.setValue(caption);
	}

	@Override
	public String getCaption() {
		if (captionLabel == null) {
			return "";
		}
		return captionLabel.getCaption();
	}

	private ClickListener okClickListener = new ClickListener() {

		private static final long serialVersionUID = 3370104844345590822L;

		@Override
		public void buttonClick(ClickEvent event) {
			handleOkClicked();
		}
	};
}
