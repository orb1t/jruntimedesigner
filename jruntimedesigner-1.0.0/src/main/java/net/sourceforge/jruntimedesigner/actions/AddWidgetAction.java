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
package net.sourceforge.jruntimedesigner.actions;

import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.widgets.IWidget;
import net.sourceforge.jruntimedesigner.widgets.IWidgetProvider;


/**
 * Adds a new widget to a runtime designer in a position provided by the current
 * mouse location
 * 
 * @author ikunin
 */
public class AddWidgetAction extends AbstractWidgetAction {
  public static final String NAME = "AddWidget";
  private IWidgetProvider widgetProvider;
  protected Point location;

  public AddWidgetAction(IWidgetProvider widgetProvider, JRuntimeDesignerController controller,
      Point location) {
    super(NAME, controller);
    putValue(AbstractAction.NAME, widgetProvider.getName());
    this.location = location;
    this.widgetProvider = widgetProvider;
    ImageIcon icon = widgetProvider.getIcon();
    if (icon != null && icon.getIconHeight() > 0 && icon.getIconWidth() > 0) {
      putValue(Action.SMALL_ICON, icon);
    }
    updateActionState();
  }

  public void doAction(ActionEvent e) throws Exception {
    IWidget widget = widgetProvider.createWidget();
    widgetProvider.initWidget(widget);
    controller.addWidget(widget, location);
  }
}
