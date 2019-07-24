package de.hswt.fi.msms.service.model;

import java.util.List;

public class MsMsJob {

	private MsMsSettings settings;

	private List<MsMsData> msMsData;

	public MsMsJob(MsMsSettings settings, List<MsMsData> msmsData) {
		this.settings = settings;
		msMsData = msmsData;
	}

	public MsMsSettings getSettings() {
		return settings;
	}

	public List<MsMsData> getMsMsData() {
		return msMsData;
	}
}
