package de.hswt.fi.ui.vaadin.handler.search;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.mail.service.model.MailTemplate;
import de.hswt.fi.mail.service.model.SearchHistoryReportTemplate;
import de.hswt.fi.search.service.mass.search.model.SearchParameter;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.handler.AbstractReportHandler;
import de.hswt.fi.ui.vaadin.windows.ReportWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;

import java.util.HashMap;
import java.util.Map;

@SpringComponent
@ViewScope
public class SearchHistoryReportHandler extends AbstractReportHandler {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchHistoryReportHandler.class);

	private SearchParameter searchParameter;

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.TARGET_HANDLER_REPORT_TO_STAFF)
	private void handleEvent(SearchParameter searchParameter) {
		LOGGER.debug("entering event bus listener handleEvent with payload {} in topic {}", searchParameter,
				EventBusTopics.TARGET_HANDLER_REPORT_TO_STAFF);

		this.searchParameter = searchParameter;
		createAndOpenReportWindow(reportUtil.parseSearchParameter(searchParameter));
	}

	@Override
	protected MailTemplate getMailTemplate() {
		return MailTemplate.SEARCH_HISTORY_REPORT;
	}

	@Override
	protected Map<String, Object> getModel() {
		SearchHistoryReportTemplate<SearchParameter> reportTemplate = generateTemplate(errorNumber);
		Map<String, Object> model = new HashMap<>();
		model.put("report", reportTemplate);
		return model;
	}

	private SearchHistoryReportTemplate<SearchParameter> generateTemplate(String errorNumber) {
		ReportWindow reportWindow = (ReportWindow) getWindow();
		SearchHistoryReportTemplate<SearchParameter> reportTemplate = new SearchHistoryReportTemplate<>();
		reportTemplate.setUsername(reportWindow.getUserName());
		reportTemplate.setEmail(reportWindow.getUserMail());
		reportTemplate.setOrganisation(reportWindow.getUserOrganization());
	
		reportTemplate.setComment(reportWindow.getUserComment());
		reportTemplate.setMailTo(reportUtil.generateMailToLink(reportWindow.getUserMail(), errorNumber));
		
//		reportTemplate.setSourceLists(searchParameter.getSourceLists());
		reportTemplate.setSearchParameter(searchParameter);
		LOGGER.info("Report template: {}", reportTemplate);
		return reportTemplate;
	}

	@Override
	protected String getSubject() {
		return "Bug report [Search " + errorNumber + "]";
	}
}