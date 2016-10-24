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

import javax.swing.ImageIcon;

import net.sourceforge.jruntimedesigner.utils.XmlException;
import net.sourceforge.jruntimedesigner.utils.XmlUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Abstract implementation of the widget provider supporting the storing and
 * loading of all design properties of any widget as XML.
 * 
 * @author Igor
 * @author $Author: ikunin $ (Last change)
 * @version $Revision: 10879 $ $Date: 2007-08-24 22:57:37 +0200 (Fr, 24 Aug 2007) $
 * @since 1.0
 */
public abstract class AbstractWidgetProvider implements IWidgetProvider {
  /**
   * @see net.sourceforge.jruntimedesigner.widgets.IWidgetProvider#getName()
   */
  public String getName() {
    return getType();
  }

  /**
   * @see net.sourceforge.jruntimedesigner.widgets.IWidgetProvider#getIcon()
   */
  public ImageIcon getIcon() {
    return null;
  }

  /**
   * @see net.sourceforge.jruntimedesigner.widgets.IWidgetProvider#initWidget(net.sourceforge.jruntimedesigner.widgets.IWidget)
   */
  public void initWidget(IWidget widget) {
  }

  /**
   * @see net.sourceforge.jruntimedesigner.widgets.IWidgetProvider#fromXML(org.w3c.dom.Element)
   */
  public IWidget fromXML(Element element) throws XmlException {
    IWidget widget = createWidget();
    widget.setId(element.getAttribute("id"));
    Integer layer = XmlUtils.getAttrInteger(element, "layer", true);
    widget.setLayer(layer != null ? layer.intValue() : 0);
    int width = XmlUtils.getAttrInteger(element, "width", false).intValue();
    int height = XmlUtils.getAttrInteger(element, "height", false).intValue();
    widget.setSize(new Dimension(width, height));
    int x = XmlUtils.getAttrInteger(element, "x", false).intValue();
    int y = XmlUtils.getAttrInteger(element, "y", false).intValue();
    widget.setLocation(new Point(x, y));
    widget.setLocked("1".equals(element.getAttribute("locked")));
    return widget;
  }

  /**
   * @see net.sourceforge.jruntimedesigner.widgets.IWidgetProvider#toXML(org.w3c.dom.Document,
   *      net.sourceforge.jruntimedesigner.widgets.IWidget)
   */
  public Element toXML(Document doc, IWidget widget) {
    Element element = doc.createElement("Widget");
    element.setAttribute("type", getType());
    element.setAttribute("id", widget.getId());
    Point location = widget.getLocation();
    Dimension dim = widget.getSize();
    element.setAttribute("layer", Integer.toString(widget.getLayer()));
    element.setAttribute("x", Integer.toString(location.x));
    element.setAttribute("y", Integer.toString(location.y));
    element.setAttribute("width", Integer.toString(dim.width));
    element.setAttribute("height", Integer.toString(dim.height));
    element.setAttribute("locked", widget.isLocked() ? "1" : "0");
    return element;
  }
}
