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


public class AlignmentAction extends AbstractWidgetAction {
  public static final String NAME = "Alignment"; 
  
  public AlignmentAction(JRuntimeDesignerController controller) {
    super("Alignment", controller);
  }

  public void doAction(ActionEvent e) throws Exception {
  }

}
