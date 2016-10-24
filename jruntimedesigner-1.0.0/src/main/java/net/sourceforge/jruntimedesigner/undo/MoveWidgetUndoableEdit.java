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

import java.awt.Point;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import net.sourceforge.jruntimedesigner.common.WidgetHolder;


@SuppressWarnings("serial")
public class MoveWidgetUndoableEdit extends AbstractUndoableEdit {
  private UndoRedoProgress progress;
  private WidgetHolder widgetHolder;
  private Point oldLocation;
  private Point newLocation;
  private int dx;
  private int dy;
  
  public String getPresentationName() {
    return "Move Widget";
  }

  public MoveWidgetUndoableEdit(UndoRedoProgress progress, WidgetHolder widgetHolder, int dx, int dy) {
    this(progress, widgetHolder, dx, dy, null, null);
  }

  public MoveWidgetUndoableEdit(UndoRedoProgress progress, WidgetHolder widgetHolder, Point oldLocation, Point newLocation) {
    this(progress, widgetHolder, 0, 0, oldLocation, newLocation);
  }
  
  private MoveWidgetUndoableEdit(UndoRedoProgress progress, WidgetHolder widgetHolder, int dx,
      int dy, Point oldLocation, Point newLocation) {
    this.progress = progress;
    this.widgetHolder = widgetHolder;
    this.oldLocation = oldLocation;
    this.newLocation = newLocation;
    this.dx = dx;
    this.dy = dy;
  }

  public void undo() throws CannotUndoException {
    progress.start();
    try {
      super.undo();
      if (oldLocation != null && newLocation != null) {
        widgetHolder.moveWidget(oldLocation);
      }
      else {
        widgetHolder.moveSingleWidget(-dx, -dy);
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
      if (oldLocation != null && newLocation != null) {
        widgetHolder.moveWidget(newLocation);
      }
      else {
        widgetHolder.moveSingleWidget(dx, dy);
      }
    }
    finally {
      progress.stop();
    }
  }

}
