package de.hswt.fi.ui.vaadin.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.views.components.QuickSearchFormComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

@SideBarItem(sectionId = Sections.SEARCH, captionCode = UIMessageKeys.QUICK_SEARCH_VIEW_CAPTION,
		order = 1)
@VaadinFontIcon(VaadinIcons.SEARCH)
@SpringView(name = QuickSearchView.VIEW_NAME)
public class QuickSearchView extends AbstractSearchView {

	private static final long serialVersionUID = 8178381062935519987L;

	static final String VIEW_NAME = "quicksearch";

	private static final Logger LOG = LoggerFactory.getLogger(QuickSearchView.class);

	private QuickSearchFormComponent searchComponent;

	private I18N i18n;

	@Autowired
	public QuickSearchView(QuickSearchFormComponent searchComponent, I18N i18n) {
		this.searchComponent = searchComponent;
		this.i18n = i18n;
	}

	@Override
	protected void initComponents() {
		super.initComponents();

		initSearchHeaderComponent();

		LOG.info("leaving method initComponents");
	}

	private void initSearchHeaderComponent() {
		addSearchComponent(searchComponent);
	}

	@Override
	protected String getViewTitle() {
		return i18n.get(UIMessageKeys.QUICK_SEARCH_VIEW_VIEW_TITLE);
	}
}
