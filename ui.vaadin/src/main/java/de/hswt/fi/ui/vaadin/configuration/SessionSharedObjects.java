package de.hswt.fi.ui.vaadin.configuration;

import com.google.common.collect.ImmutableList;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.VaadinSessionScope;
import de.hswt.fi.model.FeatureSet;
import de.hswt.fi.search.service.search.api.CompoundSearchService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 @author August Gilg
 **/

@SpringComponent
@VaadinSessionScope
public class SessionSharedObjects {

    private boolean cookiesAccepted = false;

	private List<FeatureSet> processingData;
	
	private String stoffidentId;
	
	private String inchiKey;
	
	private String redirectViewName;

	private LinkedHashMap<CompoundSearchService, Boolean> searchServices;

	@Autowired
	public SessionSharedObjects(List<CompoundSearchService> compoundSearchServices) {
		compoundSearchServices.sort(Comparator.comparingInt(CompoundSearchService::getIndex));
		searchServices = compoundSearchServices.stream()
				.collect(Collectors.toMap(
						Function.identity(),
						CompoundSearchService::isAccessible,
						(e1, e2) -> e1,
						LinkedHashMap::new));
		processingData = new ArrayList<>();
	}

    public boolean isCookiesAccepted() {
        return cookiesAccepted;
    }

    public void setCookiesAccepted(boolean cookiesAccepted) {
        this.cookiesAccepted = cookiesAccepted;
    }

	public void addProcessingData(FeatureSet featureSet) {
		if (featureSet == null) {
			return;
		}
		processingData.add(featureSet);
	}

	public List<FeatureSet> getAvailableProcessingData() {
		return ImmutableList.copyOf(processingData);
	}

	public void clearProcessingData() {
		processingData.clear();
	}

	public Optional<String> getStoffidentId() {
		return Optional.ofNullable(stoffidentId);
	}

	public void setStoffidentId(String stoffidentId) {
		this.stoffidentId = stoffidentId;
	}

	public Optional<String> getInchiKey() {
		return Optional.ofNullable(inchiKey);
	}

	public void setInchiKey(String inchiKey) {
		this.inchiKey = inchiKey;
	}

	public Optional<String> getRedirectViewName() {
		return Optional.ofNullable(redirectViewName);
	}

	public void setRedirectViewName(String redirectViewName) {
		this.redirectViewName = redirectViewName;
	}

	public List<CompoundSearchService> getSearchServices() {
		return ImmutableList.copyOf(searchServices.keySet());
	}

	public List<CompoundSearchService> getAvailableSearchServices() {
		return searchServices.entrySet().stream()
				.filter(Map.Entry::getValue)
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
	}

	public void enableSearchService(String searchServiceName) {
		searchServices.entrySet().stream()
				.filter(entry -> entry.getKey().getDatasourceName().equals(searchServiceName))
				.findFirst()
				.ifPresent(entryMatch -> entryMatch.setValue(true));
	}
}