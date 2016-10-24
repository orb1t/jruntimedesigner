/**
 * 
 */
package net.sourceforge.jruntimedesigner.widgets.swing;

import javax.swing.JSplitPane;

/**
 * @author wolcen
 *
 */
public class JSplitPaneWidgetProvider extends SwingContainerWidgetProvider<JSplitPane> {

	@Override
	protected void initialize(SwingContainerWidget<JSplitPane> widget) {
		super.initialize(widget);
		JSplitPane component = (JSplitPane)widget.getComponent();
		component.setResizeWeight(0.5);
		component.setOneTouchExpandable(true);
		component.setDividerLocation(0.5);
		component.setLeftComponent(widget.createEditor("JSplitPaneLeft"));
		component.setRightComponent(widget.createEditor("JSplitPaneRight"));
	}
	
	

}
