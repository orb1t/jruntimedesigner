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

import java.util.List;

import javax.swing.JMenu;

import net.sourceforge.jruntimedesigner.widgets.IWidget;


/**
 * 
 * @author ikunin
 * @author $Author: ikunin $ (Last change)
 * @version $Revision: 10527 $ $Date: 2007-08-07 14:21:43 +0200 (Di, 07 Aug 2007) $
 * @since 1.0
 */
public interface IWidgetContainer {
  public List<IWidget> getWidgets();
  public JMenu createWidgetsMenu();
}
