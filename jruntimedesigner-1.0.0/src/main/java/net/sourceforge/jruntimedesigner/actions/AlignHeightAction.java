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

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.common.IWidgetHolder;
import net.sourceforge.jruntimedesigner.selection.IWidgetSelectionListener;
import net.sourceforge.jruntimedesigner.selection.WidgetSelectionEvent;


public class AlignHeightAction extends AbstractWidgetAction {
  public static final String NAME = "AlignHeight"; 
  
  public AlignHeightAction(final JRuntimeDesignerController controller) {
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
    Dimension guideSize = guideWidget.getWidgetSize();
    for(IWidgetHolder widget : selectionManager.getSelectedWidgets()) {
      if (widget == guideWidget) {
        continue;
      }
      if(!widget.isResizable()) {
        continue;
      }
      widget.resizeWidget(new Dimension(widget.getWidgetSize().width, guideSize.height));
    }
    controller.setDirty();
  }

}
