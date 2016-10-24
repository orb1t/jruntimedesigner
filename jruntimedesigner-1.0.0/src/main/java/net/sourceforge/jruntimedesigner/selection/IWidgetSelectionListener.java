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
package net.sourceforge.jruntimedesigner.selection;

import java.util.EventListener;

/**
 * This listener is notified if the widget selection has been changed.
 * 
 * @author ikunin
 * @author $Author: ikunin $ (Last change)
 * @version $Revision: 10787 $ $Date: 2007-08-21 14:54:05 +0200 (Di, 21 Aug 2007) $
 * @since 1.0
 */
public interface IWidgetSelectionListener extends EventListener {
  /**
   * Called if the widget selection has been changed.
   * 
   * @param event
   */
  public void widgetSelectionChanged(WidgetSelectionEvent event);
}
