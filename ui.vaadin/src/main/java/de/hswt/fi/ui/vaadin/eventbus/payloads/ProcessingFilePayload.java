package de.hswt.fi.ui.vaadin.eventbus.payloads;

import java.nio.file.Path;
import java.util.List;

public class ProcessingFilePayload {

	private List<Path> files;

	private Path rtiCalibration;
	
	private boolean notifyAll;

	public ProcessingFilePayload(List<Path> files, Path rtiCalibration) {
		this(files, rtiCalibration, true);
	}

	public ProcessingFilePayload(List<Path> files, Path rtiCalibration, boolean notifyAll) {
		this.files = files;
		this.notifyAll = notifyAll;
		this.rtiCalibration = rtiCalibration;
	}

	public boolean isNotifyAll() {
		return notifyAll;
	}

	public List<Path> getPaths() {
		return files;
	}
	
	public Path getCalibrationFile() {
		return rtiCalibration;
	}
}