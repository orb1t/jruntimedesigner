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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.SwingConstants;

/**
 * Extended version of the EqualsLayout originally written by Santhosh Kumar that
 * takes into account the minimal size of components. Used to layout the buttons
 * in the dialog. All buttons become the same size and can be alignment on the
 * left or on the right side of the dialog.
 *
 * @author Santhosh Kumar
 * @author ikunin
 */
public class EqualsLayout implements LayoutManager, SwingConstants {
  private int gap;
  private int alignment;
  private int inset;

  public EqualsLayout() {
		this(RIGHT, 10, 3);
	}
  
  /**
   * Create a new <code>EqualsLayout</code> with the given <code>gap</code>
   * between elements.
   *
   * @param gap
   */
  public EqualsLayout(int gap) {
    this(RIGHT, gap, 0);
  }

  /**
   * Create a new <code>EqualsLayout</code> with the given <code>gap</code>
   * between elements, aligned according to the parameter <code>alignment</code>
   * to the left or right side.
   *
   * @param alignment
   * @param gap
   * @param inset
   */
  public EqualsLayout(int alignment, int gap, int inset) {
    setInset(inset);
    setGap(gap);
    setAlignment(alignment);
  }

  /**
   * Return the alignment.
   *
   * @return
   */
  public int getAlignment() {
    return alignment;
  }

  /**
   * Set the alignment.
   *
   * @param alignment
   */
  public void setAlignment(int alignment) {
    this.alignment = alignment;
  }

  /**
   * Return the gap between components.
   *
   * @return
   */
  public int getGap() {
    return gap;
  }

  /**
   * Set the gap between components
   *
   * @param gap
   */
  public void setGap(int gap) {
    this.gap = gap;
  }

  /**
   * Return the inset to surrounding components.
   *
   * @return
   */
  public int getInset() {
    return inset;
  }

  /**
   * Set insets to surrounding component.
   *
   * @param inset
   */
  public void setInset(int inset) {
    this.inset = inset;
  }

  /**
   * Calculates the dimensions of the components in the container.
   *
   * @param children
   * @return
   */
  private Dimension[] dimensions(Component children[]) {
    int maxWidth = 0;
    int maxHeight = 0;
    int visibleCount = 0;
    Dimension componentPreferredSize;
    Dimension componentMinimumSize;

    for (int i = 0, c = children.length; i < c; i++) {
      if (children[i].isVisible()) {
        componentPreferredSize = children[i].getPreferredSize();
        componentMinimumSize = children[i].getMinimumSize();
        maxWidth = Math.max(maxWidth, Math.max(componentPreferredSize.width,
            componentMinimumSize.width));
        maxHeight = Math.max(maxHeight, Math.max(componentPreferredSize.height,
            componentMinimumSize.height));
        visibleCount++;
      }
    }

    int usedWidth = inset + maxWidth * visibleCount + gap * (visibleCount - 1);
    int usedHeight = inset + maxHeight;
    return new Dimension[]{new Dimension(maxWidth, maxHeight),
        new Dimension(usedWidth, usedHeight),};
  }

  /**
   * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
   */
  public void layoutContainer(Container container) {
    Insets insets = container.getInsets();

    Component[] children = container.getComponents();
    Dimension dim[] = dimensions(children);

    int maxWidth = dim[0].width;
    int maxHeight = dim[0].height;
    int usedWidth = dim[1].width;

    switch (alignment) {
      case LEFT :
      case TOP :
        for (int i = 0, c = children.length; i < c; i++) {
          if (!children[i].isVisible())
            continue;
          children[i].setBounds(inset + insets.left + (maxWidth + gap) * i, insets.top,
              maxWidth, maxHeight);
        }
        break;
      case RIGHT :
      case BOTTOM :
        for (int i = 0, c = children.length; i < c; i++) {
          if (!children[i].isVisible())
            continue;
          children[i].setBounds(container.getWidth() - insets.right - usedWidth
              + (maxWidth + gap) * i - inset, insets.top, maxWidth, maxHeight);
        }
        break;
    }
  }

  /**
   * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
   */
  public Dimension minimumLayoutSize(Container c) {
    return preferredLayoutSize(c);
  }

  /**
   * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
   */
  public Dimension preferredLayoutSize(Container container) {
    Insets insets = container.getInsets();

    Component[] children = container.getComponents();
    Dimension dim[] = dimensions(children);

    int usedWidth = dim[1].width;
    int usedHeight = dim[1].height;

    return new Dimension(insets.left + usedWidth + insets.right + 2 * inset, insets.top
        + usedHeight + insets.bottom + inset);
  }

  /**
   * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
   *      java.awt.Component)
   */
  public void addLayoutComponent(String string, Component comp) {}

  /**
   * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
   */
  public void removeLayoutComponent(Component c) {}
}
