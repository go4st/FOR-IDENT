package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.ContainerContentComponent;
import de.hswt.fi.ui.vaadin.container.ResultContainer;
import de.hswt.fi.ui.vaadin.container.SearchHistoryTreeItem;
import de.hswt.fi.ui.vaadin.container.SearchHistoryTreeRootItem;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.Optional;

public abstract class AbstractSearchHistoryComponent<RESULTCONTAINER extends ResultContainer> extends ContainerContentComponent {

	private static final long serialVersionUID = -6501562658472985708L;

	private static final Logger LOG = LoggerFactory.getLogger(AbstractSearchHistoryComponent.class);

	protected final ViewEventBus eventBus;

	protected final I18N i18n;

	private final ComponentFactory componentFactory;

	private RESULTCONTAINER currentContainer;

	private CssLayout headerLayout;

	protected Tree<SearchHistoryTreeItem> tree;

	private TreeData<SearchHistoryTreeItem> treeData;

	private TreeDataProvider<SearchHistoryTreeItem> dataProvider;

	private SearchHistoryTreeRootItem<RESULTCONTAINER> currentSearchItem;

	private SearchHistoryTreeItem<Entry> selectedResultItem;

	protected abstract SearchHistoryTreeRootItem<RESULTCONTAINER> createRootItem(RESULTCONTAINER container);

	protected abstract String getDisplayedName(Entry entry);

	// Subclasses must implement this method AND listen to their specific clear
	// event (Search, File, RTI) and call clearSearch() function.
	protected abstract void handleClearHistory(DummyPayload payload);

	@Autowired
	public AbstractSearchHistoryComponent(ViewEventBus eventBus, I18N i18n, ComponentFactory componentFactory) {
		this.eventBus = eventBus;
		this.i18n = i18n;
		this.componentFactory = componentFactory;
	}

	@PostConstruct
	private void postConstruct() {
		setSizeFull();

		initHeader();
		initComponents();

		eventBus.subscribe(this);
	}

	@PreDestroy
	private void preDestroy() {
		eventBus.unsubscribe(this);
	}

	private void initHeader() {
		headerLayout = new CssLayout();

		Button clearButton = componentFactory.createButton(VaadinIcons.ERASER,
				i18n.get(UIMessageKeys.CLEAR_SEARCH_BUTTON_CAPTION));
		clearButton.addClickListener((Button.ClickEvent event) -> sendClearEvent());
		headerLayout.addComponent(clearButton);

		Button reportButton = componentFactory.createButton(VaadinIcons.BUG,
				i18n.get(UIMessageKeys.REPORT_SEARCH_BUTTON_CAPTION));
		reportButton.addClickListener((Button.ClickEvent event) -> handleReportSearch());
		headerLayout.addComponent(reportButton);
	}

	private void initComponents() {

		CssLayout layout = new CssLayout();
		layout.setSizeFull();
		layout.addStyleName(CustomValoTheme.SCROLL_OVERFLOW);

		tree = new Tree<>();
		tree.setSizeFull();
		tree.addStyleName(CustomValoTheme.SEARCH_HISTORY_TREE);
		tree.setSelectionMode(Grid.SelectionMode.NONE);
		tree.addItemClickListener(event -> handleItemClick(event.getItem()));
		tree.setStyleGenerator(item -> {

			String cssClasses = "";

			if (item.equals(currentSearchItem) || item.equals(selectedResultItem)) {
				cssClasses += CustomValoTheme.HISTORY_TREE_ITEM_SLECTED + " ";
			}

			if (isRootElement(item) || isResultItem(item)) {
				cssClasses += CustomValoTheme.HISTORY_TREE_ITEM_CLICKABLE;
			}

			return cssClasses;
		});

		ContextMenu contextMenu = new ContextMenu(tree, true);
		contextMenu.addContextMenuOpenListener(listener -> {
			Tree.TreeContextClickEvent<SearchHistoryTreeItem> contextClickEvent =
					(Tree.TreeContextClickEvent<SearchHistoryTreeItem>) listener.getContextClickEvent();

			SearchHistoryTreeItem rootItem = getRootItem(contextClickEvent.getItem());
			contextMenu.removeItems();

			if (rootItem != null) {
				contextMenu.addItem(i18n.get(UIMessageKeys.SEARCH_HISTORY_CONTEXT_MENU_REMOVE)
								+ rootItem.getCaption(),
						command -> removeSearchItem(rootItem));
			}
		});


		treeData = new TreeData<>();
		dataProvider = new TreeDataProvider<>(treeData);
		tree.setDataProvider(dataProvider);

		layout.addComponent(tree);
		addComponent(layout);
	}

	private SearchHistoryTreeItem getRootItem(SearchHistoryTreeItem item) {

		SearchHistoryTreeItem rootItem = item;

		while (treeData.getParent(rootItem) != null) {
			rootItem = treeData.getParent(rootItem);
		}

		return rootItem;
	}

