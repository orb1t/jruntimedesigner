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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.sourceforge.jruntimedesigner.utils.ResourceUtils;
import net.sourceforge.jruntimedesigner.utils.XmlException;

/**
 * A small demo for a runtime designer.
 * 
 * @author ikunin
 * @since 1.0
 */
public class JRuntimeDesignerStart {
	protected static final ResourceBundle res = ResourceUtils
			.getBundle(JRuntimeDesigner.class);
	private JRuntimeDesignerEditor editor;

	/*
	 * private JSGPanel sceneGraphPanel; private SGTransform.Scale scale; private
	 * Rotate rotation; private SGTransform.Translate translate;
	 */

	public JRuntimeDesignerStart() {
		initLayout();
	}

	private void initLayout() {
		final JFrame frame = new JFrame("Runtime designer");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(getDesignerEditor(), BorderLayout.CENTER);
		frame.setSize(new Dimension(800, 800));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (canClose()) {
					frame.setVisible(false);
					frame.dispose();
					System.exit(0);
				}
			}
		});
	}

	private boolean canClose() {
		if (editor != null && editor.hasChanges()) {
			if (JOptionPane.showConfirmDialog(null, res
					.getString("confirmation.discardChanges")) == JOptionPane.OK_OPTION) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}


	public void load(InputStream is) throws XmlException {
		editor.load(is);
	}


	public JRuntimeDesignerEditor getDesignerEditor() {
		if (editor == null) {
			editor = new JRuntimeDesignerEditor();
			}
		return editor;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			// ignore;
		}
		JRuntimeDesignerStart designerStarter = new JRuntimeDesignerStart();
		try {
			InputStream is = designerStarter.getClass().getResourceAsStream(
					"samples/samplelayout.xml");
			if (is != null) {
				designerStarter.load(is);
			}
		} catch (XmlException e) {
			e.printStackTrace();
		}
	}

}
