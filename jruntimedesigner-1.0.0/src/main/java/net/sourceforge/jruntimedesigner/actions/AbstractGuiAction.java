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

import javax.swing.AbstractAction;
import javax.swing.JComponent;

public abstract class AbstractGuiAction extends AbstractAction {

  public AbstractGuiAction(String name) {
    super(name);
  }

  public abstract void doAction(ActionEvent e) throws Exception;

  /**
   * Calls {@link #doAction} in a try-catch-block.
   */
  public final void actionPerformed(ActionEvent event) {
    try {
      doAction(event);
    }
    catch (Exception exception) {
      if (event.getSource() != null && event.getSource() instanceof JComponent)
        handleException(exception, (JComponent) event.getSource());
      else
        handleException(exception, null);
    }
  }

  protected void handleException(Exception ex, JComponent source) {
    ex.printStackTrace();
   // ExceptionHandler.handleException(ex, source);
  }
}
