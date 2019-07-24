package de.hswt.fi.ui.vaadin.views.components.processing;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import de.hswt.fi.calculation.service.api.CalculationService;
import de.hswt.fi.processing.service.model.ProcessCandidate;
import de.hswt.fi.search.service.tp.model.Compound;
import de.hswt.fi.search.service.tp.model.Pathway;
import de.hswt.fi.search.service.tp.model.Transformation;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.vaadin.component.D3GraphComponent;
import de.hswt.vaadin.component.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SpringComponent
@PrototypeScope
public class TransformationProductsDetailsComponent extends CssLayout {

	private static final long serialVersionUID = 1L;

	private static final int IMAGE_WIDTH = 200;

	private static final int IMAGE_HEIGHT = 200;

	private final CalculationService calculationService;

	private D3GraphComponent<Object> graphComponent;

	@Autowired
	public TransformationProductsDetailsComponent(CalculationService calculationService) {
		this.calculationService = calculationService;

	}

	public void init(ProcessCandidate candidate) {

		CssLayout layout = new CssLayout();
		layout.setSizeFull();
		layout.addStyleName(CustomValoTheme.CSS_LAYOUT_FLEX_COLUMN);

		graphComponent = new D3GraphComponent<>();
		graphComponent.setDuration(50);
		graphComponent.setLinkDistance(150);
		graphComponent.setCharge(-3000);
		graphComponent.setSkipFrames(1000);
		graphComponent.addStyleName(CustomValoTheme.BORDER_NONE);
		graphComponent.addStyleName(CustomValoTheme.FLEX_ITEM_EXPAND);

		layout.addComponent(graphComponent);

		Label licenseLabel = new Label();
		licenseLabel.setWidth("100%");
		licenseLabel.setContentMode(ContentMode.HTML);
		licenseLabel.setValue("<p>Transformation pathway's data (" +
				"<a target=\"_blank\" href=\"https://envipath.org/package/32de3cf4-e3e6-4168-956e-32fa5ddb0ce1\">" +
				"EAWAG-BBD Package" +
				"</a>" +
				") originates from " +
				"<a target=\"_blank\" href=\"https://envipath.org/\">" +
				"enviPath UG (haftungsbeschränkt) & Co. KG (c) 2018" +
				"</a>" +
				", licensed under " +
				"<a target=\"_blank\" href=\"https://creativecommons.org/licenses/by-nc-sa/4.0/\">" +
				"CC BY-NC-SA 4.0" +
				"</a>" +
				".</p>");
		layout.addComponent(licenseLabel);

		addComponent(layout);

		initGraph(candidate);
	}

	private void initGraph(ProcessCandidate candidate) {
		for (Pathway pathway : candidate.getPathwayCandidate().getPathways().values()) {
			constructPathway(pathway, candidate.getEntry().getInchiKey().getValue());
		}
	}

	private void constructPathway(Pathway pathway, String inChiKey) {
		Map<String, Node<Object>> compoundsMap = new HashMap<>();

		for (Compound compound : pathway.getCompounds()) {
			TpDisplayData displayData = new TpDisplayData(compound.getNeutralMass(),
					compound.getFormula());
			Node<Object> node = graphComponent.addNode(compound.getName(), displayData);
			compoundsMap.put(compound.getId(), node);

			graphComponent.addNodeResource(node, getImage(compound.getSmiles()));
			if (compound.getInChiKey().equals(inChiKey)) {
				node.setClazz("node-candidate");
			} else if (!compound.getMatchingFeatures().isEmpty()) {
				node.setClazz("node-feature");
			} else if (compound.getRoot()) {
				node.setClazz("node-root");
			}
		}

		for (Transformation transformation : pathway.getTransformations()) {
			graphComponent.addLink(compoundsMap.get(transformation.getCompoundId()),
					compoundsMap.get(transformation.getTransformationProductId()));
		}

		graphComponent.update();
	}

	private Resource getImage(String smiles) {
		Resource imageResource = null;
		byte[] createdImage = calculationService.getSmilesAsImage(smiles, IMAGE_WIDTH,
				IMAGE_HEIGHT);
		if (createdImage != null) {
			imageResource = new StreamResource(new RenderedImageSource(createdImage),
					System.currentTimeMillis() + "");
		}

		return imageResource;
	}

	public class RenderedImageSource implements StreamSource {

		private static final long serialVersionUID = 1L;

		private byte[] imageData;

		RenderedImageSource(byte[] imageData) {
			this.imageData = imageData;
		}

		@Override
		public InputStream getStream() {
			return new ByteArrayInputStream(imageData);
		}
	}

	private class TpDisplayData {

		private double mass;

		private String formula;

		TpDisplayData(double mass, String formula) {
			this.mass = mass;

			this.formula = formula;
		}

		public double getMass() {
			return mass;
		}

		public String getFormula() {
			return formula;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		TransformationProductsDetailsComponent that = (TransformationProductsDetailsComponent) o;
		return Objects.equals(calculationService, that.calculationService) &&
				Objects.equals(graphComponent, that.graphComponent);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), calculationService, graphComponent);
	}
}
