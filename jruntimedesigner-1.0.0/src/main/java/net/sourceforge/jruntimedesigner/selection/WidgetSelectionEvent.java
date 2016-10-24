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

import java.util.EventObject;


/**
 * Event object propagated to the {@link IWidgetSelectionListener}
 * 
 * @author ikunin
 * @author $Author: ikunin $ (Last change)
 * @version $Revision: 10788 $ $Date: 2007-08-21 14:54:33 +0200 (Di, 21 Aug 2007) $
 * @since 1.0
 */
@SuppressWarnings("serial")
public class WidgetSelectionEvent extends EventObject {
  public WidgetSelectionEvent(WidgetSelectionManager source) {
    super(source);
  }

  public WidgetSelectionManager getSelectionManager() {
    return (WidgetSelectionManager) getSource();
  }

  /**
   * @return true if at least on widget is selected.
   */
  public boolean hasSelection() {
    return getSelectionManager().hasSelection();
  }
  

  /**
   * @return true if at least on widget is selected.
   */
  public int getSelectedWidgetCount() {
    return getSelectionManager().getSelectedWidgets().size();
  }

}
