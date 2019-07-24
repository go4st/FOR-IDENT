package de.hswt.fi.application.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "de.hswt.fi.ui")
public class UIProperties {

	private Header header;

	private Links links;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Links getLinks() {
		return links;
	}

	public void setLinks(Links links) {
		this.links = links;
	}

	public static class Header {

		private String caption;

		private String collapsedCaption;

		private boolean visible;

		public void setVisible(boolean visible) {
			this.visible = visible;
		}

		public boolean isVisible() {
			return visible;
		}

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}

		public String getCollapsedCaption() {
			return collapsedCaption;
		}

		public void setCollapsedCaption(String collapsedCaption) {
			this.collapsedCaption = collapsedCaption;
		}

		@Override
		public String toString() {
			return "Header [caption=" + caption + ", collapsedCaption=" + collapsedCaption + "]";
		}

	}

	public static class Links {

		private String reach;

		private String massbankParameterized;

		private String chemicalizeRoot;

		private String chemicalizeParameterized;

		private String epa;

		public String getReach() {
			return reach;
		}

		public void setReach(String reach) {
			this.reach = reach;
		}

		public String getMassbankParameterized() {
			return massbankParameterized;
		}

		public void setMassbankParameterized(String massbankParameterized) {
			this.massbankParameterized = massbankParameterized;
		}

		public String getChemicalizeRoot() {
			return chemicalizeRoot;
		}

		public void setChemicalizeRoot(String chemicalizeRoot) {
			this.chemicalizeRoot = chemicalizeRoot;
		}

		public String getChemicalizeParameterized() {
			return chemicalizeParameterized;
		}

		public void setChemicalizeParameterized(String chemicalizeParameterized) {
			this.chemicalizeParameterized = chemicalizeParameterized;
		}

		public String getEpa() {
			return epa;
		}

		public void setEpa(String epa) {
			this.epa = epa;
		}

		@Override
		public String toString() {
			return "Link [" + (reach != null ? "reach=" + reach + ", " : "")
					+ (massbankParameterized != null
							? "massbankParameterized=" + massbankParameterized + ", " : "")
					+ (chemicalizeRoot != null ? "chemicalizeRoot=" + chemicalizeRoot + ", " : "")
					+ (chemicalizeParameterized != null
							? "chemicalizeParameterized=" + chemicalizeParameterized : "")
					+ "]";
		}

	}

	@Override
	public String toString() {
		return "UserInterfaceConfigurationProperties [header=" + header + "]";
	}

}
