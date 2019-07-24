package de.hswt.fi.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as definition of a bean column, which is declared via
 * {@link de.hswt.fi.beans.annotations.BeanColumn}.
 *
 * @author Marco Luthardt
 */
public class BeanColumnDefinition {

	/**
	 * A list of i18n keys, which contains the key of the corresponding fields
	 * and all key's of it's parents.
	 */
	private List<String> i18nIds;

	/**
	 * The type of the field which is annotated.
	 */
	private Class<?> type;

	/**
	 * The format definition which is used when converting the value to a
	 * string, e.g. from double.
	 */
	private String formatDefinition;

	/**
	 * The property id, which is used to access the field via bean accessors.
	 */
	private String propertyId;

	/**
	 * The real property id, when the given column is a ghost column.
	 */
	private String revenantPropertyId;

	/**
	 * The non localized caption of the column.
	 */
	private String caption;

	/**
	 * The name of a getter method on the type of the field, which returns a
	 * i18n key for the value of this field.
	 */
	private String i18nValuePropertyId;

	/**
	 * A list of all children, or nested column definitions of the definition.
	 */
	private List<BeanColumnDefinition> children;

	/**
	 * Defines if the type of the field is a collection.
	 */
	private boolean isCollection;

	private boolean isGroup;

	/**
	 * Defines, if this column is a selector.
	 */
	private boolean isSelector;

	public BeanColumnDefinition() {
		i18nIds = new ArrayList<>();
		children = new ArrayList<>();
		formatDefinition = "";
		propertyId = "";
		revenantPropertyId = "";
		caption = "";
		i18nValuePropertyId = "";
		isCollection = false;
		isGroup = false;
		isSelector = false;
	}

	public List<String> getI18nIds() {
		return i18nIds;
	}

	public String getI18nId() {
		if (i18nIds.isEmpty()) {
			return "";
		}
		return i18nIds.get(i18nIds.size() - 1);
	}

	public void addI18nId(String i18nId) {
		if (i18nId == null || i18nId.isEmpty() || i18nIds.contains(i18nId)) {
			return;
		}
		i18nIds.add(0, i18nId);
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getI18nValuePropertyId() {
		return i18nValuePropertyId;
	}

	public void setI18nValuePropertyId(String i18nValuePropertyId) {
		this.i18nValuePropertyId = i18nValuePropertyId;
	}

	public String getFormatDefinition() {
		return formatDefinition;
	}

	public void setFormatDefinition(String formatDefinition) {
		this.formatDefinition = formatDefinition;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getRevenantPropertyId() {
		return revenantPropertyId;
	}

	public void setRevenantPropertyId(String revenantPropertyId) {
		this.revenantPropertyId = revenantPropertyId;
	}

	public void addChild(BeanColumnDefinition child) {
		if (child != null && !children.contains(child)) {
			children.add(child);
		}
	}

	public List<BeanColumnDefinition> getChildren() {
		return children;
	}

	public boolean isCollection() {
		return isCollection;
	}

	public void setCollection(boolean isCollection) {
		this.isCollection = isCollection;
	}

	public boolean isGroup() {
		return isGroup;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public boolean isSelector() {
		return isSelector;
	}

	public void setSelector(boolean isSelector) {
		this.isSelector = isSelector;
	}

	/**
	 * Returns true if the given column is not a real one, a so called ghost
	 * column.
	 *
	 * @return true if this column is a ghost column, otherwise false
	 */
	public boolean isGhost() {
		return !revenantPropertyId.isEmpty();
	}

	@Override
	public String toString() {
		return "BeanColumnDefinition [" + (i18nIds != null ? "i18nIds=" + i18nIds + ", " : "")
				+ (type != null ? "type=" + type + ", " : "")
				+ (formatDefinition != null ? "formatDefinition=" + formatDefinition + ", " : "")
				+ (propertyId != null ? "propertyId=" + propertyId + ", " : "")
				+ (revenantPropertyId != null ? "revenantPropertyId=" + revenantPropertyId + ", "
						: "")
				+ "isCollection=" + isCollection + ", isGroup=" + isGroup + ", isSelector="
				+ isSelector + "]";
	}

}
