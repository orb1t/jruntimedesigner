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

import javax.swing.ImageIcon;

import net.sourceforge.jruntimedesigner.utils.XmlException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public interface IWidgetProvider {
	 public static final String PROVIDER_CANVAS = "Canvas";
	 public static final String PROVIDER_WIDGET = "Widget";
  /**
   * Name of the Widget to show
   * 
   * @return
   */
  public String getName();

  /**
   * Widget type to use internally by storing and restoring the widgets
   * 
   * @return
   */
  public String getType();

  /**
   * Optional icon for a widget
   * 
   * @return
   */
  public ImageIcon getIcon();

  /**
   * Create a new Widget component.
   * 
   * @return
   */
  public IWidget createWidget();

  /**
   * Can be used to initialize the widget after the creation.
   * 
   * @param widget
   */
  public void initWidget(IWidget widget);

  /**
   * Serialize the widget to XML element.
   * 
   * @param doc
   * @param comp
   * @return
   */
  public Element toXML(Document doc, IWidget comp);

  /**
   * Restore the widget from XML element.
   * 
   * @param element
   * @return
   * @throws XmlException
   */
  public IWidget fromXML(Element element) throws XmlException;
}
