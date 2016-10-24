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

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class DetachableComponentListener implements ComponentListener {
  private IListenerActivator activator;
  private ComponentListener listener;

  public DetachableComponentListener(ComponentListener listener,
      IListenerActivator activator) {
    if (listener == null) {
      throw new IllegalArgumentException("Mandatory parameter listener is null!");
    }

    if (activator == null) {
      throw new IllegalArgumentException("Mandatory parameter activator is null!");
    }

    this.listener = listener;
    this.activator = activator;
  }

  public void componentHidden(ComponentEvent e) {
    if (activator.isListenerEnabled()) {
      listener.componentHidden(e);
    }
  }

  public void componentMoved(ComponentEvent e) {
    if (activator.isListenerEnabled()) {
      listener.componentMoved(e);
    }
  }

  public void componentResized(ComponentEvent e) {
    if (activator.isListenerEnabled()) {
      listener.componentResized(e);
    }
  }

  public void componentShown(ComponentEvent e) {
    if (activator.isListenerEnabled()) {
      listener.componentShown(e);
    }
  }
}
