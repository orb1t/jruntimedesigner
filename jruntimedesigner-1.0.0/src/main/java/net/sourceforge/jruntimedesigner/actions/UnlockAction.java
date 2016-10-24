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
import net.sourceforge.jruntimedesigner.selection.IWidgetSelectionListener;
import net.sourceforge.jruntimedesigner.selection.WidgetSelectionEvent;


public class UnlockAction extends AbstractWidgetAction {
  public static final String NAME = "Unlock";
  private IWidgetHolder widgetHolder;

  public UnlockAction(IWidgetHolder widgetHolder) {
    super(NAME, widgetHolder.getController());
    this.widgetHolder = widgetHolder;
  }

  public UnlockAction(final JRuntimeDesignerController controller) {
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
      widgetHolder.getWidget().setLocked(false);
    }

    if (selectionManager.hasSelection()) {
      for(IWidgetHolder selectedWidget : selectionManager.getSelectedWidgets()) {
        selectedWidget.getWidget().setLocked(false);
      }
    }
    panel.repaint();
    controller.setDirty();
  }

}
