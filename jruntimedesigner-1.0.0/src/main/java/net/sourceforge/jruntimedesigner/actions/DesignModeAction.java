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


public class DesignModeAction extends AbstractWidgetAction {
  public static final String NAME = "Design"; 
  
  public DesignModeAction(JRuntimeDesignerController controller) {
    super(NAME, controller);
    updateActionState();
  }
  
  protected void updateActionState() {
    setEnabled(!controller.isDesignMode());
  }

  public void doAction(ActionEvent e) throws Exception {
    controller.setDesignMode(true);
  }

}
