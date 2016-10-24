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
/*
 * Copyright � Igor Kunin 2007-2008 
 * All rights reserved.
 */
package net.sourceforge.jruntimedesigner.common;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicMenuItemUI;

/**
 * Group menu item
 * @author ikunin
 * @author $Author:  $ (Last change)
 * @version $Revision: 1 $ $Date:  $
 * @since 1.0
 */
@SuppressWarnings("serial")
public class GroupMenuItem extends JMenuItem {
  public GroupMenuItem(final String name) {
    super(name);
    setUI(new GroupMenuItemUI());
    setEnabled(false);
  }

  public void doClick() {}

  static class GroupMenuItemUI extends BasicMenuItemUI {

    public static ComponentUI createUI(final JComponent c) {
      return new GroupMenuItemUI();
    }

    /**
     * Method which renders the text of the current menu item.
     * <p>
     * 
     * @param g
     *          Graphics context
     * @param menuItem
     *          Current menu item to render
     * @param textRect
     *          Bounding rectangle to render the text.
     * @param text
     *          String to render
     */
    protected void paintText(final Graphics g, final JMenuItem menuItem, final Rectangle textRect, final String text) {
      final FontMetrics fm = g.getFontMetrics();
      final int mnemonicIndex = menuItem.getDisplayedMnemonicIndex();
//      // W2K Feature: Check to see if the Underscore should be rendered.
//      if (WindowsLookAndFeel.isMnemonicHidden() == true) {
//        mnemonicIndex = -1;
//      }
      final Color oldColor = g.getColor();
      BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemonicIndex, textRect.x, textRect.y
          + fm.getAscent());
      g.setColor(oldColor);
    }
  }
}
