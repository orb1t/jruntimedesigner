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

import java.util.EventObject;

import javax.swing.undo.UndoManager;

@SuppressWarnings("serial")
public class UndoStateEvent extends EventObject {
  public UndoStateEvent(UndoManager undoManager) {
    super(undoManager);
  }
  
  public UndoManager getUndoManager() {
    return (UndoManager)getSource();
  }

}
