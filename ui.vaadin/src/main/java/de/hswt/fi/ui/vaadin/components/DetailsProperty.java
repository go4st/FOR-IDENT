package de.hswt.fi.ui.vaadin.components;

import com.vaadin.ui.Component;

import java.util.Optional;

public class DetailsProperty {

	private Component component;

	private String value;

	private String valueCaption;

	private String ph;

	private String phCaption;

	private String source;

	private String sourceCaption;

	private String additional;

	private String additionalCaption;

	private String lastModified;

	private String lastModifiedCaption;

	public DetailsProperty() {
	}

	public DetailsProperty(Component component) {
		this.component = component;
	}

	public Optional<Component> getComponent() {
		return Optional.ofNullable(component);
	}

	public Optional<String> getValue() {
		return Optional.ofNullable(value);
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Optional<String> getValueCaption() {
		return Optional.ofNullable(valueCaption);
	}

	public void setValueCaption(String valueCaption) {
		this.valueCaption = valueCaption;
	}

	public Optional<String> getSource() {
		return Optional.ofNullable(source);
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Optional<String> getSourceCaption() {
		return Optional.ofNullable(sourceCaption);
	}

	public void setSourceCaption(String sourceCaption) {
		this.sourceCaption = sourceCaption;
	}

	public Optional<String> getAdditional() {
		return Optional.ofNullable(additional);
	}

	public void setAdditional(String additional) {
		this.additional = additional;
	}

	public Optional<String> getAdditionalCaption() {
		return Optional.ofNullable(additionalCaption);
	}

	public void setAdditionalCaption(String additionalCaption) {
		this.additionalCaption = additionalCaption;
	}

	public Optional<String> getLastModified() {
		return Optional.ofNullable(lastModified);
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public Optional<String> getLastModifiedCaption() {
		return Optional.ofNullable(lastModifiedCaption);
	}

	public void setLastModifiedCaption(String lastModifiedCaption) {
		this.lastModifiedCaption = lastModifiedCaption;
	}

	public Optional<String> getPh() {
		return Optional.ofNullable(ph);
	}

	public void setPh(String ph) {
		this.ph = ph;
	}

	public Optional<String> getPhCaption() {
		return Optional.ofNullable(phCaption);
	}

	public void setPhCaption(String phCaption) {
		this.phCaption = phCaption;
	}
}