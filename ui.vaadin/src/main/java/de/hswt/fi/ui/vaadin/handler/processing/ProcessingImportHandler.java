package de.hswt.fi.ui.vaadin.handler.processing;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Notification.Type;
import de.hswt.fi.fileimport.service.api.FeatureContentImporter;
import de.hswt.fi.model.FeatureSet;
import de.hswt.fi.processing.service.api.ProcessingService;
import de.hswt.fi.processing.service.model.ProcessingJob;
import de.hswt.fi.ui.vaadin.CustomNotification;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.configuration.SessionSharedObjects;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.eventbus.payloads.ProcessingFilePayload;
import de.hswt.fi.ui.vaadin.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBus.SessionEventBus;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.i18n.I18N;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringComponent
@ViewScope
public class ProcessingImportHandler extends AbstractHandler<ViewEventBus> {

	private static final long serialVersionUID = 1L;

	private static final int MAX_AMOUNT_FEATURES = 10000;

	private static Logger LOGGER = LoggerFactory.getLogger(ProcessingImportHandler.class);

	private final I18N i18n;

	private final FeatureContentImporter featureContentImporter;

	private final ProcessingService processService;

	private final SessionEventBus sessionEventBus;

	private final SessionSharedObjects sessionObjects;

	@Autowired
	public ProcessingImportHandler(I18N i18n, FeatureContentImporter featureContentImporter,
								   ProcessingService processService, SessionEventBus sessionEventBus,
								   SessionSharedObjects sessionObjects) {
		this.i18n = i18n;
		this.featureContentImporter = featureContentImporter;
		this.processService = processService;
		this.sessionEventBus = sessionEventBus;
		this.sessionObjects = sessionObjects;
	}

	@EventBusListenerMethod
	protected void handleEvent(Event<ProcessingFilePayload> event) {
		LOGGER.debug("entering event bus listener handleEvent " + "with payload {}",
				event.getPayload());

		// Import all files and store as FeatureSets in list
		List<FeatureSet> featureSets = new ArrayList<>();
		List<Path> paths = event.getPayload().getPaths();

		// Check if calibration file was set
		Path calibrationPath = event.getPayload().getCalibrationFile();
		if(calibrationPath == null || calibrationPath.toString().isEmpty()) {
			paths
				.stream()
				.map(featureContentImporter::importFromFile)
				.filter(Optional::isPresent)
				.forEach(presentFeature -> featureSets.add(presentFeature.get()));
		} else {
			paths
				.stream()
				.map(feature -> featureContentImporter.importFromFileWithCalibrationData(feature,calibrationPath))
				.filter(Optional::isPresent)
				.forEach(presentFeature -> featureSets.add(presentFeature.get()));
		}
		
		createProcessingJobs(featureSets);
	}

	private void createProcessingJobs(List<FeatureSet> featureSets) {

		List<FeatureSet> nonNull = featureSets.stream().filter(Objects::isNull).collect(Collectors.toList());
		List<FeatureSet> nonNullNonEmpty = nonNull.stream().filter(featureSet -> featureSet.getFeatures().isEmpty()).collect(Collectors.toList());
		List<FeatureSet> nonNullTooMany = nonNull.stream().filter(featureSet -> featureSet.getFeatures().size() > MAX_AMOUNT_FEATURES).collect(Collectors.toList());

		featureSets.removeAll(Stream.of(nonNull.stream(), nonNullNonEmpty.stream(), nonNullTooMany.stream())
				.flatMap(Function.identity())
				.collect(Collectors.toList()));

		nonNullNonEmpty.forEach(this::handleEmptyFeatureSet);
		nonNullTooMany.forEach(this::handleTooManyInFeatureSet);
		featureSets.forEach(this::handleValidFeatureSets);
	}

	private void handleEmptyFeatureSet(FeatureSet featureSet) {
		LOGGER.debug("uploaded file {} is invalid", featureSet.getName());
		showInvalidFileNotification(
				i18n.get(
						UIMessageKeys.FILE_SEARCH_FORM_COMPONENT_INVALID_FILE_NOTIFICATION_CAPTION),
				i18n.get(
						UIMessageKeys.FILE_SEARCH_FORM_COMPONENT_INVALID_FILE_NOTIFICATION_DESCRIPTION,
						featureSet.getName()));
	}

	private void handleTooManyInFeatureSet(FeatureSet featureSet) {
		LOGGER.debug("too many features in feature set {}", featureSet.getFeatures().size());
		showInvalidFileNotification(i18n.get(
				UIMessageKeys.FILE_SEARCH_FORM_COMPONENT_INVALID_FILE_NOTIFICATION_CAPTION),
				i18n.get(
						UIMessageKeys.FILE_SEARCH_FORM_COMPONENT_TOO_MANY_FEATURES_DESCRIPTION,
						featureSet.getName(), MAX_AMOUNT_FEATURES, featureSet.getFeatures().size()));
	}

	private void handleValidFeatureSets(FeatureSet featureSet) {
		sessionObjects.addProcessingData(featureSet);
		ProcessingJob processJob = processService.getProcessingJob(featureSet);

		LOGGER.debug("publish session event in handleEvent with payload ({}) and topic {}",
				processJob, EventBusTopics.SOURCE_HANDLER_FILE_ADDED);
		sessionEventBus.publish(EventBusTopics.SOURCE_HANDLER_FILE_ADDED, this, processJob);
		LOGGER.debug("publish event in handleEvent with payload ({}) and topic {}",
				processJob, EventBusTopics.SOURCE_HANDLER_SEARCH_SELECTED);
		eventBus.publish(EventBusTopics.SOURCE_HANDLER_SEARCH_SELECTED, this, processJob);
	}

	private void showInvalidFileNotification(String caption, String message) {
		new CustomNotification.Builder(caption, message, Type.ERROR_MESSAGE)
				.delay(CustomNotification.ERROR_DELAY).position(Position.MIDDLE_CENTER).build()
				.show(Page.getCurrent());
	}
}