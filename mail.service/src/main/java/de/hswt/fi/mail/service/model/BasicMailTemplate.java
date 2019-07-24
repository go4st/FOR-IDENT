package de.hswt.fi.mail.service.model;

import de.hswt.fi.search.service.mass.search.model.SourceList;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

public class BasicMailTemplate implements Serializable {

	private static final long serialVersionUID = 1L;

	private LocalDateTime date;

	private String username;

	private String email;

	private String organisation;

	private List<SourceList> sourceLists;

	private String comment;

	private String mailTo;

	public BasicMailTemplate() {
		date = LocalDateTime.now();
	}

	public String getDate() {
		return date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.GERMAN));
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public List<SourceList> getSourceLists() {
		return sourceLists;
	}

	public void setSourceLists(List<SourceList> sourceLists) {
		this.sourceLists = sourceLists;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((mailTo == null) ? 0 : mailTo.hashCode());
		result = prime * result + ((organisation == null) ? 0 : organisation.hashCode());
		result = prime * result + ((sourceLists == null) ? 0 : sourceLists.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BasicMailTemplate other = (BasicMailTemplate) obj;
		if (comment == null) {
			if (other.comment != null) {
				return false;
			}
		} else if (!comment.equals(other.comment)) {
			return false;
		}
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (mailTo == null) {
			if (other.mailTo != null) {
				return false;
			}
		} else if (!mailTo.equals(other.mailTo)) {
			return false;
		}
		if (organisation == null) {
			if (other.organisation != null) {
				return false;
			}
		} else if (!organisation.equals(other.organisation)) {
			return false;
		}
		if (sourceLists == null) {
			if (other.sourceLists != null) {
				return false;
			}
		} else if (!sourceLists.equals(other.sourceLists)) {
			return false;
		}
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "BasicMailTemplate [localDateTime=" + date + ", username=" + username + ", email=" + email
				+ ", organisation=" + organisation + ", sourceLists=" + sourceLists + ", comment=" + comment
				+ ", mailTo=" + mailTo + "]";
	}

}
