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

import net.sourceforge.jruntimedesigner.JRuntimeDesigner;


@SuppressWarnings("serial")
public class PanelStateEvent extends EventObject {
  private JRuntimeDesigner panel;
  private boolean isDesignMode;

  public PanelStateEvent(JRuntimeDesigner source, boolean isDesignMode) {
    super(source);
    this.panel = source;
    this.isDesignMode = isDesignMode;
  }

  public final JRuntimeDesigner getPanel() {
    return panel;
  }

  public boolean isDesignMode() {
    return isDesignMode;
  }
}
