package net.sourceforge.jruntimedesigner.widgets;

import java.util.List;

public interface IWidgetProviderFactory {

	void registerWidgetProvider(IWidgetProvider widgetProvider);

	void unregisterWidgetProvider(IWidgetProvider widgetProvider);

	IWidgetProvider getWidgetProvider(String name);

	List<String> getWidgetProviderNames(String owner);

	boolean isWidgetProviderRegistered(String type);
	
}