/**
 * 
 */
package net.sourceforge.jruntimedesigner.widgets.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import net.sourceforge.jruntimedesigner.JRuntimeDesigner;

/**
 * @author wolcen
 *
 */
public class JTabbedPaneWidgetProvider extends SwingContainerWidgetProvider<JTabbedPane> {

	@Override
	protected void initialize(SwingContainerWidget<JTabbedPane> widget) {
		super.initialize(widget);
	}

	@Override
	public JMenu createSpecializedMenu(SwingContainerWidget<JTabbedPane> widget) {
		MyActionListener listener = new MyActionListener(widget);
		JMenu menu = new JMenu("Specialized");
		JMenuItem jmi = new JMenuItem("New TabPage");
		jmi.addActionListener(listener);
		jmi.setActionCommand("new_tabpage");
		menu.add(jmi);
		JTabbedPane jtp = (JTabbedPane)widget.getComponent();
		for(int i = 0; i < jtp.getTabCount(); i++){
			String title = jtp.getTitleAt(i);
			JRuntimeDesigner jrd = (JRuntimeDesigner)jtp.getComponentAt(i);
			jmi = new JMenuItem("Remove "+title);
			jmi.addActionListener(listener);
			jmi.setActionCommand(jrd.getLayoutName());
			menu.add(jmi);
		}
		return menu;
	}

	@Override
	public void doSpecializedAction(SwingContainerWidget<JTabbedPane> widget,String command) {
		JTabbedPane jtp = (JTabbedPane)widget.getComponent();
		if("new_tabpage".equals(command)){
			int index = widget.getNextEditorIndex();
			String layoutName = "TabPage_"+index;
			JRuntimeDesigner editor = widget.createEditor(layoutName);
			jtp.add(layoutName, editor);
		}else{
			JRuntimeDesigner jrd = widget.getEditor(command);
			jtp.remove(jrd);
		}
	}
	
	class MyActionListener implements ActionListener{
		private SwingContainerWidget<JTabbedPane> widget;
		
		public MyActionListener(SwingContainerWidget<JTabbedPane> widget) {
			this.widget = widget;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			widget.doSpecializedAction(e.getActionCommand());
		}
		
	}

}
