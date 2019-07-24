package de.hswt.fi.ui.vaadin;

import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.application.properties.ApplicationProperties;
import de.hswt.fi.search.service.mass.search.model.SearchParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.i18n.I18N;

import java.util.UUID;

@SpringComponent
@PrototypeScope
public class ReportUtil {

	private final I18N i18n;

	private final ApplicationProperties applicationProperties;

	private StringBuilder stringBuilder;

	@Autowired
	public ReportUtil(I18N i18n, ApplicationProperties applicationProperties) {
		this.i18n = i18n;
		this.applicationProperties = applicationProperties;
	}
	public String generateMailToLink(String email, String errorCode) {
		String mailTo = "mailto:" + email + "?subject=Report:" + errorCode + "&bcc=";
		mailTo += String.join(",", applicationProperties.getContactAdresses());
		return mailTo;
	}

	public String getErrorNumber() {
		return "#" + UUID.randomUUID().toString().substring(0, 8);
	}

	public String generateReportMessage(String errorCode, String userName, String userMail, String organisation, String bugDescription, String comment) {

		String mailTo = "mailto:" + userMail + "?subject=Report:" + errorCode + "&bcc=";

		mailTo += String.join(",", applicationProperties.getContactAdresses());

		mailTo = "<a href=\"" + mailTo + "\">Reply</a>";

		return "User: " + userName + "\nMail: " + userMail + "\nOrganization: " + organisation
				+ "\nreported a bug" + "\n\nGenerated Bug description\n\n" + bugDescription
				+ "\n\n\nUser Comment:\n\n" + comment + "\n\n" + mailTo;
	}

	public String parseSearchParameter(SearchParameter searchParameter) {

		stringBuilder = new StringBuilder();

		stringBuilder.append(i18n.get(UIMessageKeys.REPORT_UTIL_SUBMITTED_DATA_TEXT));

		if (searchParameter.getIndexChar() != ' ') {
			stringBuilder.append("\nIndex: ")
					.append(searchParameter.getIndexChar());
		}

		appendIfNotNull("\nId: ", searchParameter.getPublicID());
		appendIfNotNull("\nInchi key: " + ": ", searchParameter.getInchiKey());
		appendIfNotNull("\nCAS: ", searchParameter.getCas());
		appendIfNotNull("\nName: ", searchParameter.getName());
		appendIfNotNull("\nAcurate Mass: ", searchParameter.getAccurateMass());
		appendIfNotNull(" " + LayoutConstants.UNICODE_PLUS_MINUS_SIGN + " Ppm: ", searchParameter.getPpm());
		appendIfNotNull("\nElemental Formula: ", searchParameter.getElementalFormula());
		appendIfNotNull("\nHalogens: ", searchParameter.getHalogens());
		appendIfNotNull("\nIonisation: ", searchParameter.getIonisation());
		appendIfNotNull("\nIUPAC: ", searchParameter.getIupac());
		appendIfNotNull("\nLogD: ", searchParameter.getLogD());
		appendIfNotNull(" " + LayoutConstants.UNICODE_PLUS_MINUS_SIGN , searchParameter.getLogDDelta());
		appendIfNotNull("\nLogP: ", searchParameter.getLogP());
		appendIfNotNull(" " + LayoutConstants.UNICODE_PLUS_MINUS_SIGN, searchParameter.getLogPDelta());
		appendIfNotNull("\nSMILES: ", searchParameter.getSmiles());

		return stringBuilder.toString();
	}

	private void appendIfNotNull(String caption, Object value) {
		if (value != null && !value.toString().isEmpty()) {
			stringBuilder.append(caption)
					.append(value.toString());
		}
	}

	public Notification createSuccessNotification() {
		return new CustomNotification.Builder(i18n.get(UIMessageKeys.SEND_REPORT_EMAIL_SUCCESS), "",
				Type.HUMANIZED_MESSAGE).delay(3000).htmlAllowd(false)
						.position(Position.MIDDLE_CENTER).styleName(ValoTheme.NOTIFICATION_SUCCESS)
						.build();
	}

	public Notification createErrorNotification() {
		return new CustomNotification.Builder(i18n.get(UIMessageKeys.SEND_REPORT_EMAIL_ERROR), "",
				Type.ERROR_MESSAGE).delay(Notification.DELAY_FOREVER).htmlAllowd(false)
						.position(Position.MIDDLE_CENTER).build();
	}

}
