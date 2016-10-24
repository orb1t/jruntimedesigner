/**
 * 
 */
package net.sourceforge.jruntimedesigner.widgets.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @author wolcen
 *
 */
public class JMenuBarWidgetProvider extends SwingWidgetProvider<JMenuBar> {
	@Override
	public JMenu createSpecializedMenu(JMenuBar component) {
		MyActionListener listener = new MyActionListener(component);
		JMenu menu = new JMenu("Menu Designer");
		JMenuItem jmi = new JMenuItem("Open");
		jmi.addActionListener(listener);
		jmi.setActionCommand("menu_designer");
		menu.add(jmi);
		return menu;
	}

	@Override
	public void doSpecializedAction(JMenuBar component, String command) {
		super.doSpecializedAction(component, command);
	}
	class MyActionListener implements ActionListener{
		private JMenuBar component;
		

		public MyActionListener(JMenuBar component) {
			super();
			this.component = component;
		}


		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuBarDesigner dialog = new JMenuBarDesigner(component);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}
		
	}
}
