package de.hswt.fi.ui.vaadin.configuration;

import com.google.common.collect.ImmutableList;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.hswt.fi.search.service.search.api.CompoundSearchService;

import java.util.List;

/**
 @author August Gilg
 **/

@SpringComponent
@ViewScope
public class ViewSharedObjects {

	private ImmutableList<CompoundSearchService> selectedDatabases;

	public void setSelectedSearchServices(List<CompoundSearchService> selectedDatabases) {
		this.selectedDatabases = ImmutableList.copyOf(selectedDatabases);
	}

	public List<CompoundSearchService> getSelectedSearchServices() {
		return selectedDatabases;
	}
}
