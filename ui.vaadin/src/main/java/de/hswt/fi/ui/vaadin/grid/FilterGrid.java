package de.hswt.fi.ui.vaadin.grid;

import com.vaadin.data.HasValue;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.AbstractRenderer;
import com.vaadin.ui.renderers.Renderer;
import de.hswt.fi.ui.vaadin.filter.Filters;

import java.util.Objects;

/**
 * Created by luthardt on 4/23/17.
 */
public class FilterGrid<T> extends Grid<T> {

	public static class FilterColumn<T, V> extends Column<T, V> {

		private HasValue<?> filterComponent;

		private Class<?> columnModelType;

		private ValueProvider<T, ?> filterValueProvider;

		/**
		 * Constructs a new Column configuration with given renderer and value
		 * provider.
		 *
		 * @param valueProvider the function to get values from items, not
		 *                      <code>null</code>
		 * @param renderer
		 */
		protected FilterColumn(ValueProvider<T, V> valueProvider, Renderer<? super V> renderer) {
			super(valueProvider, renderer);
			filterValueProvider = valueProvider;
		}

		/**
		 * Constructs a new Column configuration with given renderer and value
		 * provider.
		 * <p>
		 * For a more complete explanation on presentation provider, see
		 * {@link #setRenderer(ValueProvider, Renderer)}.
		 *
		 * @param valueProvider
		 *            the function to get values from items, not
		 *            <code>null</code>
		 * @param presentationProvider
		 *            the function to get presentations from the value of this
		 *            column, not <code>null</code>. For more details, see
		 *            {@link #setRenderer(ValueProvider, Renderer)}
		 * @param renderer
		 *            the presentation renderer, not <code>null</code>
		 * @param <P>
		 *            the presentation type
		 *
		 * @since 8.1
		 */
		protected <P> FilterColumn(ValueProvider<T, V> valueProvider,
							 ValueProvider<V, P> presentationProvider,
							 Renderer<? super P> renderer) {
			super(valueProvider, presentationProvider, renderer);
			filterValueProvider = valueProvider;
		}
		public FilterColumn<T, V> setFilterType(Class<?> columnModelType) {
			this.columnModelType = columnModelType;
			this.filterComponent = createFilterForColumn(columnModelType);
			return this;
		}

		public <M> FilterColumn<T, V> setFilterCaptionProvider(ItemCaptionGenerator<M> captionProvider, Class<M> columnModelType) {
			setFilterType(columnModelType);
			if (ComboBox.class.isInstance(filterComponent)) {
				((ComboBox<M>)filterComponent).setItemCaptionGenerator(captionProvider);
			}
			return this;
		}

		public FilterColumn<T, V> setFilterValueProvider(ValueProvider<T, ?> valueProvider) {
			filterValueProvider = valueProvider;
			return this;
		}

		private HasValue<?> createFilterForColumn(Class<?> columnModelType) {
			HasValue<?> filterComponent = createFilterComponent(columnModelType);
			if (filterComponent != null) {
				((FilterGrid<T>)getGrid()).getFilterRow().getCell(this).setComponent((Component) filterComponent);
			}
			return filterComponent;
		}

		private HasValue<?> createFilterComponent(Class<?> columnModelType) {
			HasValue<?> filterValue = null;
			if (String.class.equals(columnModelType)) {
				TextField textField = new TextField();
				textField.setWidth("100%");
				textField.setPlaceholder("Filter ...");
				filterValue = textField;
			} else if (Boolean.class.equals(columnModelType)) {
				ComboBox<Boolean> comboBox = new ComboBox<>();
				comboBox.setItems(true, false);
				comboBox.setWidth("100%");
				comboBox.setPlaceholder("Filter ...");
				filterValue = comboBox;
			} else if (Double.class.equals(columnModelType)) {
				DoubleFilterField filterField = new DoubleFilterField();
				filterField.setWidth("100%");
				filterValue = filterField;
			} else if (Integer.class.equals(columnModelType)) {
				IntegerFilterField filterField = new IntegerFilterField();
				filterField.setWidth("100%");
				filterValue = filterField;
			}

			if (filterValue != null) {
				filterValue.addValueChangeListener(event -> {
					getGrid().getDataProvider().refreshAll();
				});
			}

			return filterValue;
		}

