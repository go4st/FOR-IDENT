package de.hswt.fi.common.spring;

import org.springframework.context.annotation.Profile;

/**
 * This class contains String Definition which can bes used alongside a
 * {@link Profile}
 * 
 *
 */
public abstract class Profiles {
	
	private Profiles() {
		// prevent Instantiation
	}
	
	public static final String NOT_TEST = "!test";

	public static final String TEST = "test";
	
	public static final String INITIALIZATION = "initialization";

	public static final String LC = "lc";

	public static final String GC = "gc";

	public static final String DEVELOPMENT_LC = "development-lc";

	public static final String DEVELOPMENT_GC = "development-gc";
}
