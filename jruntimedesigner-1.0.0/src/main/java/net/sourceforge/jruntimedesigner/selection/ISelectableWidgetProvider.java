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

import java.util.Iterator;

import net.sourceforge.jruntimedesigner.common.IWidgetHolder;


/**
 * Provides the <link {@link WidgetSelectionManager} with widgets <@link
 * {@link IWidgetHolder> available for the selection.
 * 
 * @author ikunin
 * @author $Author: $ (Last change)
 * @version $Revision: 1 $ $Date: $
 * @since 1.0
 */
public interface ISelectableWidgetProvider {
  /**
   * Provides the <link {@link WidgetSelectionManager} with widgets <@link
   * {@link IWidgetHolder> available for the selection.
   * 
   * @return
   */
  public Iterator<IWidgetHolder> getSelectableWidgets();
}
