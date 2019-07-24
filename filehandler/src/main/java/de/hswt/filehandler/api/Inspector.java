package de.hswt.filehandler.api;

import java.nio.file.Path;
import java.util.Map;

public interface Inspector<C> {

	Map<String, Double> getCapableReaders(Map<String, Reader<C>> readers, Path path, Class<? extends Object> clazz);

	double canHandle(Reader<C> reader, Path path, Class<? extends Object> clazz);

}
