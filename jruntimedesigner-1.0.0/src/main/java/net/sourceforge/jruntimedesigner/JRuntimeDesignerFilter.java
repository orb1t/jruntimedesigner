package net.sourceforge.jruntimedesigner;

import java.util.Collection;

import net.sourceforge.jruntimedesigner.widgets.IWidgetProviderFilter;

public class JRuntimeDesignerFilter implements IWidgetProviderFilter {

	@Override
	public boolean isSupported(String owner) {
		return false ;
	}

	@Override
	public void filter(Collection<String> names,String owner) {
		names.add("Canvas");
	}
}
