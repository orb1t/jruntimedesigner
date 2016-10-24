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
package net.sourceforge.jruntimedesigner.utils;

import java.io.Serializable;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

public abstract class AbstractModel implements IModel, Serializable {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -3470925785682574502L;

  /** List of listeners */
  protected EventListenerList listenerList = new EventListenerList();

  private boolean valueIsAdjusting;

  /**
   * Adds a listener to the list that's notified each time a change to the data
   * model occurs.
   *
   * @param l
   *          the ModelListener
   */
  public void addModelListener(ModelListener l) {
    listenerList.add(ModelListener.class, l);
  }

  /**
   * Removes a listener from the list that's notified each time a change to the
   * data model occurs.
   *
   * @param l
   *          the ModelListener
   */
  public void removeModelListener(ModelListener l) {
    listenerList.remove(ModelListener.class, l);
  }

  /**
   * Returns an array of all the table model listeners registered on this model.
   *
   * @return all of this model's <code>ModelListener</code>s or an empty
   *         array if no model listeners are currently registered
   *
   * @see #addModelListener
   * @see #removeModelListener
   */
  public ModelListener[] getModelListeners() {
    return (ModelListener[]) listenerList.getListeners(ModelListener.class);
  }

  /**
   * Notifies all listeners that the model data has changed. If model is adjusting
   * its values, notification doesn't occur.
   * 
   *
   * @see ModelEvent
   * @see EventListenerList
   */
  protected synchronized void fireModelDataChanged() {
      if (valueIsAdjusting) {
          return;
      }
      
      if (!SwingUtilities.isEventDispatchThread()) {
          SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                  fireModelDataChangedInternal();
              }
          });
      }
      else {
          fireModelDataChangedInternal();
      }
  }

  private void fireModelDataChangedInternal() {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ModelListener.class) {
        ((ModelListener) listeners[i + 1]).modelDataChanged(new ModelEvent(this));
      }
    }
  }
  
  /**
     * Indicates that changes to the values of the model should be considered as
     * a single event. This attribute will be set to true at the start of a
     * series of changes to the model, and will be set to false when the value
     * has finished changing.
     * 
     * @param b
     */
    protected void setValueIsAdjusting(boolean valueIsAdjusting) {
        this.valueIsAdjusting = valueIsAdjusting;
    }
  
  /**
     * Returns <code>true</code> indicating that changes to the model should
     * be considered as a single event, otherwise <code>false</code>.
     * 
     * @return
     */
    public boolean getValueIsAdjusting() {
        return this.valueIsAdjusting;
    }
  
}
