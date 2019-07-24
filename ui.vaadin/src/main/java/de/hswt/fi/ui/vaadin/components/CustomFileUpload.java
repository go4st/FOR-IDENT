package de.hswt.fi.ui.vaadin.components;

import com.vaadin.ui.Upload;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomFileUpload extends Upload {

    private static final long serialVersionUID = -2028718257956576293L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomFileUpload.class);

	private final FileReceiver receiver;

	private final Path directory;

	public CustomFileUpload(Path directory) {
		super();
		this.directory = directory;
		this.receiver = new FileReceiver();

		setReceiver(receiver);

		addFailedListener(event -> {
			try {
				Files.delete(receiver.getPath());
			} catch (IOException e) {
				LOGGER.error("An error occured: ", e);
			}
		});

		// TODO Move this to own css declaration
		setButtonStyleName("v-button " + CustomValoTheme.BACKGROUND_COLOR_ALT3 + " " + CustomValoTheme.BORDER_NONE);
	}

	@Override
	public FileReceiver getReceiver() {
		return receiver;
	}

	public class FileReceiver implements Upload.Receiver {

		private Path path;

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {

			try {
				path = Paths.get(directory + File.separator + filename);
				if (!Files.exists(path)) {
					return new FileOutputStream(Files.createFile(path).toFile());
				}
			} catch (IOException e) {
				LOGGER.error("An error occured: ", e);
			}

			return null;
		}

		public Path getPath() {
			return path;
		}
	}
}
