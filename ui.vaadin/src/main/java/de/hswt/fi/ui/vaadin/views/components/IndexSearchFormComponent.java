package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.search.service.mass.search.model.SearchParameter;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.ContainerContentComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
@ViewScope
public class IndexSearchFormComponent extends ContainerContentComponent {

	private static final long serialVersionUID = -8418444280847408703L;

	private static final Logger LOG = LoggerFactory.getLogger(IndexSearchFormComponent.class);

	// 26 Characters from A-Z
	private static final int NUMBER_OF_CHARACTERS = 26;

	private static final int NUMBER_OF_NUMERICALS = 9;

	private static final int NUMBER_OF_CHAR_COLUMNS = 5;

	private static final int NUMBER_OF_NUMERIC_COLUMNS = 3;

	private final ViewEventBus eventBus;

	private final I18N i18n;

	private CssLayout searchLayout;

	@Autowired
	public IndexSearchFormComponent(ViewEventBus eventBus, I18N i18n) {
		this.eventBus = eventBus;
		this.i18n = i18n;
	}

	@PostConstruct
	private void postConstruct() {
		setSizeFull();

		addStyleName(CustomValoTheme.PADDING);
		addStyleName(CustomValoTheme.CSS_LAYOUT_SCROLLBAR);

		searchLayout = new CssLayout();
		searchLayout.setSizeFull();
		addComponent(searchLayout);

		createCharacterButtons();
		createNumericalButtons();
		
	}

	private void createCharacterButtons() {
		CssLayout characterLayout = new CssLayout();
		characterLayout.addStyleName(CustomValoTheme.MARGIN_BOTTOM);
		characterLayout.setWidth("100%");
		searchLayout.addComponent(characterLayout);

		List<Button> row = new ArrayList<>();

		int characterAscii = 65;

		for (int i = 1; i <= NUMBER_OF_CHARACTERS; i++) {

			final Button button = new Button(Character.toString((char) characterAscii));
			button.addClickListener(c -> handleButtonClick(button.getCaption().toLowerCase().charAt(0)));
			row.add(button);
			if (row.size() == NUMBER_OF_CHAR_COLUMNS) {
				CssLayout rowLayout = createRowOfButtons(row);
				rowLayout.addStyleName(CustomValoTheme.MARGIN_BOTTOM);
				characterLayout.addComponent(rowLayout);
				row.clear();
			}
			characterAscii++;
		}

		if (!row.isEmpty()) {
			characterLayout.addComponent(createRowOfButtons(row));
		}

	}

	private void createNumericalButtons() {
		CssLayout numbersLayout = new CssLayout();
		numbersLayout.addStyleName(CustomValoTheme.MARGIN_TOP);
		numbersLayout.setWidth("100%");
		searchLayout.addComponent(numbersLayout);

		// 1 = 49, 9 = 57
		int asciiValue = 49;

		List<Button> row = new ArrayList<>();

		for (int i = 1; i <= NUMBER_OF_NUMERICALS; i++) {
			final Button button = new Button(Character.toString((char) asciiValue));
			button.addClickListener(c -> handleButtonClick(button.getCaption().toLowerCase().charAt(0)));
			row.add(button);
			if (row.size() == NUMBER_OF_NUMERIC_COLUMNS) {
				CssLayout rowLayout = createRowOfButtons(row);
				if (i <= NUMBER_OF_NUMERICALS - NUMBER_OF_NUMERIC_COLUMNS) {
					rowLayout.addStyleName(CustomValoTheme.MARGIN_BOTTOM);
				}
				numbersLayout.addComponent(rowLayout);
				row.clear();
			}
			asciiValue++;
		}
	}

	private CssLayout createRowOfButtons(List<Button> buttons) {
		CssLayout rowLayout = new CssLayout();
		rowLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_ROW);

		for (int i = 0; i < buttons.size(); i++) {
			Button button = buttons.get(i);
			if (i < buttons.size() - 1) {
				button.addStyleName(CustomValoTheme.MARGIN_RIGHT);
			}
			button.addStyleName(CustomValoTheme.FLEX_ITEM_EXPAND);
			button.addStyleName(ValoTheme.BUTTON_LARGE);
			button.addStyleName(CustomValoTheme.BACKGROUND_COLOR_NONE);
			button.addStyleName(CustomValoTheme.COLOR_ALT3);
			rowLayout.addComponent(button);
		}

		return rowLayout;
	}

	private void handleButtonClick(char character) {
		LOG.debug("publish event inside handleButtonClick");
		eventBus.publish(this, new SearchParameter(character));
	}

	@Override
	public String getTitle() {
		return i18n.get(UIMessageKeys.INDEX_SEARCH_VIEW_SEARCH_TITLE);
	}

	@Override
	public Component getHeaderComponent() {
		return null;
	}
}