	private void removeSearchItem(SearchHistoryTreeItem rootItem) {
		currentSearchItem = null;
		treeData.removeItem(rootItem);
		dataProvider.refreshAll();
	}

	private void handleItemClick(SearchHistoryTreeItem item) {

		if (isRootElement(item)) {
			RESULTCONTAINER container = ((SearchHistoryTreeRootItem<RESULTCONTAINER>) item).getContent();
			if (!currentContainer.equals(container)) {
				selectionChanged(container);
			}
		}

		if (item.getContent() != null && Entry.class.isAssignableFrom(item.getContent().getClass())) {

			SearchHistoryTreeItem searchItem = tree.getTreeData().getParent(tree.getTreeData().getParent(item));

			if (!searchItem.equals(currentSearchItem)) {
				selectionChanged((RESULTCONTAINER) searchItem.getContent());
			}

			LOG.debug("publish event inside handleItemClick with payload ({}) in topic {}",
					item.getContent(),
					EventBusTopics.TARGET_HANDLER_SELECT_ENTRY);
			eventBus.publish(EventBusTopics.TARGET_HANDLER_SELECT_ENTRY, this, item.getContent());
		}
	}

	private boolean isRootElement(SearchHistoryTreeItem item) {
		return SearchHistoryTreeRootItem.class.isAssignableFrom(item.getClass());
	}

	private boolean isResultItem(SearchHistoryTreeItem item) {

		if (item.getContent() == null) {
			return false;
		}

		return Entry.class.isAssignableFrom(item.getContent().getClass());
	}

	void handleClear() {

		currentSearchItem = null;
		currentContainer = null;

		dataProvider.getTreeData().clear();
		dataProvider.refreshAll();
	}

	private void sendClearEvent() {
		LOG.debug("publish event inside handleClear with topic {}",
				EventBusTopics.TARGET_HANDLER_CLEAR_HISTORY);
		eventBus.publish(EventBusTopics.TARGET_HANDLER_CLEAR_HISTORY, this,
				DummyPayload.INSTANCE);
	}

	private void handleReportSearch() {
		LOG.debug("publish event inside handleReportSearch with topic {}",
				EventBusTopics.TARGET_HANDLER_REPORT_TO_STAFF);
		if (currentContainer != null) {
			eventBus.publish(EventBusTopics.TARGET_HANDLER_REPORT_TO_STAFF, this,
					currentContainer.getSearchParameter());
		}
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_CONTAINER_CHANGED)
	private void handleSearchResultSelection(RESULTCONTAINER searchContainer) {
		LOG.debug(
				"entering event bus listener handleSearchResultSelection with payload ({}) in topic {}",
				searchContainer, EventBusTopics.SOURCE_HANDLER_CONTAINER_CHANGED);

		if (searchContainer.getResultsContainer().isEmpty() ||
				currentContainer != null && currentContainer.equals(searchContainer)) {
			return;
		}

		currentContainer = searchContainer;

		Optional<SearchHistoryTreeItem> item = treeData.getRootItems().stream()
				.filter(rootItem -> rootItem.getContent().equals(searchContainer))
				.findFirst();

		if (item.isPresent() && isRootElement(item.get())) {
			currentSearchItem = (SearchHistoryTreeRootItem) item.get();
		} else {
			currentSearchItem = createRootItem(searchContainer);
			treeData.addItems(Collections.singleton(currentSearchItem), SearchHistoryTreeItem::getChildren);
		}

		dataProvider.refreshAll();
	}

	// Selection changed in ResultView
	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_ENTRY_SELECTED)
	private void handleSelectionChanged(Entry entry) {
		LOG.debug("entering event bus listener handleSelectionChanged width payload {} in topic {}",
				entry, EventBusTopics.SOURCE_HANDLER_ENTRY_SELECTED);

		if (currentSearchItem == null) {
			return;
		}

		Optional<SearchHistoryTreeItem> resultItem = treeData.getChildren(currentSearchItem.getSelectedResultParentItem())
				.stream()
				.filter(item -> item.getContent().equals(entry))
				.findFirst();

		if (resultItem.isPresent()) {
			selectedResultItem = resultItem.get();
		} else {
			selectedResultItem = new SearchHistoryTreeItem<>(getDisplayedName(entry), entry);
			treeData.addItems(currentSearchItem.getSelectedResultParentItem(), selectedResultItem);
		}

		dataProvider.refreshAll();
	}

	private void selectionChanged(RESULTCONTAINER container) {
		LOG.debug("publish event inside selectionChanged with topic {}",
				EventBusTopics.TARGET_HANDLER_SELECT_CONTAINER);
		eventBus.publish(EventBusTopics.TARGET_HANDLER_SELECT_CONTAINER, this, container);
	}

	@Override
	public Component getHeaderComponent() {
		return headerLayout;
	}

	// Do not override hash() and equals() in abstract component classes because of identity issues when attach / remove
}