		private SerializablePredicate<T> getFilter() {
			if (getValueProvider() == null) {
				return null;
			} else if (String.class.equals(columnModelType)) {
				return model -> Filters.subWordFilter(
							(String)filterValueProvider.apply(model), (String)filterComponent.getValue());
			} else if (Boolean.class.equals(columnModelType)) {
				return model -> filterComponent.getValue() == null || Objects.equals(
						filterValueProvider.apply(model), filterComponent.getValue());
			} else if (Double.class.equals(columnModelType)) {
				return this::applyDoubleFilter;
			} else if (Integer.class.equals(columnModelType)) {
				return this::applyIntegerFilter;
			}
			return null;
		}

		private boolean applyIntegerFilter(T model) {
			Integer value = (Integer)filterValueProvider.apply(model);
			IntegerFilterField.NumberRange<Integer> range = ((IntegerFilterField)filterComponent).getValue();

			if (!range.getFrom().isPresent() && !range.getTo().isPresent()) {
				return true;
			} else if (value == null) {
				return false;
			} else if (!range.getFrom().isPresent()) {
				return range.getTo().get() >= value;
			} else if (!range.getTo().isPresent()) {
					return range.getFrom().get() <= value;
				} else if (range.getFrom().get() >= range.getTo().get()) {
					return false;
				} else {
					return range.getFrom().get() <= value && range.getTo().get() >= value;
				}
			}

			private boolean applyDoubleFilter(T model) {
				Double value = (Double)filterValueProvider.apply(model);
				DoubleFilterField.NumberRange<Double> range = ((DoubleFilterField)filterComponent).getValue();

				if (!range.getFrom().isPresent() && !range.getTo().isPresent()) {
					return true;
				} else if (value == null) {
					return false;
				} else if (!range.getFrom().isPresent()) {
					return range.getTo().get() >= value;
				} else if (!range.getTo().isPresent()) {
					return range.getFrom().get() <= value;
				} else if (range.getFrom().get() >= range.getTo().get()) {
					return false;
				} else {
					return range.getFrom().get() <= value && range.getTo().get() >= value;
				}
			}

			private void clearFilter() {
				if (filterComponent != null) {
					filterComponent.clear();
				}
			}
		}

		protected <V, P> Column<T, V> createColumn(
				ValueProvider<T, V> valueProvider,
				ValueProvider<V, P> presentationProvider,
				AbstractRenderer<? super T, ? super P> renderer) {
			return new FilterColumn<>(valueProvider, presentationProvider, renderer);
		}

		private HeaderRow filterRow;

		public FilterGrid(Class<T> beanType) {
			super(beanType);

			filterRow = addHeaderRowAt(1);
		}

		public <V> FilterColumn<T, V> addFilterColumn(ValueProvider<T, V> valueProvider) {
			return (FilterColumn<T, V>) addColumn(valueProvider);
		}

		public <V> FilterColumn<T, V> addFilterColumn(ValueProvider<T, V> valueProvider,
										  AbstractRenderer<? super T, ? super V> renderer) {
			return (FilterColumn<T, V>) addColumn(valueProvider, renderer);
		}

		private HeaderRow getFilterRow() {
			return filterRow;
		}


		@SuppressWarnings("unchecked")
		public void setListDataProvider(ListDataProvider<T> dataProvider) {
			dataProvider.clearFilters();
			for (Column<T, ?> column : getColumns()) {
				if (!FilterColumn.class.isInstance(column)) {
					continue;
				}
				SerializablePredicate<T> filter = ((FilterColumn<T,?>)column).getFilter();
				if (filter != null) {
					dataProvider.addFilter(filter);
				}
			}
			super.setDataProvider(dataProvider);
		}

		public void clearFilter() {
			for (Column<T, ?> column : getColumns()) {
				if (column instanceof FilterColumn) {
					((FilterColumn) column).clearFilter();
				}
			}
			if (getDataProvider() instanceof ListDataProvider) {
				((ListDataProvider) getDataProvider()).clearFilters();
			}
		}
	}
