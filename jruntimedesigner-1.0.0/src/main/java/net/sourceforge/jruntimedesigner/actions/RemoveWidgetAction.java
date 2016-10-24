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

import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.common.IWidgetHolder;
import net.sourceforge.jruntimedesigner.common.WidgetHolder;
import net.sourceforge.jruntimedesigner.selection.IWidgetSelectionListener;
import net.sourceforge.jruntimedesigner.selection.WidgetSelectionEvent;


public class RemoveWidgetAction extends AbstractWidgetAction {
  public static final String NAME = "Remove";
  private WidgetHolder widgetHolder;

  public RemoveWidgetAction(WidgetHolder widgetHolder) {
    super(NAME, widgetHolder.getController());
    this.widgetHolder = widgetHolder;
    updateActionState();
  }

  public RemoveWidgetAction(final JRuntimeDesignerController controller) {
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
    String msg = "";
    if (widgetHolder == null) {
      msg = res.getString("removeSelectedWidgetsAction.confirmation");
    }
    else {
      if (!widgetHolder.isSelected()) {
        msg = res.getString("removeWidgetAction.confirmation");
      }
      else {
        msg = res.getString("removeSelectedWidgetsAction.confirmation");
      }
    }
    if (!confirmAction(msg)) {
      return;
    }

    if (widgetHolder != null) {
      controller.removeWidget(widgetHolder.getWidget());
    }

    if ((widgetHolder == null || widgetHolder.isSelected())
        && selectionManager.hasSelection()) {
      IWidgetHolder[] selectedWidgets = selectionManager.getSelectedWidgetsAsArray();
      for (int i = 0; i < selectedWidgets.length; i++) {
        IWidgetHolder selectedWidget = selectedWidgets[i];
        controller.removeWidget(selectedWidget.getWidget());
      }
    }
    panel.repaint();
    controller.setDirty();
  }
}
