package de.hswt.fi.ui.vaadin.filter;

import java.util.regex.PatternSyntaxException;

/**
 * Created by luthardt on 4/5/17.
 */
public class Filters {

	private Filters() {
		// Prevent instantiation
	}

	public static boolean subWordFilter(String content, String filterText) {
		// Pass all if words not given
		if (filterText == null || filterText.isEmpty()) {
			return true;
		} else if (content == null) {
			return false;
		}

		String pattern = filterText.replace("", ".*");

		// The actual filter logic + error handling
		try {
			return content.matches("(?i)" + pattern);
		} catch (PatternSyntaxException e) {
			return false;
		}
	}

}
