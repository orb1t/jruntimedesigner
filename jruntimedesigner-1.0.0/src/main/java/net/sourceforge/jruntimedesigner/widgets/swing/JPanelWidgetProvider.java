/**
 * 
 */
package net.sourceforge.jruntimedesigner.widgets.swing;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * @author wolcen
 *
 */
public class JPanelWidgetProvider extends SwingContainerWidgetProvider<JPanel> {
 
	@Override
	protected void initialize(SwingContainerWidget<JPanel> widget) {
		super.initialize(widget);
		JComponent component = widget.getComponent();
		component.setLayout(new BorderLayout());
		component.add(widget.createEditor("JPanel"), BorderLayout.CENTER);
	}

}
