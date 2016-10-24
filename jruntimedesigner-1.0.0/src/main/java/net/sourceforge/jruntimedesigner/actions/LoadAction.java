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


/**
 * Action for loading the layout.
 * 
 * @author ikunin
 * @author $Author: ikunin $ (Last change)
 * @version $Revision: 10994 $ $Date: 2007-08-17 13:49:05 +0200 (Fr, 17 Aug
 *          2007) $
 * @since 1.0
 */
public class LoadAction extends AbstractWidgetAction {
  public static final String NAME = "Load";

  public LoadAction(JRuntimeDesignerController controller) {
    super(NAME, controller);
    updateActionState();
  }

  public void doAction(ActionEvent e) throws Exception {
    try {
      JRuntimeDesignerController.setEditingWidget(true);
      controller.load();
    }
    finally {
      JRuntimeDesignerController.setEditingWidget(false);
    }
  }
}
