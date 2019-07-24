package de.hswt.fi.ui.vaadin.eventbus.payloads;

public class UserPropertyPayload<T> {

	private String key;

	private T value;

	public UserPropertyPayload(String key, T value) {
		this.key = key;
		this.value = value;
	}

	public UserPropertyPayload(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public T getValue() {
		return this.value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserPropertyPayload [key=");
		builder.append(this.key);
		builder.append(", value=");
		builder.append(this.value);
		builder.append("]");
		return builder.toString();
	}

}
