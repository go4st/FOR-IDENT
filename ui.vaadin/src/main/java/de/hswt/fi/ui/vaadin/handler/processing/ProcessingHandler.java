package de.hswt.fi.ui.vaadin.handler.processing;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import de.hswt.fi.processing.service.api.ProcessingService;
import de.hswt.fi.processing.service.model.*;
import de.hswt.fi.ui.vaadin.CustomNotification;
import de.hswt.fi.ui.vaadin.configuration.ViewSharedObjects;
import de.hswt.fi.ui.vaadin.container.ProcessingResultContainer;
import de.hswt.fi.ui.vaadin.handler.AbstractSearchHandler;
import de.hswt.fi.ui.vaadin.views.states.ProcessingViewState;
import de.hswt.fi.ui.vaadin.windows.SummaryWindow;
import de.hswt.fi.ui.vaadin.windows.TaskWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.i18n.I18N;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@SpringComponent
@ViewScope
public class ProcessingHandler extends
        AbstractSearchHandler<ProcessingJob, ProcessingResult, ProcessCandidate, ProcessingResultContainer> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingHandler.class);


	private final ProcessingService processService;

	private final ViewSharedObjects viewSharedObjects;

	private final ProcessingViewState viewState;

	private final SummaryWindow summaryWindow;

	private final TaskWindow taskWindow;

	private UI currentUI;

	@Autowired
	public ProcessingHandler(I18N i18n, ProcessingService processService, ViewSharedObjects viewSharedObjects,
							 ProcessingViewState viewState, SummaryWindow summaryWindow, TaskWindow taskWindow) {
		super(i18n, ProcessingResultContainer.class);
		currentUI = UI.getCurrent();
		this.processService = processService;
		this.viewSharedObjects = viewSharedObjects;
		this.viewState = viewState;
		this.summaryWindow = summaryWindow;
		this.taskWindow = taskWindow;
	}

	@EventBusListenerMethod
	protected void handleEvent(ProcessingJob processingJob) {
		LOGGER.debug("entering event bus listener handleEvent with ProcessJob {}", processingJob);

		if (processingJob == null) {
			LOGGER.debug("ProcessJob is null - leaving method");
			return;
		}

		if (!sourceListsAreValid()) {
			LOGGER.debug("sourceLists are empty - returning");
			showErrorNotification();
			return;
		}

		processingJob.setRequestedProcessUnits(
				processService.getAvailableProcessingUnits(processingJob));

		LOGGER.debug("Selected Source Lists: {}", viewSharedObjects.getSelectedSearchServices());
		processingJob.setSelectedSearchServices(viewSharedObjects.getSelectedSearchServices());

		ProcessingResultContainer container = getExistingContainer(processingJob,
				viewState.getSearchHistoryContainer());

		if (container == null) {
			executeProcessing(processingJob);
			return;
		} else if (container.equals(viewState.getCurrentSearch())) {
			// is currently selected search
			LOGGER.debug("equal current search - leaving method");
			return;
		}

		viewState.setCurrentSearch(container);

		// send also an empty result to get the ui notified
		fireUpdateEvents(container);
	}

	private void executeProcessing(ProcessingJob processingJob) {
		
		showTaskWindow(processService.getProcessingUnitOrder(processingJob.getSettings()));

		CompletableFuture.supplyAsync(() -> {
			try {
				return processService.executeJob(processingJob, taskWindow::processUnitStateChanged);
			} catch (Exception e) {
				taskWindow.close();
				new CustomNotification.Builder("Error",
						"An unexpected error occured during processing",
						Type.ERROR_MESSAGE).build().show(UI.getCurrent().getPage());
				LOGGER.error("Error in processing occured: {}", e.getMessage());
				return null;
			}
		}).thenAccept(result -> handleCreatedResult(processingJob, result));

	}

	private void handleCreatedResult(ProcessingJob processingJob, ProcessingResult result) {
		ProcessingResultContainer container = createResultsContainer(processingJob, result);
		currentUI.access(() -> {
			taskWindow.close();
			showSummaryWindow(result.getResultSummary());
			viewState.setCurrentSearch(container);

			// send also an empty result to get the ui notified
			fireUpdateEvents(container);

			// Important garbage collection after processing
			System.gc();
		});
	}

	private void showTaskWindow(Set<ProcessingUnit> processUnits) {
		taskWindow.setProcessUnits(processUnits);
		currentUI.addWindow(taskWindow);
	}

	private void showSummaryWindow(ProcessResultSummary resultSummary) {
		summaryWindow.setResultSummary(resultSummary);
		currentUI.addWindow(summaryWindow);
	}

	private ProcessingResultContainer createResultsContainer(ProcessingJob processJob, ProcessingResult results) {
		ProcessingResultContainer container = createContainer(processJob, results, results.getResults());

		if (!container.getResultsContainer().isEmpty()) {
			viewState.getSearchHistoryContainer().add(container);
		}

		return container;
	}

	private boolean sourceListsAreValid() {
		return !viewSharedObjects.getSelectedSearchServices().isEmpty();
	}
}
