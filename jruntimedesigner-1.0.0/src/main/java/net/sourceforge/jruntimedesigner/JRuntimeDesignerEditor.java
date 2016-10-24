/*******************************************************************************
 * Copyright (c) 2008 Igor Kunin.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Igor Kunin (ikunin) - initial API and implementation
 ******************************************************************************/
package net.sourceforge.jruntimedesigner;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.w3c.dom.Document;

import net.sourceforge.jruntimedesigner.common.SyntaxEditor;
import net.sourceforge.jruntimedesigner.utils.ScrollPaneBirdViewEnabler;
import net.sourceforge.jruntimedesigner.utils.XmlException;
import net.sourceforge.jruntimedesigner.utils.XmlUtils;

/**
 * Main editor for a runtime designer.
 * 
 * @author ikunin
 * @since 1.0
 */
public class JRuntimeDesignerEditor extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JRuntimeDesigner designer;
	private JScrollPane scrollpane;
	private JToolBar toolbar;
	private SyntaxEditor syntaxEditor;
	private String lastXML;


	public JRuntimeDesignerEditor() {
		initLayout();
		init();
	}

	private void initLayout() {
		setLayout(new BorderLayout());
		add(getToolbar(), BorderLayout.NORTH);
		add(getCenterPanle(), BorderLayout.CENTER);
	}

	public boolean hasChanges() {
		return designer.hasChanges();
	}

	public void init() {
		designer.setShowGrid(true);
		designer.setDesignModeSupported(true);
		designer.setDesignMode(true);
	}

	public void load(InputStream is) throws XmlException {
		designer.load(is);
	}

	private JToolBar getToolbar() {
		if (toolbar == null) {
			toolbar = new JToolBar();
			Action[] toolbarActions = getDesigner().getToolbarActions();
			for (int i = 0; i < toolbarActions.length; i++) {
				Action action = toolbarActions[i];
				if (action != null) {
					toolbar.add(action);
				} else {
					toolbar.addSeparator();
				}
			}
		}
		return toolbar;
	}

	private JPanel getCenterPanle() {
		JPanel pc = new JPanel();
		pc.setLayout(new BorderLayout());
		JTabbedPane jtabbed = new JTabbedPane(SwingConstants.BOTTOM);
		jtabbed.addTab("Desinger", getScrollPane());
		jtabbed.addTab("Source", getSyntaxEditor());
		jtabbed.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tp = (JTabbedPane) e.getSource();
				if (tp.getSelectedComponent().equals(syntaxEditor)) {
					try {
						Document doc = designer.getModel().toXML(designer.getLayoutName());
						String xml = XmlUtils.toString(doc, "utf-8");
						syntaxEditor.setText(xml);
						lastXML = syntaxEditor.getText();
					} catch (XmlException e1) {
						e1.printStackTrace();
					}
				} else if (tp.getSelectedComponent().equals(scrollpane)) {
					String xml = syntaxEditor.getText();
					if(lastXML != null && !lastXML.equals(xml) ){
						try {
							InputStream is = new ByteArrayInputStream(xml.getBytes());
							designer.load(is);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}

			}
		});
		pc.add(jtabbed, BorderLayout.CENTER);
		return pc;

	}

	private SyntaxEditor getSyntaxEditor() {
		if (syntaxEditor == null) {
			syntaxEditor = new SyntaxEditor();
		}
		return syntaxEditor;
	}


	private JScrollPane getScrollPane() {
		if (scrollpane == null) {
			scrollpane = new JScrollPane(getDesigner());
			ScrollPaneBirdViewEnabler.install(scrollpane);
		}
		return scrollpane;
	}

	public JRuntimeDesigner getDesigner() {
		if (designer == null) {
			designer = new JRuntimeDesigner();
			designer.setPreferredSize(new Dimension(1024, 768));
		}
		return designer;
	}


	public void setDesignMode(boolean isDesignMode) {
		designer.setDesignMode(isDesignMode);
	}


}
