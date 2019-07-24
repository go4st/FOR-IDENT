package de.hswt.fi.ui.vaadin.views.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.hswt.fi.application.properties.ApplicationProperties;
import de.hswt.fi.calculation.service.api.CalculationService;
import de.hswt.fi.common.ValueFormatUtil;
import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.search.service.mass.search.model.properties.AbstractNumberProperty;
import de.hswt.fi.search.service.mass.search.model.properties.AbstractStringProperty;
import de.hswt.fi.search.service.mass.search.model.properties.NumberValueProperty;
import de.hswt.fi.search.service.mass.search.model.properties.StringValueProperty;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.components.ContainerContentComponent;
import de.hswt.fi.ui.vaadin.components.DetailsProperty;
import de.hswt.fi.ui.vaadin.components.DetailsSectionComponent;
import de.hswt.fi.ui.vaadin.eventbus.EventBusTopics;
import de.hswt.fi.ui.vaadin.factories.ComponentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ViewEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.events.annotation.EventBusListenerTopic;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.teemu.switchui.Switch;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;
import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractDetailsComponent<ENTRY> extends ContainerContentComponent {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDetailsComponent.class);

	private static final int STRUCTURE_IMAGE_HEIGHT = 400;

	private static final int STRUCTURE_IMAGE_WIDTH = 400;

	private static final String WEB_SEARCH_PREFIX = "http://www.google.com/search?q=";

	protected final I18N i18n;

	private final ApplicationProperties applicationProperties;

	private final ComponentFactory componentFactory;

	private final Provider<DetailsSectionComponent> detailsSectionProvider;

	private final CalculationService calculationService;

	protected final ViewEventBus eventBus;

	private Entry currentEntry;

	protected ENTRY currentEntryContainer;

	private CssLayout headerLayout;

	private DetailsSectionComponent structureComponent;

	private DetailsSectionComponent webSearchComponent;

	private DetailsSectionComponent generalComponent;

	private DetailsSectionComponent categoriesComponent;

	private DetailsSectionComponent listComponent;

	private DetailsSectionComponent logPComponent;

	private DetailsSectionComponent logDComponent;

	private DetailsSectionComponent additionalNamesComponent;

	private DetailsSectionComponent massBankComponent;

	private DetailsSectionComponent externalLinksComponent;

	private CssLayout tileLayout;

	private Set<DetailsSectionComponent> detailsComponents;

	@Autowired
	public AbstractDetailsComponent(I18N i18n, ApplicationProperties applicationProperties,
									ComponentFactory componentFactory, Provider<DetailsSectionComponent> detailsSectionProvider,
									CalculationService calculationService, ViewEventBus eventBus) {
		this.i18n = i18n;
		this.applicationProperties = applicationProperties;
		this.componentFactory = componentFactory;
		this.detailsSectionProvider = detailsSectionProvider;
		this.calculationService = calculationService;
		this.eventBus = eventBus;
	}

	protected abstract Entry getEntry();

	@PostConstruct
	private void postConstruct() {
		addStyleName(CustomValoTheme.CSS_LAYOUT_SCROLLBAR);
		addStyleName(CustomValoTheme.PADDING);

		setSizeFull();

		detailsComponents = new HashSet<>();

		initHeader();
		initComponents();

		eventBus.subscribe(this);
	}

	@PreDestroy
	private void preDestroy() {
		eventBus.unsubscribe(this);
	}

	protected void initHeader() {
		headerLayout = new CssLayout();

		Button downloadButton = componentFactory.createButton(VaadinIcons.DOWNLOAD,
				i18n.get(UIMessageKeys.DOWNLOAD_BUTTON_CAPTION));
		downloadButton.addClickListener(e -> handleDownload());
		headerLayout.addComponent(downloadButton);

		Button reportButton = componentFactory.createButton(VaadinIcons.BUG,
				i18n.get(UIMessageKeys.REPORT_RECORD_BUTTON_CAPTION));
		reportButton.addClickListener(e -> handleReportRecord());
		headerLayout.addComponent(reportButton);

		Label switchLabel = new Label(
				i18n.get(UIMessageKeys.ABSTRACT_DETAILS_COMPONENT_METADATA_CAPTION));
		switchLabel.setWidthUndefined();
		switchLabel.setHeight("100%");
		switchLabel.addStyleName(CustomValoTheme.MARGIN_RIGHT);
		switchLabel.addStyleName(ValoTheme.LABEL_LARGE);
		switchLabel.addStyleName(ValoTheme.LABEL_LIGHT);
		switchLabel.addStyleName(CustomValoTheme.COLOR_ALT1);
		headerLayout.addComponent(switchLabel);

		Switch switchButton = new Switch();
		switchButton.setValue(true);
		switchButton.setPrimaryStyleName("v-custom-switch");
		switchButton.addStyleName(CustomValoTheme.BORDER_COLOR_WHITE);
		switchButton.addValueChangeListener(e -> setMetaDataVisible(switchButton.getValue()));
		headerLayout.addComponent(switchButton);
	}

	private void initComponents() {
		tileLayout = new CssLayout();
		tileLayout.setWidth("100%");
		addComponent(tileLayout);

		structureComponent = createDetailsComponent(
				i18n.get(UIMessageKeys.STRUCTURE_DETAILS_CAPTION));

		webSearchComponent = createDetailsComponent("Web Search");

		generalComponent = createDetailsComponent(
				i18n.get(UIMessageKeys.GENERAL_DETAILS_CAPTION));

		categoriesComponent = createDetailsComponent(
				i18n.get(UIMessageKeys.CATEGORIES_DETAILS_CAPTION));

		listComponent = createDetailsComponent(
				i18n.get(UIMessageKeys.LISTS_DETAILS_CAPTION));

		logPComponent = createDetailsComponent(
				i18n.get(UIMessageKeys.LOG_P_DETAILS_CAPTION));

		logDComponent = createDetailsComponent(
				i18n.get(UIMessageKeys.LOG_D_DETAILS_CAPTION));

		additionalNamesComponent = createDetailsComponent(
				i18n.get(UIMessageKeys.ADDITIONAL_NAME_DETAILS_CAPTION));

		massBankComponent = createDetailsComponent(
				i18n.get(UIMessageKeys.MASSBANK_DETAILS_CAPTION));

		externalLinksComponent = createDetailsComponent(
				i18n.get(UIMessageKeys.EXTERNAL_LINKS_DETAILS_CAPTION));

	}

	private DetailsSectionComponent createDetailsComponent(String caption) {
		DetailsSectionComponent component = detailsSectionProvider.get();
		component.setHeaderCaption(caption);
		component.setWidth("100%");
		component.setVisible(false);
		component.addStyleName(CustomValoTheme.PADDING_VERTICAL);
		tileLayout.addComponent(component);
		detailsComponents.add(component);
		return component;
	}

	private void entryChanged() {
		updateStructure();
		updateWebSearchLinks();
		updateGeneralContainer();

		updateDetailsComponent(getCategoryDetails(), categoriesComponent);
		updateDetailsComponent(getSourceListDetails(), listComponent);
		updateDetailsComponent(getLogPDetails(), logPComponent);
		updateDetailsComponent(getLogDDetails(), logDComponent);
		updateDetailsComponent(getAdditionalNamesDetails(), additionalNamesComponent);
		updateMassBank();
		updateExternalLinks();
	}

	private void updateStructure() {

		// Check if License is available
		if (!calculationService.isAvailable()) {
			structureComponent.setVisible(false);
			return;
		}

		// Remove all previous added Images
		structureComponent.clearItems();

		if (currentEntry.getSmiles() == null) {
			structureComponent.setVisible(false);
			return;
		}

		// Create Image from Smiles & set Visible
		StreamResource resource = generateStreamResource();
		if (resource != null) {
			resource.setMIMEType("image/png");
			Image image = new Image(null, resource);
			image.addStyleName(CustomValoTheme.MAX_WIDTH_100_PERCENT);
			image.addStyleName(CustomValoTheme.MAX_HEIGHT_100_PERCENT);
			structureComponent.addEntry(new DetailsProperty(image));
			structureComponent.setVisible(true);
		}
	}

	private void updateWebSearchLinks() {

		webSearchComponent.clearItems();

		LinkedList<DetailsProperty> properties = new LinkedList<>();

		if (currentEntry.getName() != null) {
			properties.add(new DetailsProperty(createLink(i18n.get(UIMessageKeys.ABSTRACT_DETAILS_COMPONENT_WEB_SEARCH_BY_NAME), WEB_SEARCH_PREFIX + currentEntry.getName().getValue())));
		}

		if (currentEntry.getCas() != null) {
			properties.add(new DetailsProperty(createLink(i18n.get(UIMessageKeys.ABSTRACT_DETAILS_COMPONENT_WEB_SEARCH_BY_CAS), WEB_SEARCH_PREFIX + currentEntry.getCas().getValue())));
		}

		if (currentEntry.getInchi() != null) {
			properties.add(new DetailsProperty(createLink(i18n.get(UIMessageKeys.ABSTRACT_DETAILS_COMPONENT_WEB_SEARCH_BY_INCHI), WEB_SEARCH_PREFIX + currentEntry.getInchi().getValue())));
		}

		updateDetailsComponent(properties, webSearchComponent);
	}

	private StreamResource generateStreamResource() {
		byte[] data = calculationService.getSmilesAsImage(
				currentEntry.getSmiles().getValue(), STRUCTURE_IMAGE_WIDTH,
				STRUCTURE_IMAGE_HEIGHT);
		if (data == null) {
			return null;
		}
		return new StreamResource(() -> new ByteArrayInputStream(data), "");
	}

	private void updateGeneralContainer() {
		updateDetailsComponent(getGeneralProperties(), generalComponent);
	}

	private void updateMassBank() {
		updateDetailsComponent(getMassBankProperties(), massBankComponent);
	}

	private void updateExternalLinks() {
		updateDetailsComponent(getExternalLinkProperties(), externalLinksComponent);
	}

	private void updateDetailsComponent(List<DetailsProperty> properties, DetailsSectionComponent component) {
		component.clearItems();
		component.setVisible(!properties.isEmpty());
		properties.forEach(component::addEntry);
	}

	private List<DetailsProperty> getGeneralProperties() {
		LinkedList<DetailsProperty> properties = new LinkedList<>();

		if (currentEntry == null) {
			return properties;
		}

		properties.add(getProperties(currentEntry.getName(), i18n.get(UIMessageKeys.FI_ENTRY_NAME)));
		properties.add(getStringValueDetailsProperty(currentEntry.getPublicID(), i18n.get(UIMessageKeys.FI_ENTRY_PUBLIC_ID)));
		properties.add(getProperties(currentEntry.getSmiles(), i18n.get(UIMessageKeys.FI_ENTRY_SMILES)));
		properties.add(getProperties(currentEntry.getInchi(), i18n.get(UIMessageKeys.FI_ENTRY_INCHI)));
		properties.add(getProperties(currentEntry.getInchiKey(), i18n.get(UIMessageKeys.FI_ENTRY_INCHI_KEY)));
		properties.add(getProperties(currentEntry.getIupac(), i18n.get(UIMessageKeys.FI_ENTRY_IUPAC)));
		properties.add(getProperties(currentEntry.getElementalFormula(), i18n.get(UIMessageKeys.FI_ENTRY_FORMULA)));
		properties.add(getProperties(currentEntry.getAccurateMass(), i18n.get(UIMessageKeys.FI_ENTRY_MASS)));
		properties.add(getProperties(currentEntry.getCas(), i18n.get(UIMessageKeys.FI_ENTRY_CAS)));

		return properties;
	}

	private List<DetailsProperty> getCategoryDetails() {
		return currentEntry.getCategories().stream()
				.map(category -> {
					DetailsProperty details = new DetailsProperty();
					details.setValueCaption(i18n.get(UIMessageKeys.FI_ENTRY_CATEGROIES));
					details.setValue(category.getValue());
					details.setLastModifiedCaption(i18n.get(UIMessageKeys.FI_ENTRY_LAST_DATE));
					details.setLastModified(ValueFormatUtil.getDateAsString(category.getLastModified()));
					return details;
				})
				.collect(Collectors.toList());
	}

	private List<DetailsProperty> getSourceListDetails() {
		return currentEntry.getSourceLists().stream()
				.map(sourceList -> {
					DetailsProperty details = new DetailsProperty();
					details.setValueCaption(i18n.get(UIMessageKeys.FI_ENTRY_SOURCE_LISTS));
					details.setValue(sourceList.getName());
					details.setAdditionalCaption(i18n.get(UIMessageKeys.FI_ENTRY_DESCRIPTION));
					details.setAdditional(sourceList.getDescription());
					return details;
				})
				.collect(Collectors.toList());
	}

	private List<DetailsProperty> getLogPDetails() {
		return currentEntry.getLogpValues().stream()
				.map(logPValue -> getProperties(logPValue, i18n.get(UIMessageKeys.FI_ENTRY_LOG_P)))
				.collect(Collectors.toList());
	}

	private List<DetailsProperty> getLogDDetails() {
		return currentEntry.getLogdValues().stream()
				.map(logDValue -> getProperties(logDValue, i18n.get(UIMessageKeys.FI_ENTRY_LOG_D)))
				.collect(Collectors.toList());
	}

	private List<DetailsProperty> getAdditionalNamesDetails() {
		return currentEntry.getAdditionalNames().stream()
				.map(name -> getProperties(name, i18n.get(UIMessageKeys.FI_ENTRY_ADDITIONAL_NAMES)))
				.collect(Collectors.toList());
	}

	private DetailsProperty getProperties(StringValueProperty stringValueProperty, String caption) {

		AbstractStringProperty stringProperty = (AbstractStringProperty) stringValueProperty;

		DetailsProperty detailsProperty = new DetailsProperty();
		detailsProperty.setValueCaption(caption);
		detailsProperty.setValue(stringProperty.getValue());
		detailsProperty.setSource(stringProperty.getSource());
		detailsProperty.setSourceCaption(i18n.get(UIMessageKeys.FI_ENTRY_SOURCE));
		detailsProperty.setLastModified(ValueFormatUtil.getDateAsString(stringProperty.getLastModified()));
		detailsProperty.setLastModifiedCaption(i18n.get(UIMessageKeys.FI_ENTRY_LAST_DATE));

		return detailsProperty;
	}

	private DetailsProperty getProperties(NumberValueProperty numberValueProperty, String caption) {

		AbstractNumberProperty numberProperty = (AbstractNumberProperty) numberValueProperty;

		DetailsProperty detailsProperty = new DetailsProperty();
		detailsProperty.setValueCaption(caption);
		detailsProperty.setValue(ValueFormatUtil.formatForMass(numberProperty.getValue()));
		detailsProperty.setSource(numberProperty.getSource());
		detailsProperty.setSourceCaption(i18n.get(UIMessageKeys.FI_ENTRY_SOURCE));
		detailsProperty.setLastModified(ValueFormatUtil.getDateAsString(numberProperty.getLastModified()));
		detailsProperty.setLastModifiedCaption(i18n.get(UIMessageKeys.FI_ENTRY_LAST_DATE));

		if (numberValueProperty.getPh() != null) {
			detailsProperty.setPhCaption(i18n.get(UIMessageKeys.FI_ENTRY_PH));
			detailsProperty.setPh(ValueFormatUtil.formatForPh(numberValueProperty.getPh(), UI.getCurrent().getLocale()));
		}

		return detailsProperty;
	}

	private DetailsProperty getStringValueDetailsProperty(String value, String caption) {
		DetailsProperty detailsProperty = new DetailsProperty();
		detailsProperty.setValueCaption(caption);
		detailsProperty.setValue(value);
		return detailsProperty;
	}

	private List<DetailsProperty> getMassBankProperties() {
		return currentEntry.getMassBankIds().stream()
				.map(massBankId -> new DetailsProperty(createMassBankLink(massBankId.getValue())))
				.collect(Collectors.toList());
	}

	private List<DetailsProperty> getExternalLinkProperties() {
		LinkedList<DetailsProperty> properties = new LinkedList<>();

		if (currentEntry == null) {
			return properties;
		}

		String searchValue = null;
		if (currentEntry.getCas() != null) {
			searchValue = currentEntry.getCas().getValue();
		} else if (currentEntry.getSmiles() != null) {
			searchValue = currentEntry.getSmiles().getValue();
		} else if (currentEntry.getIupac() != null) {
			searchValue = currentEntry.getIupac().getValue();
		} else if (currentEntry.getName() != null) {
			searchValue = currentEntry.getName().getValue();
		}

		properties.add(new DetailsProperty(
				createLink("Echa's REACH", applicationProperties.getUi().getLinks().getReach())));

		String chemicalizeLink;
		if (searchValue != null) {
			chemicalizeLink = applicationProperties.getUi().getLinks()
					.getChemicalizeParameterized();
			chemicalizeLink = chemicalizeLink.replace("{value}", searchValue);
		} else {
			chemicalizeLink = applicationProperties.getUi().getLinks().getChemicalizeRoot();
		}

		String dtxsid = currentEntry.getDtxsid();

		if (dtxsid != null) {
			String epaLink = applicationProperties.getUi().getLinks().getEpa() + dtxsid;
			properties.add(new DetailsProperty(createLink("EPA Dashboard", epaLink)));
		}

		properties.add(new DetailsProperty(createLink("www.chemicalize.org", chemicalizeLink)));

		return properties;
	}

	private Link createMassBankLink(String massBankId) {
		String link = applicationProperties.getUi().getLinks().getMassbankParameterized();
		return createLink("MassBank EU ID: " + massBankId, link.replace("{value}", massBankId));
	}

	private Link createLink(String label, String target) {
		Link link = new Link(label, new ExternalResource(target));
		link.setTargetName("_blank");
		return link;
	}

	private void setMetaDataVisible(boolean visible) {
		detailsComponents.forEach(component -> component.setMetaDataVisible(visible));
	}

	private void handleDownload() {

		if (currentEntry == null) {
			LOGGER.debug("currentEntry is null - returning");
			return;
		}

		LOGGER.debug("publish event inside handleDownload with topic {}",
				EventBusTopics.TARGET_HANDLER_REQUEST_RESULT_DOWNLOAD);
		eventBus.publish(EventBusTopics.TARGET_HANDLER_REQUEST_RESULT_DOWNLOAD, this,
				getEntry());
	}

	private void handleReportRecord() {

		if (currentEntry == null) {
			LOGGER.debug("currentEntry is null - returning");
			return;
		}

		LOGGER.debug("publish event inside handleReportRecord with topic {}",
				EventBusTopics.TARGET_HANDLER_REPORT_TO_STAFF);
		eventBus.publish(EventBusTopics.TARGET_HANDLER_REPORT_TO_STAFF, this,
				currentEntry);
	}

	@EventBusListenerMethod
	@EventBusListenerTopic(topic = EventBusTopics.SOURCE_HANDLER_ENTRY_SELECTED)
	@SuppressWarnings("unused")
	private void handleEntrySelection(ENTRY entry) {
		LOGGER.debug("entering event bus listener handleEntrySelection with ENTRY {} in topic {}",
				entry, EventBusTopics.SOURCE_HANDLER_ENTRY_SELECTED);

		if (currentEntryContainer == null || (entry != null	&& !currentEntryContainer.equals(entry))) {
			currentEntryContainer = entry;
			currentEntry = getEntry();
			entryChanged();
		}
	}

	@Override
	public Component getHeaderComponent() {
		return headerLayout;
	}

	// Do not override hash() and equals() in abstract component classes because of identity issues when attach / remove
}
