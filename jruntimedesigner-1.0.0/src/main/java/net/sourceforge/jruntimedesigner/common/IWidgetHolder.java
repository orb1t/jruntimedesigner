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
package net.sourceforge.jruntimedesigner.common;

import java.awt.Dimension;
import java.awt.Point;

import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.widgets.IWidget;


/**
 * Runtime Designer interacts internally with widgets via IWidgetHolder interface. The
 * implementation of this interface in turn interacts with the concrete widget.
 * This concept allows to decouple the widget functionality from the
 * functionality needed by the RuntimeDesigner like making the widget movable or
 * resizable in a design mode.
 * 
 * @author ikunin
 * @author $Author: ikunin $ (Last change)
 * @version $Revision: 11616 $ $Date: 2007-11-04 21:24:26 +0100 (So, 04 Nov 2007) $
 * @since 1.0
 */
public interface IWidgetHolder {

  public Point getWidgetLocation();

  public Dimension getWidgetSize();

  public void recalcSize();

  public void moveWidget(int dx, int dy);

  public void moveSingleWidget(int dx, int dy);

  public void moveWidget(Point point);

  public void resizeWidget(int dx, int dy);

  public void resizeWidget(Dimension size);

  public boolean isDesignMode();

  public void setDesignMode(boolean isDesignMode);

  public void setSelected(boolean isSelected);

  public boolean isSelected();

  public void setGuide(boolean isGuide);

  public void setLocked(boolean isLocked);

  public boolean isGuide();

  public JRuntimeDesignerController getController();

  public IWidget getWidget();

  public boolean isLocked();

  public boolean isResizable();

  public int getLayer();

  public void setLayer(int layer);
}
