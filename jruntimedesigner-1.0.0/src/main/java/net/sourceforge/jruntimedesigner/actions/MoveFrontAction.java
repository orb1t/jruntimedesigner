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

import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.common.IWidgetHolder;
import net.sourceforge.jruntimedesigner.common.WidgetHolder;
import net.sourceforge.jruntimedesigner.selection.IWidgetSelectionListener;
import net.sourceforge.jruntimedesigner.selection.WidgetSelectionEvent;


public class MoveFrontAction extends AbstractWidgetAction {
  public static final String NAME = "MoveFront";
  private WidgetHolder widgetHolder;

  public MoveFrontAction(WidgetHolder widgetHolder) {
    super(NAME, widgetHolder.getController());
    this.widgetHolder = widgetHolder;
  }

  public MoveFrontAction(final JRuntimeDesignerController controller) {
    super(NAME, controller);
    selectionManager.addWidgetSelectionListener(new IWidgetSelectionListener() {
      public void widgetSelectionChanged(WidgetSelectionEvent event) {
        updateActionState();
      }
    });
    updateActionState();
  }

  protected void updateActionState() {
    setEnabled(controller.isDesignMode()
        && (widgetHolder != null || selectionManager.hasSelection()));
  }

  public void doAction(ActionEvent e) throws Exception {
    if (widgetHolder != null) {
      int layer = widgetHolder.getLayer();
      if (layer < 100) {
        widgetHolder.setLayer(100);
        panel.setLayer(widgetHolder, 100);
      }
    }

    if ((widgetHolder == null || widgetHolder.isSelected()) && selectionManager.hasSelection()) {
      for(IWidgetHolder selectedWidget : selectionManager.getSelectedWidgets()) {
        if (widgetHolder != null && selectedWidget == widgetHolder.getWidget()) {
          continue;
        }
        int layer = selectedWidget.getLayer();
        if (layer < 100) {
          selectedWidget.setLayer(100);
          if (selectedWidget instanceof JComponent) {
            panel.setLayer((JComponent) selectedWidget, layer);
          }
        }
      }
    }
    panel.repaint();
    controller.setDirty();
  }

}
