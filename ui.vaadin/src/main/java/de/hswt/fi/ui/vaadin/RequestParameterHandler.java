package de.hswt.fi.ui.vaadin;

import com.vaadin.server.SynchronizedRequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import de.hswt.fi.fileimport.service.api.FeatureContentImporter;
import de.hswt.fi.model.FeatureSet;
import de.hswt.fi.ui.vaadin.configuration.SessionSharedObjects;
import de.hswt.fi.ui.vaadin.views.ProcessingView;
import de.hswt.fi.ui.vaadin.views.SearchView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

import static com.google.common.base.Strings.isNullOrEmpty;

@SpringComponent
public class RequestParameterHandler extends SynchronizedRequestHandler {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestParameterHandler.class);

	private static final String PARAMETER_PUBLIC_ID = "stoffidentid";

	private static final String PARAMETER_INCHI_KEY = "inchikey";

	private static final String PARAMETER_PROCESS_DATA = "processdata";

	private final ApplicationContext applicationContext;

	private final FeatureContentImporter featureContentImporter;

	@Autowired
	public RequestParameterHandler(ApplicationContext applicationContext, FeatureContentImporter featureContentImporter) {
		this.applicationContext = applicationContext;
		this.featureContentImporter = featureContentImporter;
	}

	@Override
	public boolean synchronizedHandleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response) {
		if (request.getMethod().equals("GET")) {
			handleGetRequest(request);
		} else if (request.getMethod().equals("POST")) {
			handlePostMethod(request);
		}

		return false;
	}

	private void handleGetRequest(VaadinRequest request) {
		if (!isNullOrEmpty(request.getParameter(PARAMETER_PUBLIC_ID))) {
			handlePublicId(request.getParameter(PARAMETER_PUBLIC_ID));
		} else if (!isNullOrEmpty(request.getParameter(PARAMETER_INCHI_KEY))) {
			handleInChiKey(request.getParameter(PARAMETER_INCHI_KEY));
		}
	}

	private void handlePostMethod(VaadinRequest request) {
		LOGGER.debug("entering method handlePost");
		if (request.getParameter(PARAMETER_PROCESS_DATA) != null) {
			handleProcessData(request.getParameter(PARAMETER_PROCESS_DATA));
		}
	}

	private void handlePublicId(String publicId) {
		LOGGER.debug("found request parameter {}", PARAMETER_PUBLIC_ID);
		SessionSharedObjects sessionSharedObjects = getSessionSharedObjectsFromContext();
		sessionSharedObjects.setStoffidentId(publicId);
		setRedirectParameterToSearchView(sessionSharedObjects);
	}

	private void handleInChiKey(String inChiKey) {
		LOGGER.debug("found request parameter {}", PARAMETER_INCHI_KEY);
		SessionSharedObjects sessionSharedObjects = getSessionSharedObjectsFromContext();
		sessionSharedObjects.setInchiKey(inChiKey);
		setRedirectParameterToSearchView(sessionSharedObjects);
	}

	private void handleProcessData(String processData) {
		LOGGER.debug("found request parameter {}\t value: {}", processData, PARAMETER_PROCESS_DATA);

		SessionSharedObjects sessionSharedObjects = getSessionSharedObjectsFromContext();

		Optional<FeatureSet> featureSet = featureContentImporter.importContent(processData);
		if (!featureSet.isPresent()) {
			sessionSharedObjects.setRedirectViewName(null);
			return;
		}
		sessionSharedObjects.addProcessingData(featureSet.get());
		sessionSharedObjects.setRedirectViewName(ProcessingView.VIEW_NAME);
	}

	private SessionSharedObjects getSessionSharedObjectsFromContext() {
		return applicationContext.getBean(SessionSharedObjects.class);
	}

	private void setRedirectParameterToSearchView(SessionSharedObjects sessionSharedObjects) {
		sessionSharedObjects.setRedirectViewName(SearchView.VIEW_NAME);
	}

}
