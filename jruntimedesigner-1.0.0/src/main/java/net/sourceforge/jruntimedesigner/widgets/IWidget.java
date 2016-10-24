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

import javax.swing.JComponent;
import javax.swing.JMenu;


/**
 * Every widget that is to be used inside of the runtime designer have to
 * implement this interface.
 * 
 * @author ikunin
 * @since 1.0
 */
public interface IWidget {
  public static final String PROPERTY_DESIGNMODE = "designMode";
  public static final String PROPERTY_LAYER = "layer";
  public static final String PROPERTY_LOCKED = "locked";

  /**
   * Returns the widget provider by which the widget was created.
   * 
   * @return
   */
  public IWidgetProvider getWidgetProvider();


  /**
   * Gets the assigned unique Id.
   * 
   * @return
   */
  public String getId();

  /**
   * Set the unique Id.
   * 
   * @param Id
   */
  public void setId(String Id);

  /**
   * Returns the current location of the widget inside of the runtime designer.
   * 
   * @return
   */
  public Point getLocation();

  /**
   * Sets the location of the widget.
   * 
   * @param point
   */
  public void setLocation(Point point);

  /**
   * Returns the widget's size.
   * 
   * @return
   */
  public Dimension getSize();

  /**
   * Sets the widget's size.
   * 
   * @param size
   */
  public void setSize(Dimension size);

  /**
   * Returns the assigned layer the widget should appear in the runtime
   * designer.
   * 
   * @return
   */
  public int getLayer();

  /**
   * Sets the layer the widget should appear in the runtime designer.
   * 
   * @param layer
   */
  public void setLayer(int layer);

  /**
   * Returns the actual widget component that is to be shown in the designer.
   * 
   * @return
   */
  public JComponent getComponent();

  /**
   * Returns true if the widget supports resizing within the designer.
   * 
   * @return
   */
  public boolean isResizable();

  /**
   * Controls the state whether the widget should be resizable in the runtime
   * designer.
   * 
   * @param value
   */
  public void setResizable(boolean value);

  /**
   * Returns true if the widget is locked.
   * 
   * @return
   */
  public boolean isLocked();

  /**
   * Controls the state whether the widget as locked.
   * 
   * @param value
   */
  public void setLocked(boolean value);

  /**
   * Returns true if widget is in design mode.
   * 
   * @return
   */
  public boolean isDesignMode();

  /**
   * Controls the state whether the widget is in design mode.
   * 
   * @param value
   */
  public void setDesignMode(boolean value);

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
  public void addPropertyChangeListener(PropertyChangeListener listener);

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
  public void removePropertyChangeListener(PropertyChangeListener listener);

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

  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

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

  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

  public JMenu createSpecializedMenu();
  
  public void doSpecializedAction(String command);
  
}
