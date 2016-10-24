package net.sourceforge.jruntimedesigner.widgets;

import java.util.Collection;

public interface IWidgetProviderFilter {
	boolean isSupported(String owner);
	void filter(Collection<String> names,String owner);
}
