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

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class DetachableMouseMotionListener implements MouseMotionListener{
  private IListenerActivator activator;
  private MouseMotionListener listener;

  public DetachableMouseMotionListener(MouseMotionListener listener, IListenerActivator activator) {
    if(listener == null) {
      throw new IllegalArgumentException("Mandatory parameter listener is null!");
    }

    if(activator == null) {
      throw new IllegalArgumentException("Mandatory parameter activator is null!");
    }

    this.activator = activator;
    this.listener = listener;
  }

  public void mouseDragged(MouseEvent e) {
    if(activator.isListenerEnabled()) {
      listener.mouseDragged(e);
    }
  }

  public void mouseMoved(MouseEvent e) {
    if(activator.isListenerEnabled()) {
      listener.mouseMoved(e);
    }
  }
}
