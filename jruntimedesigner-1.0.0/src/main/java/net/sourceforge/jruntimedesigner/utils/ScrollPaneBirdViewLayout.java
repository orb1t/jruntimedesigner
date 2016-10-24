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

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Support class used by the ScrollPaneSelector that adds a bird view to any
 * scroll pane activated by small button located in a bottom right corner of the
 * scroll pane.
 *
 * @author SwingLabs
 * @version $Revision: 10087 $ $Date: 2007-06-15 11:16:16 +0200 (Fr, 15 Jun 2007) $
 * @since 1.0
 */
@SuppressWarnings("serial")
class ScrollPaneBirdViewLayout extends ScrollPaneLayout {
  private void superlayoutContainer(Container parent) {
    JScrollPane scrollPane = (JScrollPane) parent;
    vsbPolicy = scrollPane.getVerticalScrollBarPolicy();
    hsbPolicy = scrollPane.getHorizontalScrollBarPolicy();
    Rectangle availR = scrollPane.getBounds();
    Insets insets = parent.getInsets();
    availR.x = insets.left;
    availR.y = insets.top;
    availR.width -= insets.left + insets.right;
    availR.height -= insets.top + insets.bottom;
    boolean leftToRight = scrollPane.getComponentOrientation().isLeftToRight();
    Rectangle colHeadR = new Rectangle(0, availR.y, 0, 0);
    if (colHead != null && colHead.isVisible()) {
      int colHeadHeight = Math.min(availR.height, colHead.getPreferredSize().height);
      colHeadR.height = colHeadHeight;
      availR.y += colHeadHeight;
      availR.height -= colHeadHeight;
    }
    Rectangle rowHeadR = new Rectangle(0, 0, 0, 0);
    if (rowHead != null && rowHead.isVisible()) {
      int rowHeadWidth = Math.min(availR.width, rowHead.getPreferredSize().width);
      rowHeadR.width = rowHeadWidth;
      availR.width -= rowHeadWidth;
      if (leftToRight) {
        rowHeadR.x = availR.x;
        availR.x += rowHeadWidth;
      }
      else {
        rowHeadR.x = availR.x + availR.width;
      }
    }
    Border viewportBorder = scrollPane.getViewportBorder();
    Insets vpbInsets;
    if (viewportBorder != null) {
      vpbInsets = viewportBorder.getBorderInsets(parent);
      availR.x += vpbInsets.left;
      availR.y += vpbInsets.top;
      availR.width -= vpbInsets.left + vpbInsets.right;
      availR.height -= vpbInsets.top + vpbInsets.bottom;
    }
    else {
      vpbInsets = new Insets(0, 0, 0, 0);
    }
    Component view = viewport == null ? null : viewport.getView();
    Dimension viewPrefSize = view == null ? new Dimension(0, 0) : view.getPreferredSize();
    Dimension extentSize = viewport == null ? new Dimension(0, 0) : viewport
        .toViewCoordinates(availR.getSize());
    boolean viewTracksViewportWidth = false;
    boolean viewTracksViewportHeight = false;
    boolean isEmpty = availR.width < 0 || availR.height < 0;
    Scrollable sv;
    if (!isEmpty && (view instanceof Scrollable)) {
      sv = (Scrollable) view;
      viewTracksViewportWidth = sv.getScrollableTracksViewportWidth();
      viewTracksViewportHeight = sv.getScrollableTracksViewportHeight();
    }
    else {
      sv = null;
    }
    Rectangle vsbR = new Rectangle(0, availR.y - vpbInsets.top, 0, 0);
    boolean vsbNeeded;
    if (isEmpty)
      vsbNeeded = false;
    else if (vsbPolicy == 22)
      vsbNeeded = true;
    else if (vsbPolicy == 21)
      vsbNeeded = false;
    else
      vsbNeeded = !viewTracksViewportHeight && viewPrefSize.height > extentSize.height;
    if (vsb != null && vsbNeeded) {
      adjustForVSB(true, availR, vsbR, vpbInsets, leftToRight);
      extentSize = viewport.toViewCoordinates(availR.getSize());
    }
    Rectangle hsbR = new Rectangle(availR.x - vpbInsets.left, 0, 0, 0);
    boolean hsbNeeded;
    if (isEmpty)
      hsbNeeded = false;
    else if (hsbPolicy == 32)
      hsbNeeded = true;
    else if (hsbPolicy == 31)
      hsbNeeded = false;
    else
      hsbNeeded = !viewTracksViewportWidth && viewPrefSize.width > extentSize.width;
    if (hsb != null && hsbNeeded) {
      adjustForHSB(true, availR, hsbR, vpbInsets);
      if (vsb != null && !vsbNeeded && vsbPolicy != 21) {
        extentSize = viewport.toViewCoordinates(availR.getSize());
        vsbNeeded = viewPrefSize.height > extentSize.height;
        if (vsbNeeded) {
          adjustForVSB(true, availR, vsbR, vpbInsets, leftToRight);
          if (!leftToRight)
            hsbR.x += vsbR.width;
        }
      }
    }
    if (viewport != null) {
      viewport.setBounds(availR);
      if (sv != null) {
        extentSize = viewport.toViewCoordinates(availR.getSize());
        boolean oldHSBNeeded = hsbNeeded;
        boolean oldVSBNeeded = vsbNeeded;
        viewTracksViewportWidth = sv.getScrollableTracksViewportWidth();
        viewTracksViewportHeight = sv.getScrollableTracksViewportHeight();
        if (vsb != null && vsbPolicy == 20) {
          boolean newVSBNeeded = !viewTracksViewportHeight
              && viewPrefSize.height > extentSize.height;
          if (newVSBNeeded != vsbNeeded) {
            vsbNeeded = newVSBNeeded;
            adjustForVSB(vsbNeeded, availR, vsbR, vpbInsets, leftToRight);
            extentSize = viewport.toViewCoordinates(availR.getSize());
          }
        }
        if (hsb != null && hsbPolicy == 30) {
          boolean newHSBbNeeded = !viewTracksViewportWidth
              && viewPrefSize.width > extentSize.width;
          if (newHSBbNeeded != hsbNeeded) {
            hsbNeeded = newHSBbNeeded;
            adjustForHSB(hsbNeeded, availR, hsbR, vpbInsets);
            if (vsb != null && !vsbNeeded && vsbPolicy != 21) {
              extentSize = viewport.toViewCoordinates(availR.getSize());
              vsbNeeded = viewPrefSize.height > extentSize.height;
              if (vsbNeeded)
                adjustForVSB(true, availR, vsbR, vpbInsets, leftToRight);
            }
          }
        }
        if (oldHSBNeeded != hsbNeeded || oldVSBNeeded != vsbNeeded)
          viewport.setBounds(availR);
      }
    }
    vsbR.height = availR.height + vpbInsets.top + vpbInsets.bottom;
    hsbR.width = availR.width + vpbInsets.left + vpbInsets.right;
    rowHeadR.height = availR.height + vpbInsets.top + vpbInsets.bottom;
    rowHeadR.y = availR.y - vpbInsets.top;
    colHeadR.width = availR.width + vpbInsets.left + vpbInsets.right;
    colHeadR.x = availR.x - vpbInsets.left;
    if (rowHead != null)
      rowHead.setBounds(rowHeadR);
    if (colHead != null)
      colHead.setBounds(colHeadR);
    if (vsb != null)
      if (vsbNeeded) {
        vsb.setVisible(true);
        vsb.setBounds(vsbR);
      }
      else {
        vsb.setVisible(false);
      }
    if (hsb != null)
      if (hsbNeeded) {
        hsb.setVisible(true);
        hsb.setBounds(hsbR);
      }
      else {
        hsb.setVisible(false);
      }
    if (lowerLeft != null)
      lowerLeft.setBounds(leftToRight ? rowHeadR.x : vsbR.x, hsbR.y, leftToRight
          ? rowHeadR.width
          : vsbR.width, hsbR.height);
    if (lowerRight != null)
      lowerRight.setBounds(leftToRight ? vsbR.x : rowHeadR.x, hsbR.y, leftToRight
          ? vsbR.width
          : rowHeadR.width, hsbR.height);
    if (upperLeft != null)
      upperLeft.setBounds(leftToRight ? rowHeadR.x : vsbR.x, colHeadR.y, leftToRight
          ? rowHeadR.width
          : vsbR.width, colHeadR.height);
    if (upperRight != null)
      upperRight.setBounds(leftToRight ? vsbR.x : rowHeadR.x, colHeadR.y, leftToRight
          ? vsbR.width
          : rowHeadR.width, colHeadR.height);
  }

