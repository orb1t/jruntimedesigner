/*******************************************************************************
 * Copyright (c) 2008 Igor Kunin.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Igor Kunin (ikunin) - initial API and implementation
 ******************************************************************************/
package net.sourceforge.jruntimedesigner.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public class WidgetProviderFactoryRegistry implements IWidgetProviderFactory {
	private static IWidgetProviderFactory instance = null;

	public static IWidgetProviderFactory instance() {
		if (instance == null) {
			instance = new WidgetProviderFactoryRegistry();
		}
		return instance;
	}

	private Map<String, IWidgetProvider> widgetProviders;
	private List<IWidgetProviderFilter> filters = new ArrayList<IWidgetProviderFilter>();

	private WidgetProviderFactoryRegistry() {
		widgetProviders = new HashMap<String, IWidgetProvider>();
		ServiceLoader<IWidgetProvider> loader = ServiceLoader.load(IWidgetProvider.class);
		Iterator<IWidgetProvider> iterator = loader.iterator();
		while (iterator.hasNext()) {
			IWidgetProvider widgetProvider = (IWidgetProvider) iterator.next();
			registerWidgetProvider(widgetProvider);
		}
		ServiceLoader<IWidgetProviderFilter> loader1 = ServiceLoader.load(IWidgetProviderFilter.class);
		Iterator<IWidgetProviderFilter> iterator1 = loader1.iterator();
		while (iterator1.hasNext()) {
			IWidgetProviderFilter filter = (IWidgetProviderFilter) iterator1.next();
			filters.add(filter);
		}
	}

	@Override
	public void registerWidgetProvider(IWidgetProvider widgetProvider) {
		if (widgetProvider == null) {
			throw new IllegalArgumentException("Mandatory parameter widgetProvider is null!");
		}
		if (!widgetProviders.containsKey(widgetProvider.getType())) {
			widgetProviders.put(widgetProvider.getType(), widgetProvider);
		}
	}

	@Override
	public void unregisterWidgetProvider(IWidgetProvider widgetProvider) {
		if (widgetProvider == null) {
			throw new IllegalArgumentException("Mandatory parameter widgetProvider is null!");
		}
		if (widgetProvider.getType() == null) {
			throw new IllegalArgumentException("WidgetProvider type is null!");
		}
		if (widgetProviders.containsKey(widgetProvider.getType())) {
			widgetProviders.remove(widgetProvider.getType());
		}
	}

	@Override
	public IWidgetProvider getWidgetProvider(String name) {
		if (!widgetProviders.containsKey(name)) {
			throw new IllegalArgumentException("WidgetProvider [" + name + "] is not registered!");
		}
		return widgetProviders.get(name);
	}

	@Override
	public List<String> getWidgetProviderNames(String owner) {
		boolean supported = false;
		List<String> names = new ArrayList<String>();
		for(IWidgetProviderFilter filter:filters) {
			if(filter.isSupported(owner)){
				supported = true;
				filter.filter(names, owner);
			}
		} 
		if(!supported){
			names.addAll(widgetProviders.keySet());
		}
		Collections.sort(names);
		
		return names;
	}

	@Override
	public boolean isWidgetProviderRegistered(String type) {
		if (type == null) {
			throw new IllegalArgumentException("Mandatory parameter type is null!");
		}
		return widgetProviders.containsKey(type);
	}

}
