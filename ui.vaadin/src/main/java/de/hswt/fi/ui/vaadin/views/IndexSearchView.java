package de.hswt.fi.ui.vaadin.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.views.components.IndexSearchFormComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

@SideBarItem(sectionId = Sections.SEARCH, captionCode = UIMessageKeys.INDEX_SEARCH_VIEW_CAPTION,
		order = 3)
@VaadinFontIcon(VaadinIcons.GRID_SMALL)
@SpringView(name = IndexSearchView.VIEW_NAME)
public class IndexSearchView extends AbstractSearchView {

	private static final long serialVersionUID = -5708147356914283848L;

	static final String VIEW_NAME = "indexsearch";

	private static final Logger LOG = LoggerFactory.getLogger(IndexSearchView.class);

	private I18N i18n;

	private IndexSearchFormComponent searchComponent;

	@Autowired
	public IndexSearchView(I18N i18n, IndexSearchFormComponent searchComponent) {
		this.i18n = i18n;
		this.searchComponent = searchComponent;
	}

	@Override
	protected void initComponents() {
		super.initComponents();

		initSearchComponent();

		LOG.info("leaving method initComponents");
	}

	private void initSearchComponent() {
		addSearchComponent(searchComponent);
	}

	@Override
	protected String getViewTitle() {
		return i18n.get(UIMessageKeys.INDEX_SEARCH_VIEW_VIEW_TITLE);
	}
}
