package de.hswt.fi.ui.vaadin;

import com.vaadin.spring.annotation.SpringComponent;
import de.hswt.fi.common.spring.Profiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Profile;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringComponent
@Profile(Profiles.NOT_TEST)
public class CustomHttpSessionListener implements HttpSessionListener, ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomHttpSessionListener.class);

	private final PathUtil pathUtil;

	@Autowired
	public CustomHttpSessionListener(PathUtil pathUtil) {
		this.pathUtil = pathUtil;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		if (applicationContext instanceof WebApplicationContext) {
			((WebApplicationContext) applicationContext).getServletContext().addListener(this);
		} else {
			throw new ApplicationContextException("Must be inside a web application context");
		}
	}

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		Path tempDir = getTempPath(sessionEvent);
		try {
			if (!tempDir.toFile().exists()) {
				pathUtil.createDirectoryWhenNotExits(tempDir);
				pathUtil.createDirectoryWhenNotExits(
						tempDir.resolve(UIConstants.TEMP_DIRECTROY_SEARCH_SUFFIX));
				pathUtil.createDirectoryWhenNotExits(
						tempDir.resolve(UIConstants.TEMP_DIRECTORY_RTI_SUFFIX));
				pathUtil.createDirectoryWhenNotExits(
						tempDir.resolve(UIConstants.TEMP_DIRECTORY_PROCESSING_SUFFIX));
				pathUtil.createDirectoryWhenNotExits(
						tempDir.resolve(UIConstants.TEMP_DIRECTORY_CALIBRATION_SUFFIX));
				pathUtil.createDirectoryWhenNotExits(
						tempDir.resolve(UIConstants.TEMP_DIRECTORY_UPLOAD_COMPOUND_DATABASE_SUFFIX));
				LOGGER.info("created session temporary directory: {}", tempDir);
			}
		} catch (IOException e) {
			LOGGER.error("Cannot create Temp dir for:" + sessionEvent.getSession().getId(), e);
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		Path tempDir = getTempPath(sessionEvent);
		try {
			if (tempDir.toFile().exists()) {
				LOGGER.info("destroyed session temporary directory: {}", tempDir);
				pathUtil.deleteDirectory(tempDir);
			}
		} catch (IOException e) {
			LOGGER.error("Cannot delete Temp dir for:" + sessionEvent.getSession().getId(), e);
		}
	}

	private Path getTempPath(HttpSessionEvent sessionEvent) {
		return Paths.get(
				sessionEvent.getSession().getServletContext()
						.getAttribute("javax.servlet.context.tempdir").toString(),
				sessionEvent.getSession().getId());
	}
}
