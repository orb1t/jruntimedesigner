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
import net.sourceforge.jruntimedesigner.events.IPanelStateListener;
import net.sourceforge.jruntimedesigner.events.PanelStateEvent;
import net.sourceforge.jruntimedesigner.utils.ActionUtilities;


@SuppressWarnings("serial")
public class ToggleModeAction extends AbstractWidgetAction {
  public static final String NAME = "ToggleMode"; 
  public ToggleModeAction(JRuntimeDesignerController controller) {
    super(NAME, controller);
    controller.addPanelStateListener(new IPanelStateListener() {
      public void panelActivated(PanelStateEvent event) {
        configureAction();
      }
      public void panelDeactivated(PanelStateEvent event) {
        configureAction();
      }
    });  
    configureAction();
  }
  
  protected void updateActionState() {
    setEnabled(controller.isDesignModeSupported());
  }

  private void configureAction() {
    if(controller.isDesignMode()) {
      ActionUtilities.actionConfigure(this, actionsRes, PreviewModeAction.NAME);
      setEnabled(true);
    }
    else {
      ActionUtilities.actionConfigure(this, actionsRes, DesignModeAction.NAME);  
      setEnabled(controller.isDesignModeSupported());
    }
  }
  
  public void doAction(ActionEvent e) throws Exception {
    controller.setDesignMode(!controller.isDesignMode());
  }

}
