package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import com.vaadin.ui.renderers.HtmlRenderer;
import de.hswt.fi.security.service.api.SecurityService;
import de.hswt.fi.security.service.model.RegisteredUser;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.ContainerContentComponent;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import de.hswt.fi.ui.vaadin.grid.FilterGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@SpringComponent
@ViewScope
public class UserAdministrationComponent extends ContainerContentComponent {

	private static final long serialVersionUID = -1107688050010166441L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserAdministrationComponent.class);

	private final ViewEventBus eventBus;

	private final ComponentFactory componentFactory;

	private final SecurityService securityService;

	private final I18N i18n;

	private RegisteredUser currentSelection;

	private List<RegisteredUser> users;

	private Label titleLabel;

	private FilterGrid<RegisteredUser> grid;

	@Autowired
	public UserAdministrationComponent(ViewEventBus eventBus, ComponentFactory componentFactory,
									   SecurityService securityService, I18N i18n) {
		this.eventBus = eventBus;
		this.componentFactory = componentFactory;
		this.securityService = securityService;
		this.i18n = i18n;
	}

	@PostConstruct
	private void postConstruct() {
		titleLabel = new Label("");

		initGrid();
		updateGridData();

		eventBus.subscribe(this);
	}

	@PreDestroy
	private void preDestroy() {
		eventBus.unsubscribe(this);
	}

	private void initGrid() {

		grid = new FilterGrid<>(RegisteredUser.class);
		grid.setSizeFull();
		grid.setSelectionMode(SelectionMode.SINGLE);
		((SingleSelectionModel<RegisteredUser>) grid.getSelectionModel()).setDeselectAllowed(false);

		grid.addSelectionListener(event -> {
			RegisteredUser selectedItem = event.getFirstSelectedItem().orElse(null);
			if (selectedItem == null) {
				return;
			}
			currentSelection = selectedItem;
		});
		grid.addItemClickListener(event -> {
			if (event.getMouseEventDetails().isDoubleClick()) {
				handleEditUser();
			}
		});

		grid.removeAllColumns();

		initColumns();
		addComponent(grid);
	}

	private void initColumns() {
		grid.addFilterColumn(RegisteredUser::getUsername)
				.setFilterType(String.class)
				.setCaption(i18n.get(UIMessageKeys.USER_USERNAME));

		grid.addFilterColumn(registeredUser -> componentFactory.getCheckedIconHtml(registeredUser.isEnabled()), new HtmlRenderer())
				.setFilterValueProvider(RegisteredUser::isEnabled)
				.setFilterCaptionProvider(item -> item ? i18n.get(UIMessageKeys.USER_ENABLED) : i18n.get(UIMessageKeys.USER_DISABLED_CAPTION), Boolean.class)
				.setWidth(120)
				.setCaption(i18n.get(UIMessageKeys.USER_ENABLED))
				.setStyleGenerator(item -> CustomValoTheme.TEXT_CENTER);

		grid.addFilterColumn(RegisteredUser::getFirstname)
				.setFilterType(String.class)
				.setCaption(i18n.get(UIMessageKeys.USER_FIRSTNAME));

		grid.addFilterColumn(RegisteredUser::getLastname)
				.setFilterType(String.class)
				.setCaption(i18n.get(UIMessageKeys.USER_LASTNAME));

		grid.addFilterColumn(RegisteredUser::getMail)
				.setFilterType(String.class)
				.setCaption(i18n.get(UIMessageKeys.USER_EMAIL));

		grid.addFilterColumn(RegisteredUser::getOrganisation)
				.setFilterType(String.class)
				.setCaption(i18n.get(UIMessageKeys.USER_ORGANISATION));

	}

	@Override
	public Component getHeaderComponent() {
		CssLayout containerHeaderLayout = new CssLayout();

		Button addUserButton = componentFactory.createButton(VaadinIcons.PLUS,
				i18n.get(UIMessageKeys.USER_ADD_USER_BUTTON_CAPTION));
		addUserButton.addClickListener(e -> handleAddUser());
		containerHeaderLayout.addComponent(addUserButton);

		Button editUserButton = componentFactory.createButton(VaadinIcons.PENCIL,
				i18n.get(UIMessageKeys.USER_EDIT_USER_BUTTON_CAPTION));
		editUserButton.addClickListener(e -> handleEditUser());
		containerHeaderLayout.addComponent(editUserButton);

		Button changePasswordButton = componentFactory.createButton(VaadinIcons.KEY,
				i18n.get(UIMessageKeys.USER_CHANGE_PASSWORD_BUTTON_CAPTION));
		changePasswordButton.addClickListener(e -> handleChangePassword());
		containerHeaderLayout.addComponent(changePasswordButton);

		Button deleteUserButton = componentFactory.createButton(VaadinIcons.CLOSE,
				i18n.get(UIMessageKeys.USER_DELETE_USER_BUTTON_CAPTION));
		deleteUserButton.addClickListener(e -> handleDeleteUser());
		containerHeaderLayout.addComponent(deleteUserButton);

		Button refreshTransformationPathwayButton = componentFactory.createButton(VaadinIcons.CLUSTER,
				i18n.get(UIMessageKeys.ADMIN_REFRESH_TP_DATABASE_BUTTON));
		refreshTransformationPathwayButton.addClickListener(e -> refreshTPData());
		containerHeaderLayout.addComponent(refreshTransformationPathwayButton);

		Button uploadCompoundDataButton = componentFactory.createButton(VaadinIcons.DATABASE,
				i18n.get(UIMessageKeys.ADMIN_UPLOAD_COMPOUND_DATA_BUTTON_CAPTION));
		uploadCompoundDataButton.addClickListener(e -> uploadCompoundData());
		containerHeaderLayout.addComponent(uploadCompoundDataButton);

		return containerHeaderLayout;
	}

	@Override
	public String getTitle() {
		return "";
	}

	@Override
	public Label getTitleLabel() {
		return titleLabel;
	}

	private void updateGridData() {

		users = securityService.findAllUsers();

		grid.setListDataProvider(DataProvider.ofCollection(users));
		grid.getDataProvider().addDataProviderListener(listener -> updateUserCountLabel());

		updateUserCountLabel();
	}

	private void updateUserCountLabel() {
		titleLabel.setValue(users.size() + i18n.get(UIMessageKeys.USER_VISIBLE) +
				grid.getDataProvider().size(new Query<>()));
	}

	private void handleAddUser() {
		LOGGER.debug("publish event inside handleAddUser with topic {}",
				EventBusTopics.TARGET_HANDLER_USER_ADD);
		eventBus.publish(EventBusTopics.TARGET_HANDLER_USER_ADD, this, new RegisteredUser());

	}

	private void handleEditUser() {
		if (currentSelection != null) {
			LOGGER.debug("publish event inside handleEditUser with topic {}",
					EventBusTopics.TARGET_HANDLER_USER_EDIT);

			eventBus.publish(EventBusTopics.TARGET_HANDLER_USER_EDIT, this,
					currentSelection);
		}
	}

	private void handleChangePassword() {
		if (currentSelection != null) {
			LOGGER.debug("publish event inside changePassword with topic {}",
					EventBusTopics.TARGET_HANDLER_USER_PASSWORD);

			eventBus.publish(EventBusTopics.TARGET_HANDLER_USER_PASSWORD, this,
					currentSelection);
		}
	}

	private void handleDeleteUser() {
		if (currentSelection != null) {
			LOGGER.debug("publish event inside confirmationDialog with topic {}",
					EventBusTopics.TARGET_HANDLER_USER_DELETE);

			eventBus.publish(EventBusTopics.TARGET_HANDLER_USER_DELETE, this,
					currentSelection);
		}
	}

	private void refreshTPData() {
		LOGGER.debug("publish event inside refreshTPData with topic {}",
				EventBusTopics.TARGET_HANDLER_SHOW_REFRESH_TP_DATABASE_WINDOW);
		eventBus.publish(EventBusTopics.TARGET_HANDLER_SHOW_REFRESH_TP_DATABASE_WINDOW, this, DummyPayload.INSTANCE);
	}

	private void uploadCompoundData() {
		LOGGER.debug("publish event inside uploadCompoundData with topic {}",
				EventBusTopics.TARGET_HANDLER_SHOW_UPLOAD_COMPOUND_DATABASE_WINDOW);
		eventBus.publish(EventBusTopics.TARGET_HANDLER_SHOW_UPLOAD_COMPOUND_DATABASE_WINDOW, this, DummyPayload.INSTANCE);
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_USER_ADDED)
	private void handleAddUser(RegisteredUser user) {
		LOGGER.debug("entering event bus listener handleAddUser with payload {} in topic {}", user,
				EventBusTopics.SOURCE_HANDLER_USER_ADDED);
		grid.clearFilter();
		updateGridData();
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_USER_CHANGED)
	private void handleUserUpdate(RegisteredUser user) {
		LOGGER.debug("entering event bus listener handleUserUpdate with payload {} in topic {}", user,
				EventBusTopics.SOURCE_HANDLER_USER_CHANGED);
		grid.clearFilter();
		updateGridData();
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_USER_DELETE)
	private void handleUserDelete(RegisteredUser user) {
		LOGGER.debug("entering event bus listener handleUserDelete with payload {} in topic {}", user,
				EventBusTopics.SOURCE_HANDLER_USER_DELETE);
		grid.clearFilter();
		updateGridData();
	}
}
