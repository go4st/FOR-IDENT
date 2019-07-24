package de.hswt.fi.ui.vaadin.grid;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.TextField;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by luthardt on 4/27/17.
 */
public abstract class AbstractNumberFilterField<T extends Number> extends CustomField<AbstractNumberFilterField.NumberRange<T>> {

	private TextField fromTextField;

	private TextField toTextField;

	private NumberRange<T> currentValue;

	protected abstract T getConvertedValue(String text);

	public AbstractNumberFilterField() {
		fromTextField = new TextField();
		fromTextField.setWidth("50%");
		fromTextField.setPlaceholder(">=");
		fromTextField.addValueChangeListener(e -> fireEvent(createValueChange(currentValue, true)));
		toTextField = new TextField();
		toTextField.setWidth("50%");
		toTextField.setPlaceholder("<=");
		toTextField.addValueChangeListener(e -> fireEvent(createValueChange(currentValue, true)));
	}

	@Override
	protected Component initContent() {
		CssLayout layout = new CssLayout(fromTextField, toTextField);
		layout.setWidth("100%");
		return layout;
	}

	@Override
	protected void doSetValue(AbstractNumberFilterField.NumberRange<T> value) {

	}

	@Override
	public NumberRange<T> getValue() {
		currentValue = new NumberRange<>(getConvertedValue(fromTextField.getValue()),
				getConvertedValue(toTextField.getValue()));
		return currentValue;
	}

	public static class NumberRange<V> {

		V from;

		V to;

		private NumberRange(V from, V to) {
			this.from = from;
			this.to = to;
		}

		public Optional<V> getFrom() {
			return Optional.ofNullable(from);
		}

		public Optional<V> getTo() {
			return Optional.ofNullable(to);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		AbstractNumberFilterField<?> that = (AbstractNumberFilterField<?>) o;
		return Objects.equals(fromTextField, that.fromTextField) &&
				Objects.equals(toTextField, that.toTextField) &&
				Objects.equals(currentValue, that.currentValue);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), fromTextField, toTextField, currentValue);
	}
}
