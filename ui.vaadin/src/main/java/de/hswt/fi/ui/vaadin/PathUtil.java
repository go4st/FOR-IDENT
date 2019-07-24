package de.hswt.fi.ui.vaadin;

import com.vaadin.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.annotation.PrototypeScope;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

@SpringComponent
@PrototypeScope
public class PathUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(PathUtil.class);

	private DeletingFileVisitor deletingVisitor = new DeletingFileVisitor();

	public void deleteDirectory(Path path) throws IOException {
		Files.walkFileTree(path, deletingVisitor);
	}

	public void createDirectoryWhenNotExits(Path path) throws IOException {
		if (path.toFile().exists() && path.toFile().isDirectory()) {
			return;
		}

		Files.createDirectories(path);
	}

	private class DeletingFileVisitor extends SimpleFileVisitor<Path> {
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attributes)
				throws IOException {
			if (attributes.isRegularFile()) {
				Files.delete(file);
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path directory, IOException ioe)
				throws IOException {
			Files.delete(directory);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException ioe) {
			LOGGER.error("An error occurred", ioe);
			return FileVisitResult.CONTINUE;
		}
	}
}
