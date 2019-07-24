package de.hswt.filehandler.api;

import java.nio.file.Path;

public interface Reader<C> {

	int CAN_NOT_HANDLE = 0;

	double canHandle(C content, Class<? extends Object> clazz);

	Object getContent(Path path);

	Class<? extends Object> getContentClass();

	String getID();
	
}
