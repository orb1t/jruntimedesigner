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

import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.common.IWidgetHolder;
import net.sourceforge.jruntimedesigner.selection.IWidgetSelectionListener;
import net.sourceforge.jruntimedesigner.selection.WidgetSelectionEvent;


public class AlignLeftAction extends AbstractWidgetAction {
  public static final String NAME = "AlignLeft"; 
  
  public AlignLeftAction(final JRuntimeDesignerController controller) {
    super(NAME, controller);
    selectionManager.addWidgetSelectionListener(new IWidgetSelectionListener() {
      public void widgetSelectionChanged(WidgetSelectionEvent event) {
          updateActionState();
      }
    });  
    updateActionState();
  }
  
  protected void updateActionState() {
    setEnabled(controller.isDesignMode() && selectionManager.getSelectedWidgetCount() > 1);
  }

  public void doAction(ActionEvent e) throws Exception {
    if (selectionManager.getSelectedWidgets().size() < 2) {
      return;
    }
    IWidgetHolder guideWidget = selectionManager.getGuideWidget();
    if(guideWidget == null) {
      showErrorMsg(res.getString("error.selectGuideWidget"));
      return;
    }
    for(IWidgetHolder widget : selectionManager.getSelectedWidgets()) {
      if (widget == guideWidget) {
        continue;
      }
      widget.moveWidget(new Point(guideWidget.getWidgetLocation().x, widget.getWidgetLocation().y));
    }
    controller.setDirty();
  }

}
