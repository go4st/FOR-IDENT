package de.hswt.fi.ui.vaadin.windows;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.model.ResultSummary;
import de.hswt.fi.processing.service.model.ProcessResultSummary;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.i18n.I18N;

import java.util.Map;

@SpringComponent
@PrototypeScope
public class SummaryWindow extends AbstractWindow {

	private static final long serialVersionUID = 1L;

	private CssLayout contentLayout;

	@Autowired
	protected SummaryWindow(ComponentFactory componentFactory, I18N i18n) {
		super(componentFactory, i18n,false);
		setWidth(LayoutConstants.LARGE);
	}

	@Override
	protected String getWindowCaption() {
		return i18n.get(UIMessageKeys.SUMMARY_WINDOW_CAPTION);
	}

	@Override
	protected Component getContentComponent() {

		initContentLayout();

		setCanFinish(true);
		setCancelButtonVisible(false);

		return contentLayout;

	}

	private void initContentLayout() {
		contentLayout = new CssLayout();
		contentLayout.setWidth("100%");
	}

	public void setResultSummary(ProcessResultSummary resultSummary) {
		contentLayout.removeAllComponents();

		if (resultSummary == null) {
			return;
		}

		for (ResultSummary summary : resultSummary.getResultSummaries()) {
			contentLayout.addComponent(new ResultSummaryTile(summary));
		}
	}

	@Override
	protected void handleOk() {
		// Nothing to do here
	}

	private class ResultSummaryTile extends CssLayout {

		private static final long serialVersionUID = 1L;

		ResultSummaryTile(ResultSummary resultSummary) {
			setWidth("100%");

			Label headerLabel = new Label();
			headerLabel.addStyleName(CustomValoTheme.COLOR_ALT3);
			headerLabel.addStyleName(ValoTheme.LABEL_LARGE);
			headerLabel.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
			headerLabel.addStyleName(CustomValoTheme.BORDER_BOTTOM_COLOR_ALT3);
			headerLabel.addStyleName("border-thick");
			addComponent(headerLabel);
			CssLayout leftLayout = new CssLayout();
			CssLayout rightLayout = new CssLayout();

			Map<String, Object> content = resultSummary.getI18nCaptionValueMap();
			for (String key : content.keySet()) {
				leftLayout.addComponent(createCaptionLabel(i18n.get(key)));
				rightLayout.addComponent(createContentLabel(content.get(key).toString()));
			}
			addComponent(componentFactory.createRowLayout(leftLayout, rightLayout));
		}

		private Label createContentLabel(String content) {
			Label label = createCaptionLabel(content);
			label.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
			return label;
		}

		private Label createCaptionLabel(String content) {
			Label label = new Label(content);
			label.setWidthUndefined();
			label.addStyleName(CustomValoTheme.BLOCK);
			label.addStyleName(CustomValoTheme.MARGIN_HALF_BOTTOM);
			return label;
		}
	}
}
