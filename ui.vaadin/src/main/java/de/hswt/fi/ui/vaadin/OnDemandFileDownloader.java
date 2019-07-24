package de.hswt.fi.ui.vaadin;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;

import java.io.IOException;

public class OnDemandFileDownloader extends FileDownloader {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2314838141223704190L;

	/**
	 * Provide both the {@link StreamSource} and the filename in an on-demand
	 * way.
	 */
	public interface OnDemandStreamResource extends StreamSource {
		String getFilename();
	}

	private OnDemandStreamResource onDemandStreamResource;

	public OnDemandFileDownloader(OnDemandStreamResource onDemandStreamResource) {
		super(new StreamResource(onDemandStreamResource, ""));
		this.onDemandStreamResource = onDemandStreamResource;
		if (this.onDemandStreamResource == null) {
			throw new IllegalArgumentException("Stream Resource may never be null");
		}

	}

	@Override
	public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path)
			throws IOException {
		getResource().setFilename(onDemandStreamResource.getFilename());
		return super.handleConnectorRequest(request, response, path);
	}

	private StreamResource getResource() {
		return (StreamResource) this.getResource("dl");
	}
}
