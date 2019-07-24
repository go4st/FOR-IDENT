package de.hswt.fi.ui.vaadin.views.components.processing;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet;
import de.hswt.fi.processing.service.model.ProcessCandidate;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;

@SpringComponent
@PrototypeScope
public class ProcessingResultDetailsComponent extends CssLayout {

	private static final long serialVersionUID = 1L;

	private static final String HEIGHT_DETAILS = "570px";

	private static final String HEIGHT_TABS = "530px";

	@Autowired
	private ScoringDetailsComponent scoringDetailsComponent;

	@Autowired
	private MsMsDetailsComponent msmsDetailsComponent;

	@Autowired
	private TransformationProductsDetailsComponent tpDetailsComponent;

	public void init(ProcessCandidate candidate) {

		setWidth("100%");

		addStyleName(CustomValoTheme.BACKGROUND_COLOR_WHITE);
		addStyleName(CustomValoTheme.PADDING_HORIZONTAL);

		TabSheet tabSheet = new TabSheet();
		tabSheet.addStyleName(CustomValoTheme.PADDING_HALF_VERTICAL);
		tabSheet.setHeight(HEIGHT_DETAILS);
		tabSheet.setWidth("100%");
		addComponent(tabSheet);

		scoringDetailsComponent.init(candidate);
		scoringDetailsComponent.setWidth("100%");
		scoringDetailsComponent.setHeight(HEIGHT_TABS);
		tabSheet.addTab(scoringDetailsComponent, "Scoring", VaadinIcons.PLUS);

		if (candidate.getMsMsCandidate() != null && candidate.getMsMsCandidate().getFragments() != null) {
			msmsDetailsComponent.init(candidate);
			msmsDetailsComponent.setWidth("100%");
			msmsDetailsComponent.setHeight(HEIGHT_TABS);
			tabSheet.addTab(msmsDetailsComponent, "MS/MS", VaadinIcons.BAR_CHART_H);
		}

		if (candidate.getPathwayCandidate() != null) {
			tpDetailsComponent.init(candidate);
			tpDetailsComponent.setWidth("100%");
			tpDetailsComponent.setHeight(HEIGHT_TABS);
			tabSheet.addTab(tpDetailsComponent, "Transformation Products", VaadinIcons.SITEMAP);
		}
	}
}
