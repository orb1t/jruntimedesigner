/**
 * 
 */
package net.sourceforge.jruntimedesigner.widgets.swing;

import java.awt.Dimension;

import javax.swing.JLabel;

/**
 * @author wolcen
 *
 */
public class JLabelWidgetProvider extends SwingWidgetProvider<JLabel> {

	@Override
	protected void initialize(JLabel component) {
		super.initialize(component);
		component.setPreferredSize(new Dimension(120, 23));
		component.setText("JLabel");
	}

}
