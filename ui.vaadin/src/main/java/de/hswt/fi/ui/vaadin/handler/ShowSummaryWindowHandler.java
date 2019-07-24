package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.DummyPayload;
import de.hswt.fi.ui.vaadin.views.states.ProcessingViewState;
import de.hswt.fi.ui.vaadin.windows.SummaryWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

@SpringComponent
@ViewScope
public class ShowSummaryWindowHandler extends AbstractWindowHandler<ViewEventBus> {

	private final ProcessingViewState viewState;

	private final SummaryWindow summaryWindow;

	private static final long serialVersionUID = 1L;

	@Autowired
	public ShowSummaryWindowHandler(ProcessingViewState viewState, SummaryWindow summaryWindow) {
		this.viewState = viewState;
		this.summaryWindow = summaryWindow;
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.OPEN_SUMMARY_WINDOW)
	private void handleClearFiles(DummyPayload payload) {
		summaryWindow.setResultSummary(viewState.getCurrentSearch().getResult().getResultSummary());
		UI.getCurrent().addWindow(summaryWindow);
	}

	@Override
	public void windowClose(CloseEvent e) {
		// Nothing to do ...
	}

	@Override
	protected Window getWindow() {
		return summaryWindow;
	}
}
