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
package net.sourceforge.jruntimedesigner.widgets;

import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JMenu;


/**
 * Base implementation of the {@link IWidget} interface.
 * 
 * @author ikunin
 * @author $Author: ikunin $ (Last change)
 * @version $Revision: 11616 $ $Date: 2007-05-29 19:15:45 +0200 (Tue, 29 May
 *          2007) $
 * @since 1.0
 */
public abstract class AbstractWidget implements IWidget {
  private String Id;
  private Point location;
  private Dimension size;
  private int layer;
  private boolean isLocked;
  private boolean isResizable = true;
  private PropertyChangeSupport propChangeSupport = new PropertyChangeSupport(this);
  private boolean isDesignMode;
  private IWidgetProvider widgetProvider;
  

  public AbstractWidget(IWidgetProvider widgetProvider) {
	super();
	this.widgetProvider = widgetProvider;
}

public String getId() {
    return Id;
  }

  public void setId(String Id) {
    this.Id = Id;
  }

  public boolean isLocked() {
    return isLocked;
  }

  public void setLocked(boolean isLocked) {
    propChangeSupport.firePropertyChange(PROPERTY_LOCKED, this.isLocked, isLocked);
    this.isLocked = isLocked;
  }

  public boolean isResizable() {
    return isResizable;
  }

  public void setResizable(boolean value) {
    this.isResizable = value;
  }

  public Point getLocation() {
    return location;
  }

  public void setLocation(Point location) {
    this.location = location;
  }

  public Dimension getSize() {
    return size;
  }

  public void setSize(Dimension size) {
    this.size = size;
  }


  /**
   * Add a PropertyChangeListener to the listener list. The listener is
   * registered for all properties. The same listener object may be added more
   * than once, and will be called as many times as it is added. If
   * <code>listener</code> is null, no exception is thrown and no action is
   * taken.
   * 
   * @param listener
   *          The PropertyChangeListener to be added
   */
  public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
    propChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * Remove a PropertyChangeListener from the listener list. This removes a
   * PropertyChangeListener that was registered for all properties. If
   * <code>listener</code> was added more than once to the same event source,
   * it will be notified one less time after being removed. If
   * <code>listener</code> is null, or was never added, no exception is thrown
   * and no action is taken.
   * 
   * @param listener
   *          The PropertyChangeListener to be removed
   */
  public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
    propChangeSupport.removePropertyChangeListener(listener);
  }

  /**
   * Add a PropertyChangeListener for a specific property. The listener will be
   * invoked only when a call on firePropertyChange names that specific
   * property. The same listener object may be added more than once. For each
   * property, the listener will be invoked the number of times it was added for
   * that property. If <code>propertyName</code> or <code>listener</code> is
   * null, no exception is thrown and no action is taken.
   * 
   * @param propertyName
   *          The name of the property to listen on.
   * @param listener
   *          The PropertyChangeListener to be added
   */

  public synchronized void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * Remove a PropertyChangeListener for a specific property. If
   * <code>listener</code> was added more than once to the same event source
   * for the specified property, it will be notified one less time after being
   * removed. If <code>propertyName</code> is null, no exception is thrown and
   * no action is taken. If <code>listener</code> is null, or was never added
   * for the specified property, no exception is thrown and no action is taken.
   * 
   * @param propertyName
   *          The name of the property that was listened on.
   * @param listener
   *          The PropertyChangeListener to be removed
   */

  public synchronized void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propChangeSupport.removePropertyChangeListener(propertyName, listener);
  }

  public boolean isDesignMode() {
    return isDesignMode;
  }

  public void setDesignMode(boolean value) {
    propChangeSupport.firePropertyChange(PROPERTY_DESIGNMODE, this.isDesignMode, value);
    this.isDesignMode = value;
  }

  public int getLayer() {
    return layer;
  }

  public void setLayer(int layer) {
    propChangeSupport.firePropertyChange(PROPERTY_LAYER, this.layer, layer);
    this.layer = layer;
  }

  public IWidgetProvider getWidgetProvider() {
    return widgetProvider;
  }
  
  public JMenu createSpecializedMenu(){
	  return null;
  }
  
  public void doSpecializedAction(String command){
	  
  }

}
