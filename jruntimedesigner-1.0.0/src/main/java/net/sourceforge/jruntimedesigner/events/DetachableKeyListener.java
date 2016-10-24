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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DetachableKeyListener implements KeyListener {
  private IListenerActivator activator;
  private KeyListener listener;

  public DetachableKeyListener(KeyListener listener, IListenerActivator activator) {
    if (listener == null) {
      throw new IllegalArgumentException("Mandatory parameter listener is null!");
    }

    if (activator == null) {
      throw new IllegalArgumentException("Mandatory parameter activator is null!");
    }

    this.listener = listener;
    this.activator = activator;
  }

  public void keyPressed(KeyEvent e) {
    if (activator.isListenerEnabled()) {
      listener.keyPressed(e);
    }
  }

  public void keyReleased(KeyEvent e) {
    if (activator.isListenerEnabled()) {
      listener.keyReleased(e);
    }
  }

  public void keyTyped(KeyEvent e) {
    if (activator.isListenerEnabled()) {
      listener.keyTyped(e);
    }
  }

}
