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


public class LockAction extends AbstractWidgetAction {
  public static final String NAME = "Lock";
  private IWidgetHolder widgetHolder;

  public LockAction(IWidgetHolder widgetHolder) {
    super(NAME, widgetHolder.getController());
    this.widgetHolder = widgetHolder;
  }

  public LockAction(final JRuntimeDesignerController controller) {
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
      widgetHolder.getWidget().setLocked(true);
      selectionManager.deselectWidget(widgetHolder);
    }

    if (selectionManager.hasSelection()) {
      for (IWidgetHolder widget : selectionManager.getSelectedWidgets()) {
        widget.getWidget().setLocked(true);
      }
      selectionManager.resetSelection();
    }
    panel.repaint();
    controller.setDirty();
  }
}
