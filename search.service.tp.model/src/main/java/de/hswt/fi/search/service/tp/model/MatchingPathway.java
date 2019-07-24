package de.hswt.fi.search.service.tp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document
public class MatchingPathway {

	@Id
	private String id;

	private List<String> inChiKeys;

	public MatchingPathway(String id) {
		this.id = id;
		inChiKeys = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public List<String> getInChiKeys() {
		return inChiKeys;
	}

	public void addInChiKeys(String inChiKey) {
		if (inChiKeys.contains(inChiKey)) {
			return;
		}
		inChiKeys.add(inChiKey);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MatchingPathway)) return false;
		MatchingPathway that = (MatchingPathway) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(inChiKeys, that.inChiKeys);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id, inChiKeys);
	}
}
