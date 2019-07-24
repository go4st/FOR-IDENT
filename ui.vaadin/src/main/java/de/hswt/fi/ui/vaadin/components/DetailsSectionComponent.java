package de.hswt.fi.ui.vaadin.components;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import org.vaadin.spring.annotation.PrototypeScope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
@PrototypeScope
public class DetailsSectionComponent extends CssLayout {

	private static final long serialVersionUID = 1L;

	private Label headerLabel;

	private CssLayout dataLayout;

	private List<CssLayout> metaDataComponents = new ArrayList<>();

	private boolean metaDataVisible = true;

	@PostConstruct
	private void postConstruct() {
		headerLabel = new Label();
		headerLabel.setWidth("100%");
		headerLabel.addStyleName(CustomValoTheme.BORDER_BOTTOM_COLOR_ALT3);
		headerLabel.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
		headerLabel.addStyleName(CustomValoTheme.PADDING_HALF_BOTTOM);
		addComponent(headerLabel);

		dataLayout = new CssLayout();
		dataLayout.setWidth("100%");
		addComponent(dataLayout);
	}

	public void setHeaderCaption(String caption) {
		headerLabel.setValue(caption);
	}

	public void clearItems() {
		dataLayout.removeAllComponents();
		metaDataComponents.clear();
	}

	public void addEntry(DetailsProperty property) {
		if (property == null) {
			return;
		}

		if (property.getComponent().isPresent()) {
			addEntry(property.getComponent().get());
			return;
		}

		if (!property.getValue().isPresent() || !property.getValueCaption().isPresent()) {
			return;
		}

		CssLayout contentLayout = new CssLayout();
		contentLayout.setWidth("100%");
		contentLayout.addStyleName(CustomValoTheme.PADDING_HALF_VERTICAL);
		contentLayout.addStyleName(CustomValoTheme.BORDER_BOTTOM_COLOR_ALT1);
		contentLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_TABLE_STYLE);

		CssLayout rowLayout = createRowLayout();
		contentLayout.addComponent(rowLayout);
		rowLayout.addComponent(createHighlightCellLabel(property.getValueCaption().get(), true));
		rowLayout.addComponent(createCellLabel(property.getValue().get()));

		if (property.getPhCaption().isPresent() && property.getPh().isPresent()) {
			addPropertyRow(contentLayout, property.getPhCaption().get(), property.getPh().get(),
					false);
		}

		if (property.getSourceCaption().isPresent() && property.getSource().isPresent()) {
			metaDataComponents.add(addPropertyRow(contentLayout, property.getSourceCaption().get(),
					property.getSource().get(), false));
		}

		if (property.getAdditionalCaption().isPresent() && property.getAdditional().isPresent()) {
			metaDataComponents.add(addPropertyRow(contentLayout,
					property.getAdditionalCaption().get(), property.getAdditional().get(), false));
		}

		if (property.getLastModifiedCaption().isPresent() && property.getLastModified().isPresent()) {
			metaDataComponents
					.add(addPropertyRow(contentLayout, property.getLastModifiedCaption().get(),
							property.getLastModified().get(), false));
		}

		setMetaDataVisible(metaDataVisible);

		dataLayout.addComponent(contentLayout);
	}

	private CssLayout addPropertyRow(CssLayout contentLayout, String caption, String value, boolean important) {
		CssLayout rowLayout;
		rowLayout = createRowLayout();
		contentLayout.addComponent(rowLayout);
		rowLayout.addComponent(createHighlightCellLabel(caption, important));
		rowLayout.addComponent(createCellLabel(value));
		return rowLayout;
	}

	private void addEntry(Component component) {
		if (component == null) {
			return;
		}

		CssLayout contentLayout = new CssLayout();
		contentLayout.setWidth("100%");
		contentLayout.addStyleName(CustomValoTheme.PADDING_HALF_VERTICAL);
		contentLayout.addStyleName(CustomValoTheme.BORDER_BOTTOM_COLOR_ALT1);
		contentLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_TABLE_STYLE);

		contentLayout.addComponent(component);

		dataLayout.addComponent(contentLayout);
	}

	public void setMetaDataVisible(boolean visible) {
		metaDataVisible = visible;
		metaDataComponents.forEach(layout -> layout.setVisible(visible));
	}

	private CssLayout createRowLayout() {
		CssLayout rowLayout = new CssLayout();
		rowLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_TABLE_ROW);
		rowLayout.setWidth("100%");
		return rowLayout;
	}

	private Label createHighlightCellLabel(String value, boolean important) {
		Label label = new Label(value);
		if (important) {
			label.addStyleName(CustomValoTheme.COLOR_ALT3);
		}
		label.addStyleName(ValoTheme.LABEL_BOLD);
		label.setWidth("30%");
		return label;
	}

	private Label createCellLabel(String value) {
		Label label = new Label(value);
		label.addStyleName(CustomValoTheme.WORDWRAP);
		label.addStyleName(CustomValoTheme.LABEL_ALIGN_RIGHT);
		label.setWidth("70%");
		return label;
	}
}
