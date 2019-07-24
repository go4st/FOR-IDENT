package de.hswt.fi.ui.vaadin.eventbus.payloads;

import de.hswt.fi.search.service.search.api.CompoundSearchService;

import java.nio.file.Path;
import java.time.LocalDate;

public class CompoundDatabasePayload {

	private final Path path;

	private final LocalDate date;

	private final CompoundSearchService compoundSearchService;

	public CompoundDatabasePayload(Path path, LocalDate date, CompoundSearchService compoundSearchService) {
		this.path = path;
		this.date = date;
		this.compoundSearchService = compoundSearchService;
	}

	public Path getPath() {
		return path;
	}

	public LocalDate getDate() {
		return date;
	}

	public CompoundSearchService getCompoundSearchService() {
		return compoundSearchService;
	}
}