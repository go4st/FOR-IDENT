package de.hswt.fi.ui.vaadin.fields;

import com.vaadin.data.Binder;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import de.hswt.fi.processing.service.model.ProcessingUnitState;
import de.hswt.fi.ui.vaadin.CustomValoTheme;

import java.util.function.Consumer;

public class ProcessingUnitStateField extends CustomField<ProcessingUnitState> {

	private static final long serialVersionUID = 1L;
	private String unitCaption;
	private ScoreValueLabel scoreLabel;
	private BooleanValueLabel enabledLabel;
	private BooleanValueLabel dataAvailableLabel;
	private boolean canBeDisabled;
	private Consumer<LayoutClickEvent> enabledClickListener;
	private ProcessingUnitState value;
	private Binder<ProcessingUnitState> binder;
	private CssLayout enabledLayout;

	public ProcessingUnitStateField(String unitCaption) {
		this.unitCaption = unitCaption;
		canBeDisabled = true;
	}

	@Override
	protected Component initContent() {
		CssLayout layout = new CssLayout();
		layout.setWidth("100%");

		Label captionLabel = new Label(unitCaption);
		captionLabel.setWidth("65%");
		layout.addComponent(captionLabel);

		CssLayout rightLayout = new CssLayout();
		rightLayout.setWidth("35%");
		layout.addComponent(rightLayout);

		CssLayout scoreLabelLayout = new CssLayout();
		scoreLabelLayout.setWidth("40%");
		scoreLabel = new ScoreValueLabel();
		scoreLabel.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		scoreLabelLayout.addComponent(scoreLabel);
		rightLayout.addComponent(scoreLabelLayout);

		enabledLayout = new CssLayout();
		enabledLayout.setWidth("30%");
		enabledLabel = new BooleanValueLabel(VaadinIcons.CIRCLE.getHtml(), VaadinIcons.CIRCLE_THIN.getHtml());
		enabledLabel.setContentMode(ContentMode.HTML);
		enabledLabel.setStyleNameTrue(CustomValoTheme.ICON_COLOR_GREEN);
		enabledLabel.setStyleNameFalse(CustomValoTheme.ICON_COLOR_RED);
		enabledLabel.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		enabledLayout.addComponent(enabledLabel);
		rightLayout.addComponent(enabledLayout);

		CssLayout dataAvailableLayout = new CssLayout();
		dataAvailableLayout.setWidth("30%");
		dataAvailableLabel = new BooleanValueLabel(VaadinIcons.CHECK.getHtml(), VaadinIcons.BAN.getHtml());
		dataAvailableLabel.setContentMode(ContentMode.HTML);
		dataAvailableLabel.setStyleNameTrue(CustomValoTheme.ICON_COLOR_GREEN);
		dataAvailableLabel.setStyleNameFalse(CustomValoTheme.ICON_COLOR_RED);
		dataAvailableLabel.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		dataAvailableLayout.addComponent(dataAvailableLabel);
		rightLayout.addComponent(dataAvailableLayout);

		bindFields();

		return layout;
	}

	private void bindFields() {
		binder = new Binder<>();
		binder.bind(scoreLabel, ProcessingUnitState::getScoreWeight, ProcessingUnitState::setScoreWeight);
		binder.bind(enabledLabel, ProcessingUnitState::isEnabled, ProcessingUnitState::setEnabled);
		binder.bind(dataAvailableLabel, ProcessingUnitState::isDataAvailable, ProcessingUnitState::setDataAvailable);
	}

	public void setEnabledClickListener(Consumer<LayoutClickEvent> enabledClickListener) {
		if (this.enabledClickListener == null) {
			this.enabledClickListener = enabledClickListener;
			enabledLayout.addLayoutClickListener(layoutClickEvent -> {
				if (canBeDisabled && value.isDataAvailable()) {
					enabledLabel.setValue(!enabledLabel.getValue());
					enabledClickListener.accept(layoutClickEvent);
				}
			});
		}
	}

	public void setScoreWeight(double score) {
		scoreLabel.setValue(score);
	}

	@Override
	protected void doSetValue(ProcessingUnitState value) {
		this.value = value;
		binder.setBean(value);
	}

	@Override
	public ProcessingUnitState getValue() {
		return value;
	}

	public boolean isCanBeDisabled() {
		return canBeDisabled;
	}

	public void canBeDisabled(boolean canBeDisabled) {
		this.canBeDisabled = canBeDisabled;
	}

	private class ScoreValueLabel extends CustomField<Double> {

		private Label label;

		private Double value;

		public ScoreValueLabel() {
			label = new Label();
		}

		public void setContentMode(ContentMode contentMode) {
			label.setContentMode(contentMode);
		}

		@Override
		protected Component initContent() {
			return label;
		}

		@Override
		protected void doSetValue(Double value) {
			this.value = value;
			label.setValue((int) (value * 100) + "%");
		}

		@Override
		public Double getValue() {
			return value;
		}
	}

	private class BooleanValueLabel extends CustomField<Boolean> {

		private Label label;

		private Boolean value;

		private final String presentationTrue;

		private final String presentationFalse;

		private String styleNameTrue;

		private String styleNameFalse;

		public BooleanValueLabel(String presentationTrue, String presentationFalse) {
			label = new Label();
			this.presentationTrue = presentationTrue;
			this.presentationFalse = presentationFalse;
		}

		public void setContentMode(ContentMode contentMode) {
			label.setContentMode(contentMode);
		}

		public void setStyleNameTrue(String styleName) {
			styleNameTrue = styleName;
		}

		public void setStyleNameFalse(String styleName) {
			styleNameFalse = styleName;
		}

		@Override
		protected Component initContent() {
			return label;
		}

		@Override
		protected void doSetValue(Boolean value) {
			this.value = value;
			label.setValue(value ? presentationTrue : presentationFalse);
			label.setStyleName(value ? styleNameTrue : styleNameFalse);
		}

		@Override
		public Boolean getValue() {
			return value;
		}
	}
}
