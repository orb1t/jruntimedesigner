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
package net.sourceforge.jruntimedesigner.events;

/**
 * Controls the state of listeners.
 * @author ikunin
 * @author $Author: ikunin $ (Last change)
 * @version $Revision: 9858 $ $Date: 2007-06-07 10:17:40 +0200 (Do, 07 Jun 2007) $
 * @since 1.0
 */
public interface IListenerActivator {
  /**
   * the listeners will be enabled if the the returned result is true
   * @return
   */
  public boolean isListenerEnabled();
}
