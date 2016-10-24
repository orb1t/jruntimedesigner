/**
 * 
 */
package net.sourceforge.jruntimedesigner.widgets.swing;

import java.awt.Dimension;

import javax.swing.JCheckBox;

/**
 * @author wolcen
 *
 */
public class JCheckBoxWidgetProvider extends SwingWidgetProvider<JCheckBox> {

	@Override
	protected void initialize(JCheckBox component) {
		super.initialize(component);
		component.setPreferredSize(new Dimension(120, 23));
	}

}
