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
package net.sourceforge.jruntimedesigner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.sourceforge.jruntimedesigner.utils.AbstractModel;
import net.sourceforge.jruntimedesigner.utils.XmlException;
import net.sourceforge.jruntimedesigner.utils.XmlUtils;
import net.sourceforge.jruntimedesigner.widgets.IWidget;
import net.sourceforge.jruntimedesigner.widgets.IWidgetProvider;
import net.sourceforge.jruntimedesigner.widgets.WidgetProviderFactoryRegistry;


/**
 * Runtime Designer model used to load and store the layout in XML Format
 * @author ikunin
 * @since 1.0
 */
@SuppressWarnings("serial")
public class JRuntimeDesignerModel extends AbstractModel {
  private List<IWidget> widgets;
  private boolean isShowGrid;

  public JRuntimeDesignerModel() {
    this.isShowGrid = true;
    widgets = Collections.synchronizedList(new ArrayList<IWidget>());
  }

  public List<IWidget> getWidgets() {
    return widgets;
  }

  public void load(String filename) throws XmlException {
    try {
      load(new FileInputStream(filename));
    }
    catch (FileNotFoundException e) {
      throw new XmlException(e.getMessage(), e);
    }
  }

  public void load(InputStream is) throws XmlException {
    Element rootElement = XmlUtils.load(is);
    load(rootElement);
  }

  public void load(Element rootElement) throws XmlException {
    widgets.clear();
    NodeList widgetNodeList = rootElement.getElementsByTagName(IWidgetProvider.PROVIDER_WIDGET);
    for (int i = 0; i < widgetNodeList.getLength(); i++) {
      Node childElement = widgetNodeList.item(i);
      if (childElement.getNodeType() != Node.ELEMENT_NODE)
        continue;
      Node parentNode = childElement.getParentNode();
      if (parentNode != rootElement) {
        continue;
      }
      Element widgetElement = (Element) childElement;
      String type = XmlUtils.getAttrString(widgetElement, "type", true);
      try {
        IWidgetProvider widgetProvider = WidgetProviderFactoryRegistry.instance().getWidgetProvider(type);
        IWidget widget = widgetProvider.fromXML(widgetElement);
        widgetProvider.initWidget(widget);
        addWidget(widget);
      }
      catch (IllegalArgumentException ex) {
        ex.printStackTrace();
      }
    }
    fireModelDataChanged();
  }

  public void save(String filename,String layoutName) throws XmlException {
    try {
      save(new FileOutputStream(filename),layoutName);
    }
    catch (FileNotFoundException e) {
      throw new XmlException(e.getMessage(), e);
    }
  }

  public Document toXML(String layoutName) throws XmlException {
    Document doc = XmlUtils.getNewDocument();
    Element rootElement = toXML(doc,layoutName);
    doc.appendChild(rootElement);
    return doc;
  }

  public Element toXML(Document doc,String layoutName) {
    Element rootElement = doc.createElement(IWidgetProvider.PROVIDER_CANVAS);
    if(layoutName != null) rootElement.setAttribute("layoutName", layoutName);
     Iterator<IWidget> iterator = widgets.iterator();
    while (iterator.hasNext()) {
      IWidget widget = iterator.next();
      IWidgetProvider widgetProvider = widget.getWidgetProvider();
      Element widgetElement = widgetProvider.toXML(doc, widget);
      rootElement.appendChild(widgetElement);
    }
    return rootElement;
  }

  public void save(OutputStream out,String layoutName) throws XmlException {
    // System.out.println("Saving data: ");
    Document doc = toXML(layoutName);
    String xml = XmlUtils.toString(doc, null);
    if (xml != null && xml.length() != 0) {
      System.out.println(xml);
      XmlUtils.save(doc, out, null);
    }
    else {
      throw new XmlException("XML representation of the layout is broken!");
    }
  }


  public void addWidget(IWidget widget) {
    widgets.add(widget);
  }

  public void removeWidget(IWidget widget) {
    widgets.remove(widget);
  }

  public void reset() {
    widgets.clear();
    fireModelDataChanged();
  }

  public List<JComponent> getWidget(String widgetType) {
    List<JComponent> result = new ArrayList<JComponent>();
    Iterator<IWidget> iterator = widgets.iterator();
    while (iterator.hasNext()) {
      IWidget widget = iterator.next();
      if (widgetType.equals(widget.getWidgetProvider().getType())) {
        result.add(widget.getComponent());
      }
    }
    return result;
  }

  public JComponent getWidget(String widgetType, String Id) {
    Iterator<IWidget> iterator = widgets.iterator();
    while (iterator.hasNext()) {
      IWidget widget = iterator.next();
      if (widgetType.equals(widget.getWidgetProvider().getType()) && Id.equals(widget.getId())) {
        return widget.getComponent();
      }
    }
    return null;
  }

  public boolean isShowGrid() {
    return isShowGrid;
  }

  public void setShowGrid(boolean isShowGrid) {
    this.isShowGrid = isShowGrid;
  }

  public void updateUI() {
    fireModelDataChanged();
  }

}
