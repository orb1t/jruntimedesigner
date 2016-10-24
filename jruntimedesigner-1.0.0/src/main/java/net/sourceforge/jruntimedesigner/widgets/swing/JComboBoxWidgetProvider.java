/**
 * 
 */
package net.sourceforge.jruntimedesigner.widgets.swing;

import java.awt.Dimension;

import javax.swing.JComboBox;

/**
 * @author wolcen
 *
 */
public class JComboBoxWidgetProvider extends SwingWidgetProvider<JComboBox> {

	@Override
	protected void initialize(JComboBox component) {
		super.initialize(component);
		component.setPreferredSize(new Dimension(120, 23));
	}

}
