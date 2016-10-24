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
package net.sourceforge.jruntimedesigner.utils;

/**
 * @author ikunin
 */
@SuppressWarnings("serial")
public class XmlException extends Exception {

  /**
   * @param message
   */
  public XmlException(String message) {
    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public XmlException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param cause
   */
  public XmlException(Throwable cause) {
    super(cause);
  }

  public XmlException() {
    super();
  }

}
