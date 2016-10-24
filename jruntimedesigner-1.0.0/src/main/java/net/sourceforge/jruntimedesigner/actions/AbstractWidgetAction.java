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

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import net.sourceforge.jruntimedesigner.JRuntimeDesigner;
import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.JRuntimeDesignerModel;
import net.sourceforge.jruntimedesigner.events.IPanelStateListener;
import net.sourceforge.jruntimedesigner.events.PanelStateEvent;
import net.sourceforge.jruntimedesigner.selection.WidgetSelectionManager;
import net.sourceforge.jruntimedesigner.utils.ActionUtilities;
import net.sourceforge.jruntimedesigner.utils.ResourceUtils;


public abstract class AbstractWidgetAction extends AbstractGuiAction {
  protected static final ResourceBundle actionsRes = ResourceUtils.getActionsBundle(AbstractWidgetAction.class);
  protected static final ResourceBundle res = ResourceUtils.getBundle(JRuntimeDesigner.class);
  protected JRuntimeDesignerController controller;
  protected JRuntimeDesigner panel;
  protected JRuntimeDesignerModel model;
  protected WidgetSelectionManager selectionManager;

  public AbstractWidgetAction(String actionName, JRuntimeDesignerController controller) {
    super(actionName);
    this.controller = controller;
    panel = controller.getPanel();
    model = controller.getModel();
    selectionManager = controller.getSelectionManager();
    ActionUtilities.actionConfigure(this, actionsRes, actionName);
    controller.addPanelStateListener(new IPanelStateListener() {
      public void panelActivated(PanelStateEvent event) {
        updateActionState();
      }
      public void panelDeactivated(PanelStateEvent event) {
        updateActionState();
      }
    });
  }
  
  protected void updateActionState() {
    setEnabled(controller.isDesignMode());
  }

  protected boolean confirmAction(String msg) {
    return JOptionPane.showConfirmDialog(null, msg, res.getString("confirmation.Title"),
        JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
  }

  protected void showErrorMsg(String msg) {
    JOptionPane.showMessageDialog(null, msg, res.getString("error.Title"), JOptionPane.OK_OPTION);
  }

}
