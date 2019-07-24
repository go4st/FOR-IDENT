package de.hswt.fi.ui.vaadin.eventbus.payloads;

import java.nio.file.Path;

public class ProcessingRtiCalibrationFilePayload {

	private Path file;

	private boolean notifyAll;

	public ProcessingRtiCalibrationFilePayload(Path file) {
		this(file, true);
	}

	public ProcessingRtiCalibrationFilePayload(Path file, boolean notifyAll) {
		this.file = file;
		this.notifyAll = notifyAll;
	}

	public boolean isNotifyAll() {
		return notifyAll;
	}

	public Path getFile() {
		return file;
	}

}
