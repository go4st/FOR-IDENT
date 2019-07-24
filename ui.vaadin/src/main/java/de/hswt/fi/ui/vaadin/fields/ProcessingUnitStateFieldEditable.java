package de.hswt.fi.ui.vaadin.fields;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import de.hswt.fi.processing.service.model.ProcessingUnitState;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus.ViewEventBus;

import javax.annotation.PostConstruct;

@SpringComponent
@PrototypeScope
public class ProcessingUnitStateFieldEditable extends CustomField<ProcessingUnitState> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingUnitStateFieldEditable.class);
	
	@Autowired
	private ViewEventBus viewEventBus;
	
	private CheckBox enabledBox;
	
	private Label dataAvailableLabel;

	private Slider slider;

	private String unitCaption;

	private Label captionLabel;

	private Label valueLabel;

	private boolean canBeUnchecked;

	private boolean scorable;

	private ProcessingUnitState value;

	@PostConstruct
	private void postConstruct() {
		scorable = true;
		canBeUnchecked = true;
	}

	@Override
	protected Component initContent() {

		CssLayout layout = new CssLayout();
		layout.setWidth("100%");

		captionLabel = new Label(unitCaption);
		captionLabel.setWidth("30%");
		layout.addComponent(captionLabel);

		CssLayout rightLayout = new CssLayout();
		rightLayout.setWidth("70%");
		layout.addComponent(rightLayout);

		CssLayout availableLayout = new CssLayout();
		availableLayout.setWidth("30%");
		dataAvailableLabel = new Label("", ContentMode.HTML);
		dataAvailableLabel.setValue(VaadinIcons.BAN.getHtml());
		dataAvailableLabel.addStyleName(CustomValoTheme.FLOAT_CENTER);
		availableLayout.addComponent(dataAvailableLabel);
		rightLayout.addComponent(availableLayout);
		
		CssLayout enabledLayout = new CssLayout();
		enabledLayout.setWidth("30%");
		enabledBox = new CheckBox();
		enabledBox.addValueChangeListener(l -> {
			getValue().setEnabled(enabledBox.getValue());
			updateComponents();
		});
		
		enabledLayout.addComponent(enabledBox);
		enabledLayout.addStyleName(CustomValoTheme.PADDING_HALF_TOP);
		enabledLayout.addStyleName(CustomValoTheme.FLOAT_CENTER);
		rightLayout.addComponent(enabledLayout);
		
		CssLayout scoreLabelLayout = new CssLayout();
		scoreLabelLayout.setWidth("40%");
		slider = new Slider(0.0d, 100d, 0);
		slider.setWidth("85%");
		slider.addValueChangeListener(l -> {
			getValue().setScoreWeight(slider.getValue() / 100d);
			updateLabels();
			LOGGER.debug("publish event inside valueChanged with topic {} and payload {}", EventBusTopics.SCORE_SETTINGS_WEIGHT_CHANGED, this);
			viewEventBus.publish(EventBusTopics.SCORE_SETTINGS_WEIGHT_CHANGED, this, this);
		});
		scoreLabelLayout.addComponent(slider);

		valueLabel = new Label();
		valueLabel.setWidth("15%");
		valueLabel.addStyleName(CustomValoTheme.FLOAT_RIGHT);
		scoreLabelLayout.addComponent(valueLabel);

		rightLayout.addComponent(scoreLabelLayout);

		updateComponents();
		
		return layout;
	}

	private void updateLabels() {
		valueLabel.setValue(slider.getValue().intValue() + "%");
	}

	private void updateComponents() {

	if(value == null) {
			slider.setValue(0.0d);
			slider.setEnabled(false);
			enabledBox.setValue(false);
			enabledBox.setEnabled(false);
			valueLabel.addStyleName(CustomValoTheme.FONT_COLOR_MENU);
			captionLabel.addStyleName(CustomValoTheme.FONT_COLOR_MENU);
			valueLabel.setValue(slider.getValue().toString());
			dataAvailableLabel.setValue(VaadinIcons.BAN.getHtml());
			dataAvailableLabel.addStyleName(CustomValoTheme.ICON_COLOR_RED);
			return;
		}
		
		enabledBox.setEnabled(true);
		slider.setValue(value.getScoreWeight() * 100);
		enabledBox.setValue(value.isEnabled());
		
		if(!enabledBox.getValue()) {
			slider.setValue(0.0d);
			slider.setEnabled(false);
			valueLabel.addStyleName(CustomValoTheme.FONT_COLOR_MENU);
			captionLabel.addStyleName(CustomValoTheme.FONT_COLOR_MENU);
		} else {
			slider.setEnabled(true);
			valueLabel.removeStyleName(CustomValoTheme.FONT_COLOR_MENU);
			captionLabel.removeStyleName(CustomValoTheme.FONT_COLOR_MENU);
		}
		
		
		if(value.isDataAvailable()) {
			dataAvailableLabel.setValue(VaadinIcons.CHECK.getHtml());
		} else {
			enabledBox.setValue(false);
			enabledBox.setEnabled(false);
			dataAvailableLabel.setValue(VaadinIcons.BAN.getHtml());
		}
		dataAvailableLabel.setStyleName(CustomValoTheme.ICON_COLOR_GREEN, value.isDataAvailable());
		dataAvailableLabel.setStyleName(CustomValoTheme.ICON_COLOR_RED, !value.isDataAvailable());
		
		valueLabel.setValue(slider.getValue().intValue() + "%");
		
		enabledBox.setEnabled(canBeUnchecked);

		if(!scorable) {
			slider.setValue(0.0d);
			slider.setEnabled(false);
			valueLabel.addStyleName(CustomValoTheme.FONT_COLOR_MENU);
		}
	}

	public void setCaptionLabel(String caption) {
		unitCaption = caption;
	}

	public void setLabelStyle(String styleName) {
		captionLabel.addStyleName(styleName);
	}
	
	public String getUnitCaption() {
		return unitCaption;
	}
	
	public void setWeight(double weight) {
		slider.setValue(weight * 100);
	}
	
	public double getWeight() {
		return slider.getValue() / 100d;
	}
	
	public boolean isScoreEnabled() {
		return enabledBox.getValue();
	}
	
	public void canBeUnchecked(boolean uncheckAble) {
		canBeUnchecked = uncheckAble;
		enabledBox.setEnabled(false);
	}

	public void setScorable(boolean scorable) {
		this.scorable = scorable;
	}

	public boolean isScorable() {
		return scorable;
	}

	@Override
	protected void doSetValue(ProcessingUnitState value) {
		this.value = value;
		updateComponents();
	}

	@Override
	public ProcessingUnitState getValue() {
		return value;
	}
}