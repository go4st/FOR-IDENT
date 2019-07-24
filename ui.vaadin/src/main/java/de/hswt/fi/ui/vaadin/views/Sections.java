/*
 * Copyright 2015 The original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.hswt.fi.ui.vaadin.views;

import de.hswt.fi.ui.vaadin.UIMessageKeys;
import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.SideBarSection;
import org.vaadin.spring.sidebar.annotation.SideBarSections;

/**
 * This is a Spring-managed bean that does not do anything. Its only purpose is
 * to define the sections of the side bar. The reason it is configured as a bean
 * is that it makes it possible to lookup the de.hswt.fi.beans.annotations from the Spring
 * application context.
 */
@SideBarSections({
		@SideBarSection(id = Sections.TOP, captionCode = UIMessageKeys.SECTIONS_TOP_CAPTION),
		@SideBarSection(id = Sections.SEARCH, captionCode = UIMessageKeys.SECTIONS_SEARCH_CAPTION),
		@SideBarSection(id = Sections.PROCESSING,
				captionCode = UIMessageKeys.SECTIONS_PROCSSING_CAPTION),
		@SideBarSection(id = Sections.ADMINISTRATION,
				captionCode = UIMessageKeys.SECTIONS_ADMINISTRATION_CAPTION) })
@Component
public class Sections {

	private Sections() {
		// Prevent instantiation
	}

	public static final String TOP = "de.hswt.fi.top";
	public static final String SEARCH = "de.hswt.fi.search";
	public static final String PROCESSING = "de.hswt.fi.processing";
	public static final String ADMINISTRATION = "de.hswt.fi.admin";
}
