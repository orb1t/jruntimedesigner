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

import java.util.EventObject;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class WidgetStateEvent extends EventObject {
  private String Id;
  private JComponent component;

  public WidgetStateEvent(Object source, JComponent component, String Id) {
    super(source);
    this.component = component;
    this.Id = Id;
  }

  public JComponent getComponent() {
    return component;
  }

  public String getId() {
    return Id;
  }
}
