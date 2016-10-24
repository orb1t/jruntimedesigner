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
package net.sourceforge.jruntimedesigner.actions;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;

import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.common.WidgetHolder;
import net.sourceforge.jruntimedesigner.editor.WidgetPropertiesDialog;
import net.sourceforge.jruntimedesigner.widgets.IWidget;


@SuppressWarnings("serial")
public class EditWidgetAction extends AbstractWidgetAction {
  public static final String NAME = "Edit";
  private WidgetHolder widgetHolder;

  public EditWidgetAction(WidgetHolder widgetHolder) {
    super(NAME, widgetHolder.getController());
    this.widgetHolder = widgetHolder;
    updateActionState();
  }

  public void doAction(ActionEvent e) throws Exception {
    try {
      JRuntimeDesignerController.setEditingWidget(true);
      IWidget widget = widgetHolder.getWidget();
	  String title = res.getString("widgetEditor.Title") + " " + widget.getWidgetProvider().getType();
	  WidgetPropertiesDialog dialog = new WidgetPropertiesDialog(panel,title);
	  dialog.initialize(widgetHolder);
	  dialog.setLocationRelativeTo(SwingUtilities.getRoot(panel));
	  dialog.setVisible(true);
  
        int previousLayer = widget.getLayer();
        if (widget.getLayer() != previousLayer) {
          panel.setLayer(widgetHolder, widget.getLayer());
        }
        widgetHolder.invalidate();
        controller.setDirty();
        panel.revalidate();
        panel.repaint();
      
    }
    finally {
      JRuntimeDesignerController.setEditingWidget(false);
    }
  }
  
  static public Frame getFrame(Component c) {
	    if (c instanceof Frame || null == c)
	      return c == null ? null : (Frame) c;
	    return getFrame(c.getParent());
	  }

}
