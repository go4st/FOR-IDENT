package de.hswt.fi.ui.vaadin.windows;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import de.hswt.fi.processing.service.model.ProcessingUnit;
import de.hswt.fi.processing.service.model.ProcessingUnitState;
import de.hswt.fi.processing.service.model.ProcessingUnitState.UnitState;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.LayoutConstants;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.i18n.I18N;

import java.util.EnumMap;
import java.util.Set;

@SpringComponent
@PrototypeScope
public class TaskWindow extends AbstractWindow {

	private static final long serialVersionUID = 1L;

	private CssLayout contentLayout;

	private EnumMap<ProcessingUnit, ProcessingWrapper> processes;

	@Autowired
	protected TaskWindow(ComponentFactory componentFactory, I18N i18n) {
		super(componentFactory, i18n, false);
		setWidth(LayoutConstants.LARGE);
		processes = new EnumMap<>(ProcessingUnit.class);
	}

	@Override
	protected String getWindowCaption() {
		return i18n.get(UIMessageKeys.TASK_WINDOW_CAPTION);
	}

	@Override
	protected Component getContentComponent() {

		initContentLayout();

		setCanFinish(false);
		setClosable(false);
		setOkButtonVisible(false);
		setCancelButtonVisible(false);

		return contentLayout;

	}

	private void initContentLayout() {
		contentLayout = new CssLayout();
		contentLayout.setSizeFull();
	}

	public void setProcessUnits(Set<ProcessingUnit> processUnits) {
		contentLayout.removeAllComponents();

		for (ProcessingUnit unit : processUnits) {
			ProcessingWrapper process = new ProcessingWrapper(getProcessUnitCaption(unit),
					getUnitState(UnitState.IDLE));
			processes.put(unit, process);
			contentLayout.addComponent(process);
		}
	}

	public void processUnitStateChanged(ProcessingUnitState state) {
		if (!processes.containsKey(state.getProcessUnit())) {
			return;
		}
		getUI().access(() -> processes.get(state.getProcessUnit()).setState(state.getUnitState()));
	}

	private String getProcessUnitCaption(ProcessingUnit unitState) {
		switch (unitState) {
			case MASS_SCREENING:
				return i18n.get(UIMessageKeys.TASK_WINDOW_PROCESSING_MASS);
			case RTI_SCREENING:
				return i18n.get(UIMessageKeys.TASK_WINDOW_PROCESSING_RTI);
			case MSMS:
				return i18n.get(UIMessageKeys.TASK_WINDOW_PROCESSING_MSMS);
//			case TP:
//				return i18n.get(UIMessageKeys.TASK_WINDOW_PROCESSING_TP);
			case MASSBANK_SIMPLE:
				return i18n.get(UIMessageKeys.TASK_WINDOW_PROCESSING_MASSBANK);
			default:
				return "";
		}
	}

	private String getUnitState(UnitState unitState) {
		switch (unitState) {
			case IDLE:
				return i18n.get(UIMessageKeys.TASK_WINDOW_WAITING_CAPTION);
			case PROCESSING:
				return i18n.get(UIMessageKeys.TASK_WINDOW_PROCESSING_CAPTION);
			case FINISHED:
				return i18n.get(UIMessageKeys.TASK_WINDOW_FINISHED_CAPTION);
			default:
				return "";
		}
	}

	@Override
	protected void handleOk() {
		// Nothing to to here
	}

	private class ProcessingWrapper extends CssLayout {

		private static final long serialVersionUID = 1L;

		private Label stateLabel;

		private Label spinnerLabel;

		ProcessingWrapper(String caption, String state) {
			addStyleName(CustomValoTheme.BLOCK);

			Label captionLabel = createCaptionLabel(caption);
			spinnerLabel = createSpinnerLabel();
			spinnerLabel.setVisible(false);
			stateLabel = createStateLabel(state);
			stateLabel.setWidth("30%");
			addComponent(componentFactory.createRowLayout(captionLabel, spinnerLabel, stateLabel));
		}

		void setState(UnitState state) {
			stateLabel.setValue(getUnitState(state));
			if (state.equals(UnitState.PROCESSING)) {
				spinnerLabel.setVisible(true);
			} else if (state.equals(UnitState.FINISHED)) {
				spinnerLabel.removeStyleName(CustomValoTheme.LABEL_SPINNER_SMALL);
				spinnerLabel.setContentMode(ContentMode.HTML);
				spinnerLabel.setValue(VaadinIcons.CHECK.getHtml());
				spinnerLabel.addStyleName(CustomValoTheme.COLOR_ALT3);
			}
		}

		private Label createStateLabel(String content) {
			Label label = createCaptionLabel(content);
			label.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
			return label;
		}

		private Label createCaptionLabel(String content) {
			Label label = new Label(content);
			label.setWidthUndefined();
			label.addStyleName(CustomValoTheme.MARGIN_HALF_BOTTOM);
			return label;
		}

		private Label createSpinnerLabel() {
			Label label = new Label();
			label.addStyleName(CustomValoTheme.LABEL_SPINNER_SMALL);
			label.setWidthUndefined();
			return label;
		}
	}
}