  private void adjustForVSB(boolean wantsVSB, Rectangle available, Rectangle vsbR,
      Insets vpbInsets, boolean leftToRight) {
    int oldWidth = vsbR.width;
    if (wantsVSB) {
      int vsbWidth = Math.max(0, Math.min(vsb.getPreferredSize().width, available.width));
      available.width -= vsbWidth;
      vsbR.width = vsbWidth;
      if (leftToRight) {
        vsbR.x = available.x + available.width + vpbInsets.right;
      }
      else {
        vsbR.x = available.x - vpbInsets.left;
        available.x += vsbWidth;
      }
    }
    else {
      available.width += oldWidth;
    }
  }

  private void adjustForHSB(boolean wantsHSB, Rectangle available, Rectangle hsbR,
      Insets vpbInsets) {
    int oldHeight = hsbR.height;
    if (wantsHSB) {
      int hsbHeight = Math.max(0, Math.min(available.height,
          hsb.getPreferredSize().height));
      available.height -= hsbHeight;
      hsbR.y = available.y + available.height + vpbInsets.bottom;
      hsbR.height = hsbHeight;
    }
    else {
      available.height += oldHeight;
    }
  }

  public void layoutContainer(Container parent) {
    superlayoutContainer(parent);
    boolean isLeftToRight = parent.getComponentOrientation().isLeftToRight();
    if (isLeftToRight && lowerRight == null)
      return;
    if (!isLeftToRight && lowerLeft == null)
      return;
    if (vsb.isVisible() && !hsb.isVisible()) {
      Rectangle vsbBounds = vsb.getBounds();
      Dimension hsbPref = hsb.getPreferredSize();
      int delta = hsbPref.height;
      vsbBounds.height -= delta;
      vsb.setBounds(vsbBounds);
      if (isLeftToRight) {
        lowerRight.setBounds(vsbBounds.x, vsbBounds.y + vsbBounds.height,
            vsbBounds.width, delta);
        lowerRight.setVisible(true);
      }
      else {
        lowerLeft.setBounds(vsbBounds.x, vsbBounds.y + vsbBounds.height, vsbBounds.width,
            delta);
        lowerLeft.setVisible(true);
      }
    }
    if (hsb.isVisible() && !vsb.isVisible()) {
      Rectangle hsbBounds = hsb.getBounds();
      Dimension vsbPref = vsb.getPreferredSize();
      int delta = vsbPref.width;
      hsbBounds.width -= delta;
      if (!isLeftToRight)
        hsbBounds.x += delta;
      hsb.setBounds(hsbBounds);
      if (isLeftToRight) {
        lowerRight.setBounds(hsbBounds.x + hsbBounds.width, hsbBounds.y, delta,
            hsbBounds.height);
        lowerRight.setVisible(true);
      }
      else {
        lowerLeft.setBounds(hsbBounds.x - delta, hsbBounds.y, delta, hsbBounds.height);
        lowerLeft.setVisible(true);
      }
    }
  }
}
