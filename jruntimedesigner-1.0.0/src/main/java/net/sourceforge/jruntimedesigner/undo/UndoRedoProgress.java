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
/**
 * Used to track whether undo/redo task is in progress or not
 *
 * @author Santhosh Kumar T
 */
public class UndoRedoProgress{
    private boolean progress = false;

    public void start(){
        progress = true;
    }

    public void stop(){
        progress = false;
    }

    public boolean isInProgress(){
        return progress;
    }
}
