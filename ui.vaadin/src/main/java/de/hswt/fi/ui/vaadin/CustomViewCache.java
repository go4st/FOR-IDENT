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
package de.hswt.fi.ui.vaadin;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.Registration;
import com.vaadin.spring.internal.BeanStore;
import com.vaadin.spring.internal.ViewCache;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Default implementation of
	 * {//link org.vaadin.spring.navigator.internal.ViewCache}. For internal use
 * only.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public class CustomViewCache implements ViewCache {

	private static final long serialVersionUID = 4634842615905376953L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomViewCache.class);

	private Map<String, ViewBeanStore> beanStores = new HashMap<>();

	private String viewUnderConstruction = null;

	private String activeView = null;

	private Registration registration;

	@Override
	public void creatingView(String viewName) {
		LOGGER.trace("Creating view [{}] in cache [{}]", viewName, this);
		getOrCreateBeanStore(viewName);
		viewUnderConstruction = viewName;
	}

	@Override
	public void viewCreated(String viewName, View viewInstance) {
		LOGGER.trace("View [{}] created in cache [{}]", viewName, this);
		viewUnderConstruction = null;
		ViewBeanStore beanStore = getOrCreateBeanStore(viewName);
		if (viewInstance == null) {
			LOGGER.trace("There was a problem creating the view [{}] in cache [{}], destroying its bean store",
					viewName, this);
			beanStore.destroy();
		}
	}

	@Override
	public BeanStore getCurrentViewBeanStore() {
		if (viewUnderConstruction != null) {
			LOGGER.trace(
					"Currently the view [{}] is under construction in cache [{}], returning its bean store",
					viewUnderConstruction, this);
			return getBeanStore(viewUnderConstruction);
		} else if (activeView != null) {
			LOGGER.trace(
					"Currently the view [{}] is active in cache [{}], returning its bean store",
					activeView, this);
			return getBeanStore(activeView);
		} else {
			throw new IllegalStateException("No active view");
		}
	}

	@PreDestroy
	void destroy() {
		LOGGER.trace("View cache [{}] has been destroyed, destroying all bean stores", this);
		for (ViewBeanStore beanStore : new HashSet<>(beanStores.values())) {
			beanStore.destroy();
		}
		Assert.isTrue(beanStores.isEmpty(),
				"beanStores should have been emptied by the destruction callbacks");
	}

	private ViewBeanStore getOrCreateBeanStore(final String viewName) {
		ViewBeanStore beanStore = beanStores.get(viewName);
		if (beanStore == null) {
			UI ui = getCurrentUI();
			if (ui == null) {
				throw new IllegalStateException("No UI bound to current thread");
			}
			beanStore = new ViewBeanStore(ui, viewName, new BeanStore.DestructionCallback() {

				private static final long serialVersionUID = 5580606280246825742L;

				@Override
				public void beanStoreDestroyed(BeanStore beanStore) {
					beanStores.remove(viewName);
				}
			});
			beanStores.put(viewName, beanStore);
		}
		return beanStore;
	}

	/**
	 * Returns the current UI.
	 */
	private UI getCurrentUI() {
		return UI.getCurrent();
	}

	private ViewBeanStore getBeanStore(String viewName) {
		ViewBeanStore beanStore = beanStores.get(viewName);
		if (beanStore == null) {
			throw new IllegalStateException("The view " + viewName + " has not been created");
		}
		return beanStore;
	}

	private class ViewBeanStore extends BeanStore implements ViewChangeListener {

		private static final long serialVersionUID = -7655740852919880134L;

		private String viewName;

		private Navigator navigator;

		private ViewBeanStore(UI ui, String viewName, DestructionCallback destructionCallback) {
			super(ui.getId() + ":" + viewName, destructionCallback);
			this.viewName = viewName;
			navigator = ui.getNavigator();
			if (navigator == null) {
				throw new IllegalStateException("UI has no Navigator");
			}
			LOGGER.trace("Adding [{}} as view change listener to [{}]", this, navigator);
			registration = navigator.addViewChangeListener(this);
		}

		@Override
		public void destroy() {
			LOGGER.trace("Removing [{}] as view change listener from [{}]", this, navigator);
			registration.remove();
			super.destroy();
		}

		@Override
		public boolean beforeViewChange(ViewChangeEvent viewChangeEvent) {
			return true;
		}

		@Override
		public void afterViewChange(ViewChangeEvent viewChangeEvent) {
			if (viewName.equals(viewChangeEvent.getViewName())) {
				viewActivated(viewName);
			} else {
				viewDeactivated(viewName);
			}
		}

		private void viewActivated(String viewName) {
			LOGGER.trace("View [{}] activated in cache [{}]", viewName, this);
			activeView = viewName;
		}

		/*
		 *
		 */
		private void viewDeactivated(String viewName) {
			LOGGER.trace("View [{}] deactivated in cache [{}], destroying its bean store", viewName,
					this);
			if (viewName.equals(activeView)) {
				activeView = null;
			}
			// getBeanStore(viewName).destroy();
			LOGGER.trace("Bean stores stored in cache [{}]: {}", this, beanStores.size());
		}
	}

}
