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

import java.awt.Rectangle;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import net.sourceforge.jruntimedesigner.common.WidgetHolder;


@SuppressWarnings("serial")
public class ResizeWidgetUndoableEdit extends AbstractUndoableEdit {
  private UndoRedoProgress progress;
  private WidgetHolder widgetHolder;
  private Rectangle oldBounds;
  private Rectangle newBounds;
  private int dx;
  private int dy;

  public ResizeWidgetUndoableEdit(UndoRedoProgress progress, WidgetHolder widgetHolder, int dx,
      int dy) {
    this(progress, widgetHolder, dx, dy, null, null);
  }

  public ResizeWidgetUndoableEdit(UndoRedoProgress progress, WidgetHolder widgetHolder,
      Rectangle oldBounds, Rectangle newBounds) {
    this(progress, widgetHolder, 0, 0, oldBounds, newBounds);
  }

  private ResizeWidgetUndoableEdit(UndoRedoProgress progress, WidgetHolder widgetHolder, int dx,
      int dy, Rectangle oldBounds, Rectangle newBounds) {
    this.progress = progress;
    this.widgetHolder = widgetHolder;
    this.oldBounds = oldBounds;
    this.newBounds = newBounds;
    this.dx = dx;
    this.dy = dy;
  }

  public void undo() throws CannotUndoException {
    progress.start();
    try {
      super.undo();
      if (oldBounds != null && newBounds != null) {
        widgetHolder.resizeWidget(oldBounds);
      }
      else {
        widgetHolder.resizeWidget(-dx, -dy);
      }
    }
    finally {
      progress.stop();
    }
  }

  public void redo() throws CannotRedoException {
    progress.start();
    try {
      super.redo();
      if (oldBounds != null && newBounds != null) {
        widgetHolder.resizeWidget(newBounds);
      }
      else {
        widgetHolder.resizeWidget(dx, dy);
      }
    }
    finally {
      progress.stop();
    }
  }

}
