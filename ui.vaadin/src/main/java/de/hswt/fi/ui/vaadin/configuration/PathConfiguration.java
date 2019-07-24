package de.hswt.fi.ui.vaadin.configuration;

import com.vaadin.spring.annotation.VaadinSessionScope;
import de.hswt.fi.ui.vaadin.UIConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSession;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class PathConfiguration {

	@VaadinSessionScope
	@Bean(name = UIConstants.TEMP_DIRECTORY)
	public Path tempDirectory(HttpSession session) {
		return Paths.get(session.getServletContext().getAttribute("javax.servlet.context.tempdir")
				.toString(), session.getId());
	}

	@VaadinSessionScope
	@Bean(name = UIConstants.TEMP_DIRECTORY_SEARCH)
	public Path searchTempDirectory(@Qualifier(UIConstants.TEMP_DIRECTORY) Path tempDirectory) {
		return tempDirectory.resolve(UIConstants.TEMP_DIRECTROY_SEARCH_SUFFIX);
	}

	@VaadinSessionScope
	@Bean(name = UIConstants.TEMP_DIRECTORY_RTI)
	public Path rtiTempDirectory(@Qualifier(UIConstants.TEMP_DIRECTORY) Path tempDirectory) {
		return tempDirectory.resolve(UIConstants.TEMP_DIRECTORY_RTI_SUFFIX);
	}

	@VaadinSessionScope
	@Bean(name = UIConstants.TEMP_DIRECTORY_PROCESSING)
	public Path processingTempDirectory(@Qualifier(UIConstants.TEMP_DIRECTORY) Path tempDirectory) {
		return tempDirectory.resolve(UIConstants.TEMP_DIRECTORY_PROCESSING_SUFFIX);
	}

	@VaadinSessionScope
	@Bean(name = UIConstants.TEMP_DIRECTORY_CALIBRATION)
	public Path calibrationTempDirectory(@Qualifier(UIConstants.TEMP_DIRECTORY) Path tempDirectory) {
		return tempDirectory.resolve(UIConstants.TEMP_DIRECTORY_CALIBRATION_SUFFIX);
	}

	@VaadinSessionScope
	@Bean(name = UIConstants.TEMP_DIRECTORY_UPLOAD_COMPOUND_DATABASE_PATH)
	public Path uploadCompoundDatabaseTempDirectory(@Qualifier(UIConstants.TEMP_DIRECTORY) Path tempDirectory) {
		return tempDirectory.resolve(UIConstants.TEMP_DIRECTORY_UPLOAD_COMPOUND_DATABASE_SUFFIX);
	}
}