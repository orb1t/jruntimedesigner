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

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import net.sourceforge.jruntimedesigner.common.IWidgetHolder;
import net.sourceforge.jruntimedesigner.selection.WidgetSelectionManager;


@SuppressWarnings("serial")
public class DeselectWidgetUndoableEdit extends AbstractUndoableEdit {
  private UndoRedoProgress progress;
  private WidgetSelectionManager selectionManager;
  private IWidgetHolder widget;

  public DeselectWidgetUndoableEdit(UndoRedoProgress progress,
      WidgetSelectionManager selectionManager, IWidgetHolder widget) {
    this.progress = progress;
    this.selectionManager = selectionManager;
    this.widget = widget;
  }

  public void undo() throws CannotUndoException {
    progress.start();
    try {
      super.undo();
      selectionManager.selectWidget(widget);
    }
    finally {
      progress.stop();
    }
  }

  public void redo() throws CannotRedoException {
    progress.start();
    try {
      super.redo();
      selectionManager.deselectWidget(widget);
    }
    finally {
      progress.stop();
    }

  }

}
