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
package net.sourceforge.jruntimedesigner.provider;

/**
 * This exception is thrown if some problem occurs by loading or saving a
 * layout.
 * 
 * @author ikunin
 * @author $Author: ikunin $ (Last change)
 * @version $Revision: 11616 $ $Date: 2007-11-04 21:24:26 +0100 (So, 04 Nov 2007) $
 * @since 1.0
 */
@SuppressWarnings("serial")
public class LayoutDataProviderException extends Exception {

  public LayoutDataProviderException() {}

  public LayoutDataProviderException(String message) {
    super(message);
  }

  public LayoutDataProviderException(Throwable cause) {
    super(cause);
  }

  public LayoutDataProviderException(String message, Throwable cause) {
    super(message, cause);
  }

}
