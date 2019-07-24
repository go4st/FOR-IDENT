package de.hswt.fi.ui.vaadin.handler.processing;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.mail.service.model.MailTemplate;
import de.hswt.fi.mail.service.model.SearchHistoryReportTemplate;
import de.hswt.fi.processing.service.model.ProcessingJob;
import de.hswt.fi.processing.service.model.ProcessingSettings;
import de.hswt.fi.processing.service.model.ProcessingUnitState;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.handler.AbstractReportHandler;
import de.hswt.fi.ui.vaadin.windows.ReportWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringComponent
@ViewScope
public class ProcessingReportHandler extends AbstractReportHandler {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingReportHandler.class);

	private ProcessingJob processJob;

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_REPORT_TO_STAFF)
	private void handleEvent(ProcessingJob processJob) {
		LOGGER.debug("entering event bus listener handleEvent with payload {} in topic {}",
				processJob, EventBusTopics.TARGET_HANDLER_REPORT_TO_STAFF);

		this.processJob = processJob;
		createAndOpenReportWindow(getBugDescription(processJob));
	}

	private String getBugDescription(ProcessingJob processJob) {
		String bugDescription = "Error Report of incorrect search\n\n" + new Date().toString()
				+ "\n\n";

		bugDescription += "Parameters of search";
		bugDescription = parseSearchParameter(processJob, bugDescription);

		return bugDescription;
	}

	@Override
	protected MailTemplate getMailTemplate() {
		return MailTemplate.PROCESSING_HISTORY_REPORT;
	}

	@Override
	protected Map<String, Object> getModel() {
		SearchHistoryReportTemplate<ProcessingJob> reportTemplate = generateTemplate(errorNumber);
		Map<String, Object> model = new HashMap<>();
		model.put("report", reportTemplate);
		return model;
	}

	protected SearchHistoryReportTemplate<ProcessingJob> generateTemplate(String errorNumber) {
		ReportWindow reportWindow = (ReportWindow) getWindow();
		SearchHistoryReportTemplate<ProcessingJob> reportTemplate = new SearchHistoryReportTemplate<>();
		reportTemplate.setUsername(reportWindow.getUserName());
		reportTemplate.setEmail(reportWindow.getUserMail());
		reportTemplate.setOrganisation(reportWindow.getUserOrganization());

		reportTemplate.setComment(reportWindow.getUserComment());
		reportTemplate
				.setMailTo(reportUtil.generateMailToLink(reportWindow.getUserMail(), errorNumber));

		reportTemplate.setSearchParameter(processJob);
		LOGGER.info("Report template: {}", reportTemplate);
		return reportTemplate;
	}

	@Override
	protected String getSubject() {
		return "Bug report [Processing " + errorNumber + "]";
	}

	private String parseSearchParameter(ProcessingJob processJob, String bugDescription) {

		ProcessingSettings settings = processJob.getSettings();
		bugDescription += "\n\tpH: " + settings.getPh();
		bugDescription += "\n\tppm: " + settings.getPrecursorPpm();
		bugDescription += "\n\tppm Fragments: " + settings.getPpmFragments();
		bugDescription += "\n\tionisation: " + settings.getIonisation();

		bugDescription += "\n\tmass screening enabled: "
				+ settings.getScoreSettings().getMassScreeningState().isEnabled();
		
		bugDescription += "\n\trti screening enabled: "
				+ settings.getScoreSettings().getRtiScreeningState().isEnabled();

		bugDescription += parseProcessState("Mass screening", settings.getScoreSettings().getMassScreeningState());
		bugDescription += parseProcessState("RTI screening", settings.getScoreSettings().getRtiScreeningState());
		bugDescription += parseProcessState("MS/MS", settings.getScoreSettings().getMsmsState());
		//TODO: Reenable if needed
		/*bugDescription += parseProcessState("Chemspider", settings.getChemSpiderState());*/
		bugDescription += parseProcessState("MassBank", settings.getScoreSettings().getMassBankSimpleState());

		return bugDescription;
	}

	private String parseProcessState(String caption, ProcessingUnitState state) {

		if (state == null) {
			return "";
		}

		String stateDescription = "\n\t" + caption + " enabled: " + state.isEnabled();
		stateDescription += "\n\t" + caption + " executed: " + state.isExecute();
		stateDescription += "\n\t" + caption + " score: " + state.getScoreWeight();
		stateDescription += "\n\t" + caption + " data available: " + state.isDataAvailable();

		if (state.getUnitState() != null && !state.getUnitState().name().isEmpty()) {
			stateDescription += "\n\t" + caption + "unit state:" + state.getUnitState().name();
		}

		return stateDescription;
	}
}