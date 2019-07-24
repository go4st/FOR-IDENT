package de.hswt.fi.ui.vaadin.handler;

import de.hswt.fi.mail.service.model.EntryReportTemplate;
import de.hswt.fi.mail.service.model.MailTemplate;
import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.ui.vaadin.windows.ReportWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractEntryReportHandler extends AbstractReportHandler {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntryReportHandler.class);

	private static final String NOT_AVAILABLE_STRING = "not available";

	private List<Entry> entries;

	protected void reportEntries(List<Entry> entries) {
		if (entries.isEmpty()) {
			return;
		}
		this.entries = entries;
		// 1 entry reported (details view report)
		if (entries.size() == 1) {
			createAndOpenReportWindow(createBugDescriptionSingleEntry(entries.iterator().next()));
		} else {
			createAndOpenReportWindow(createBugDescriptionMultipleEntries(entries));
		}
	}

	@Override
	protected MailTemplate getMailTemplate() {
		return MailTemplate.ENTRY_REPORT;
	}

	@Override
	protected Map<String, Object> getModel() {
		EntryReportTemplate entryReportTemplate = generateTemplate(errorNumber);
		Map<String, Object> model = new HashMap<>();
		model.put("report", entryReportTemplate);
		return model;
	}

	private EntryReportTemplate generateTemplate(String errorNumber) {
		ReportWindow reportWindow = (ReportWindow) getWindow();
		EntryReportTemplate entryReportTemplate = new EntryReportTemplate();
		entryReportTemplate.setUsername(reportWindow.getUserName());
		entryReportTemplate.setEmail(reportWindow.getUserMail());
		entryReportTemplate.setOrganisation(reportWindow.getUserOrganization());
		entryReportTemplate.setComment(reportWindow.getUserComment());
		entryReportTemplate
				.setMailTo(reportUtil.generateMailToLink(reportWindow.getUserMail(), errorNumber));
		entryReportTemplate.setEntries(entries);
		LOGGER.info("Report template: {}", entryReportTemplate);
		return entryReportTemplate;
	}

	private String createBugDescriptionMultipleEntries(List<Entry> entries) {
		StringBuilder bugDescription = new StringBuilder("Error Report of multiple incorrect entries\n\n"
				+ new Date().toString() + "\n\n");

		bugDescription.append("Entries:\n");

		for (Entry entry : entries) {
			bugDescription.append(parseEntryDetails(entry)).append("\n");
		}

		return bugDescription.toString();
	}

	private String createBugDescriptionSingleEntry(Entry entry) {

		String bugDescription = "Error Report of incorrect entry details\n\n"
				+ new Date().toString() + "\n\n";

		bugDescription += "Entry:\n";
		bugDescription += parseEntryDetails(entry);

		return bugDescription;
	}

	private String parseEntryDetails(Entry entry) {

		String entryDetails = "Public ID: " + getStringRepresentation(entry.getPublicID());
		entryDetails += ", Name: " + getStringRepresentation(entry.getName().getValue());
		entryDetails += ", SMILES: " + getStringRepresentation(entry.getSmiles().getValue());
		entryDetails += ", CAS: " + getStringRepresentation(entry.getCas().getValue());
		entryDetails += ", Inchi: " + getStringRepresentation(entry.getInchi().getValue());
		entryDetails += ", Inchi key: " + getStringRepresentation(entry.getInchiKey().getValue());

		return entryDetails;
	}


	private String getStringRepresentation(String string) {
		return string != null ? string : NOT_AVAILABLE_STRING;
	}
}
