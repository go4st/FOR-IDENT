package de.hswt.fi.fileimport.service.xml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class Spectrum {

	@JacksonXmlProperty(isAttribute = true)
	private String type;

	@JacksonXmlProperty(isAttribute = true, localName = "scans")
	private int numberOfScans;

	@JacksonXmlProperty(isAttribute = true, localName = "cpdAlgo")
	private String peakDetectionAlgorithm;

	@JacksonXmlProperty(localName = "MSDetails")
	private MsDetails msDetails;

	@JacksonXmlProperty(localName = "RTRanges")
	private List<RetentionTimeRange> retentionTimeRanges;
	
	@JacksonXmlProperty(localName="Device")
	private Device device;
	
	@JacksonXmlProperty(localName = "MzOfInterest")
	private MzOfInterest mzOfInterest;
	
	@JacksonXmlProperty(localName = "MSPeaks")
	private List<MsPeak> peaks;
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNumberOfScans() {
		return numberOfScans;
	}

	public void setNumberOfScans(int numberOfScans) {
		this.numberOfScans = numberOfScans;
	}

	public String getPeakDetectionAlgorithm() {
		return peakDetectionAlgorithm;
	}

	public void setPeakDetectionAlgorithm(String peakDetectionAlgorithm) {
		this.peakDetectionAlgorithm = peakDetectionAlgorithm;
	}

	public MsDetails getMsDetails() {
		return msDetails;
	}

	public void setMsDetails(MsDetails msDetails) {
		this.msDetails = msDetails;
	}

	public List<RetentionTimeRange> getRetentionTimeRanges() {
		return retentionTimeRanges;
	}

	public void setRetentionTimeRanges(List<RetentionTimeRange> retentionTimeRanges) {
		this.retentionTimeRanges = retentionTimeRanges;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("Spectrum [");
		if (type != null) {
			builder.append("type=");
			builder.append(type);
			builder.append(", ");
		}
		builder.append(", numberOfScans=");
		builder.append(numberOfScans);
		builder.append(", ");
		if (peakDetectionAlgorithm != null) {
			builder.append("peakDetectionAlgorithm=");
			builder.append(peakDetectionAlgorithm);
			builder.append(", ");
		}
		if (msDetails != null) {
			builder.append("msDetails=");
			builder.append(msDetails);
			builder.append(", ");
		}
		if (retentionTimeRanges != null) {
			builder.append("retentionTimeRanges=");
			builder.append(
					retentionTimeRanges.subList(0, Math.min(retentionTimeRanges.size(), maxLen)));
			builder.append(", ");
		}
		if (device != null) {
			builder.append("device=");
			builder.append(device);
			builder.append(", ");
		}
		if (mzOfInterest != null) {
			builder.append("mzOfInterest=");
			builder.append(mzOfInterest);
			builder.append(", ");
		}
		if (peaks != null) {
			builder.append("peaks=");
			builder.append(peaks.subList(0, Math.min(peaks.size(), maxLen)));
		}
		builder.append("]");
		return builder.toString();
	}

	public MzOfInterest getMzOfInterest() {
		return mzOfInterest;
	}

	public void setMzOfInterest(MzOfInterest mzOfInterest) {
		this.mzOfInterest = mzOfInterest;
	}

	public List<MsPeak> getPeaks() {
		return peaks;
	}

	public void setPeaks(List<MsPeak> peaks) {
		this.peaks = peaks;
	}

}
