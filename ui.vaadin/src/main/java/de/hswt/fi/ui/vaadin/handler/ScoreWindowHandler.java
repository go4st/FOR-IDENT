package de.hswt.fi.ui.vaadin.handler;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import de.hswt.fi.processing.service.model.ProcessingSettings;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.windows.AbstractWindow;
import de.hswt.fi.ui.vaadin.windows.ScoreWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

@SpringComponent
@ViewScope
public class ScoreWindowHandler extends AbstractWindowHandler<ViewEventBus> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ScoreWindowHandler.class);

	private ScoreWindow scoreWindow;

	@Autowired
	public ScoreWindowHandler(ScoreWindow scoreWindow) {
		this.scoreWindow = scoreWindow;
	}

	@SuppressWarnings("unused")
	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.OPEN_SCORE_WINDOW)
	private void handleScoreWindow(ProcessingSettings settings) {
		LOGGER.debug("entering event bus listener handleScoreWindow with payload {} and topic {}", settings, EventBusTopics.OPEN_SCORE_WINDOW);
		UI.getCurrent().addWindow(scoreWindow);
		scoreWindow.setProcessingJob(settings);
	}

	@Override
	public void windowClose(CloseEvent e) {
		if (AbstractWindow.CloseType.OK.equals(scoreWindow.getCloseType())) {
			LOGGER.debug("publish event inside handleOk with payload {} and topic {}", scoreWindow.getSettings(),
					EventBusTopics.PROCESSING_SCORE_SETTINGS_CHANGED);
			eventBus.publish(EventBusTopics.PROCESSING_SCORE_SETTINGS_CHANGED, this, scoreWindow.getSettings());
		}
	}

	@Override
	protected Window getWindow() {
		return scoreWindow;
	}
}