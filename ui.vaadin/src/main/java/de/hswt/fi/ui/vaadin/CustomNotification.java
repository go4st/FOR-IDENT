package de.hswt.fi.ui.vaadin;

import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class CustomNotification {

	private final Notification notification;

	// Delay in Milliseconds
	private static final int DEFAULT_DELAY = 3000;

	private static final Position DEFAULT_POSITION = Position.MIDDLE_CENTER;

	private static final boolean DEFAULT_IS_HTML_ALLOWED = false;

	// User must click to dismiss notification
	public static final int ERROR_DELAY = -1;

	private CustomNotification(Builder builder) {
		String caption = builder.caption;
		notification = new Notification(caption, builder.type);
		notification.setDelayMsec(builder.delay);
		notification.setDescription(builder.descritpion);
		notification.setHtmlContentAllowed(builder.isHtmlAllowed);
		notification.setPosition(builder.position);
		if (!builder.styleName.isEmpty()) {
			notification.setStyleName(notification.getStyleName() + " " + builder.styleName);
		}
	}

	private Notification getNotification() {
		return notification;
	}

	public static class Builder {

		// mandatory fields
		private final String caption;

		private final String descritpion;

		private final Type type;

		// optional fields
		private Position position = DEFAULT_POSITION;

		private int delay = DEFAULT_DELAY;

		private boolean isHtmlAllowed = DEFAULT_IS_HTML_ALLOWED;

		private String styleName = "";

		public Builder(String caption, String description, Type type) {
			this.caption = caption;
			this.descritpion = description;
			this.type = type;
		}

		public Builder position(Position position) {
			this.position = position;
			return this;
		}

		public Builder delay(int delay) {
			this.delay = delay;
			return this;
		}

		public Builder htmlAllowd(boolean isHtmlAllowed) {
			this.isHtmlAllowed = isHtmlAllowed;
			return this;
		}

		public Builder styleName(String styleName) {
			this.styleName = styleName;
			return this;
		}

		public Notification build() {
			return new CustomNotification(this).getNotification();
		}
	}
}
