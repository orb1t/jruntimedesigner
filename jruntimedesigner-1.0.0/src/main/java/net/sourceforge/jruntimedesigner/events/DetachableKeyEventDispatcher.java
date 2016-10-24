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
package net.sourceforge.jruntimedesigner.events;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

public class DetachableKeyEventDispatcher implements KeyEventDispatcher {
  private IListenerActivator activator;
  private KeyEventDispatcher listener;

  public DetachableKeyEventDispatcher(KeyEventDispatcher listener, IListenerActivator activator) {
    if (listener == null) {
      throw new IllegalArgumentException("Mandatory parameter listener is null!");
    }

    if (activator == null) {
      throw new IllegalArgumentException("Mandatory parameter activator is null!");
    }

    this.listener = listener;
    this.activator = activator;
  }

  public boolean dispatchKeyEvent(KeyEvent e) {
    if (activator.isListenerEnabled()) {
      return listener.dispatchKeyEvent(e);
    }
    return false;
  }

}
