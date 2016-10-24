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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
  /**
   * An extension to default UndoManager which
   * manages undo/redo actions
   * 
   * @author Santhosh Kumar T
   */
  @SuppressWarnings("serial")
  public class BaseUndoManager extends UndoManager{
      private EventListenerList undoStateListenerList = new EventListenerList();
      
      public BaseUndoManager(){
          stateChanged();
      }
  
      /*---------------------[ Actions ]-----------------------*/
  
      private Action undoAction = new UndoAction();
      private Action redoAction = new RedoAction();
  
      public Action getUndoAction(){
          return undoAction;
      }
  
      public Action getRedoAction(){
          return redoAction;
      }
  
      private class UndoAction extends AbstractAction{
          public UndoAction(){
              super("Undo");
              putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("undo.gif")));
              putValue(Action.ACCELERATOR_KEY
                      , KeyStroke.getKeyStroke(KeyEvent.VK_Z
                      , Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
          }
  
          public void actionPerformed(ActionEvent ae){
              undo();
          }
      }
  
      private class RedoAction extends AbstractAction{
          public RedoAction(){
              super("Redo");
              putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("redo.gif")));
              putValue(Action.ACCELERATOR_KEY
                      , KeyStroke.getKeyStroke(KeyEvent.VK_Y
                      , Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
          }
  
          public void actionPerformed(ActionEvent ae){
              redo();
          }
      }
  
      /*---------------------[ StateChange ]----------------------*/
  
      public synchronized void undo() throws CannotUndoException{
          super.undo();
          stateChanged();
      }
  
      public synchronized void redo() throws CannotRedoException{
          super.redo();
          stateChanged();
      }
  
      public synchronized void discardAllEdits() {
        super.discardAllEdits();
        stateChanged();
      }
  
      public void undoableEditHappened(UndoableEditEvent undoableEditEvent){
          super.undoableEditHappened(undoableEditEvent);
          stateChanged();
      }
  
      protected void stateChanged(){
          undoAction.setEnabled(canUndo());
          redoAction.setEnabled(canRedo());
          fireUndoStateChanged();
      }

      public void addUndoStateListener(IUndoStateListener l) {
        undoStateListenerList.add(IUndoStateListener.class, l);
      }

      public void removeUndoStateListener(IUndoStateListener l) {
        undoStateListenerList.remove(IUndoStateListener.class, l);
      }

      /**
       * Notifies all listeners that the selection has changed.
       */
      private synchronized void fireUndoStateChanged() {
        if (!SwingUtilities.isEventDispatchThread()) {
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              fireUndoStateChangedInternal();
            }
          });
        }
        else {
          fireUndoStateChangedInternal();
        }
      }

      /**
       * Notifies all listeners that the selection has changed.
       */
      private void fireUndoStateChangedInternal() {
        Object[] listeners = undoStateListenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
          if (listeners[i] == IUndoStateListener.class) {
            ((IUndoStateListener) listeners[i + 1])
                .undoStateChanged(new UndoStateEvent(this));
          }
        }
      }
  }
 
