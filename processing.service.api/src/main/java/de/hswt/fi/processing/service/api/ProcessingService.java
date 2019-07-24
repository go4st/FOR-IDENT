package de.hswt.fi.processing.service.api;

import de.hswt.fi.model.FeatureSet;
import de.hswt.fi.processing.service.model.*;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public interface ProcessingService {

	ProcessingResult executeJob(ProcessingJob job, Consumer<ProcessingUnitState> stateUpdateCallback);

	List<ProcessingUnit> getAvailableProcessingUnits(ProcessingJob job);

	Set<ProcessingUnit> getProcessingUnitOrder(ProcessingSettings settings);

	ProcessingJob getProcessingJob(FeatureSet featureSet);

}
