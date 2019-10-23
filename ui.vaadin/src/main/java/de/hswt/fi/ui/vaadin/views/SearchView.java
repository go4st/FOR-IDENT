package de.hswt.fi.ui.vaadin.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.views.components.SearchFormComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import java.util.Objects;

@SideBarItem(sectionId = Sections.SEARCH, captionCode = UIMessageKeys.SEARCH_VIEW_CAPTION,
		order = 2)
@VaadinFontIcon(VaadinIcons.SEARCH_PLUS)
@SpringView(name = SearchView.VIEW_NAME)
public class SearchView extends AbstractSearchView {

	private static final long serialVersionUID = 2707899413359225304L;
	public static final String VIEW_NAME = "search";
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchView.class);
	private SearchFormComponent searchFormComponent;
	private I18N i18n;

	@Autowired
	public SearchView(SearchFormComponent searchFormComponent, I18N i18n) {
		this.searchFormComponent = searchFormComponent;
		this.i18n = i18n;
	}

	@Override
	protected void initComponents() {
		super.initComponents();

		initSearchHeaderComponent();

		LOGGER.info("leaving method initComponents");
	}

	private void initSearchHeaderComponent() {
		addSearchComponent(searchFormComponent);
	}

	@Override
	protected String getViewTitle() {
		return i18n.get(UIMessageKeys.SEARCH_VIEW_VIEW_TITLE);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		SearchView that = (SearchView) o;
		return Objects.equals(searchFormComponent, that.searchFormComponent) &&
				Objects.equals(i18n, that.i18n);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), searchFormComponent, i18n);
	}
}
