package de.hswt.fi.search.service.rti.model;

import de.hswt.fi.model.FeatureSet;
import de.hswt.fi.search.service.mass.search.model.SourceList;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Marco Luthardt
 */
public class RTIJob implements Serializable {

	private static final long serialVersionUID = 1L;

	private RTISettings settings;
	
	private FeatureSet featureSet;

	/** The source lists. */
	private List<SourceList> sourceLists;

	public FeatureSet getFeatureSet() {
		return featureSet;
	}

	public void setFeatureSet(FeatureSet featureSet) {
		this.featureSet = featureSet;
	}

	public RTIJob(RTISettings settings, FeatureSet featureSet) {
		this.settings = settings;
		this.featureSet = featureSet;
	}

	public RTISettings getSettings() {
		return settings;
	}

	/**
	 * Gets the source lists.
	 *
	 * @return the source lists
	 */
	public List<SourceList> getSourceLists() {
		return sourceLists;
	}

	/**
	 * Sets the source lists.
	 *
	 */
	public void setSourceLists(List<SourceList> sourceLists) {
		this.sourceLists = sourceLists;
	}

}
