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

import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;

/**
 * @author Santhosh Kumar T
 */
@SuppressWarnings("serial")
public class SmartUndoManager extends BaseUndoManager implements Runnable {
  private UndoableEdit edit;

  public void undoableEditHappened(UndoableEditEvent event) {
    if (edit == null) {
      edit = event.getEdit();
      SwingUtilities.invokeLater(this);
    }
    else if (!edit.addEdit(event.getEdit())) {
      CompoundEdit compoundEdit = new CompoundEdit();
      compoundEdit.addEdit(edit);
      compoundEdit.addEdit(event.getEdit());
      edit = compoundEdit;
    }
  }

  public void run() {
    if (edit instanceof CompoundEdit) {
      CompoundEdit compoundEdit = (CompoundEdit) edit;
      if (compoundEdit.isInProgress())
        compoundEdit.end();
    }
    super.undoableEditHappened(new UndoableEditEvent(this, edit));
    edit = null;
  }
}
