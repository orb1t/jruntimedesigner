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
package net.sourceforge.jruntimedesigner.undo;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;

/**
 * Used to simplify the way of implementing the undoable model interface.
 * @author ikunin
 * @author $Author:  $ (Last change)
 * @version $Revision: 1 $ $Date:  $
 * @since 1.0
 */
public class UndoableModelSupport {
  private Vector<UndoableEditListener> listeners = new Vector<UndoableEditListener>();

  public void addUndoableEditListener(UndoableEditListener listener) {
    listeners.add(listener);
  }

  public void removeUndoableEditListener(UndoableEditListener listener) {
    listeners.remove(listener);
  }

  @SuppressWarnings("unchecked")
  public void fireUndoableEditHappened(UndoableEdit edit) {
    UndoableEditEvent event = new UndoableEditEvent(this, edit);
    Iterator<UndoableEditListener> iter = ((List<UndoableEditListener>) listeners.clone()).iterator();
    while (iter.hasNext())
      iter.next().undoableEditHappened(event);
  }
}
