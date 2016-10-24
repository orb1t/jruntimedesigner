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

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.SwingConstants;

/**
 * MySwing: Advanced Swing Utilites Copyright (C) 2005 Santhosh Kumar T <p/>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. <p/> This library is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * @author Santhosh Kumar T
 * @author ikunin
 */
public class ResizableBorder implements IResizableBorder {
  private int dist = 6;
  private boolean isResizable;
  private static final Color GUIDE_COLOR = Color.red;// new Color(4, 254, 252);
  private static final Color SELECTED_COLOR = Color.green;
  private static final Color LOCKED_COLOR = Color.GRAY;
  private static final Color LOCKED_SELECTED_COLOR = Color.BLACK;

  private static final int locations[] = {SwingConstants.NORTH, SwingConstants.SOUTH,
      SwingConstants.WEST, SwingConstants.EAST, SwingConstants.NORTH_WEST,
      SwingConstants.NORTH_EAST, SwingConstants.SOUTH_WEST, SwingConstants.SOUTH_EAST, 0, // move
      -1, // no location
  };

  private static final int cursors[] = {Cursor.N_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR,
      Cursor.W_RESIZE_CURSOR, Cursor.E_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR,
      Cursor.NE_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR,
      Cursor.MOVE_CURSOR, Cursor.DEFAULT_CURSOR,};
  private boolean isSelected;
  private boolean isGuide;
  private boolean isLocked;

  public ResizableBorder(int dist) {
    this.dist = dist;
    this.isResizable = true;
  }

  public Insets getBorderInsets(Component component) {
    return new Insets(dist, dist, dist, dist);
  }

  public boolean isBorderOpaque() {
    return false;
  }

  public void paintBorder(Component component, Graphics g, int x, int y, int w, int h) {
    //g.setColor(Color.darkGray);
    //g.drawRect(x + dist, y + dist, w - dist * 2 - 1, h - dist *2 - 1);
	g.setColor(Color.LIGHT_GRAY);
	g.drawRect(x, y, w - 1, h - 1);
	g.fillRect(x, y, w - 1, dist);
	g.fillRect(x, y + h - dist, w - 1, dist);
	g.fillRect(x, y, dist, h - 1);
	g.fillRect(x + w - dist, y, dist, h - 1);
	  

    for (int i = 0; i < locations.length - 2; i++) {
      Rectangle rect = getRectangle(x, y, w, h, locations[i]);
      if (isSelected) {
        g.setColor(isGuide ? GUIDE_COLOR : isLocked
            ? LOCKED_SELECTED_COLOR
            : SELECTED_COLOR);
      }
      else {
        g.setColor(isLocked ? LOCKED_COLOR : Color.WHITE);
      }
      g.fillRect(rect.x, rect.y, rect.width - 1, rect.height - 1);
      g.setColor(Color.BLACK);
      g.drawRect(rect.x, rect.y, rect.width - 1, rect.height - 1);
    }
  }

  private Rectangle getRectangle(int x, int y, int w, int h, int location) {
    switch (location) {
      case SwingConstants.NORTH :
        return new Rectangle(x + w / 2 - dist / 2, y, dist, dist);
      case SwingConstants.SOUTH :
        return new Rectangle(x + w / 2 - dist / 2, y + h - dist, dist, dist);
      case SwingConstants.WEST :
        return new Rectangle(x, y + h / 2 - dist / 2, dist, dist);
      case SwingConstants.EAST :
        return new Rectangle(x + w - dist, y + h / 2 - dist / 2, dist, dist);
      case SwingConstants.NORTH_WEST :
        return new Rectangle(x, y, dist, dist);
      case SwingConstants.NORTH_EAST :
        return new Rectangle(x + w - dist, y, dist, dist);
      case SwingConstants.SOUTH_WEST :
        return new Rectangle(x, y + h - dist, dist, dist);
      case SwingConstants.SOUTH_EAST :
        return new Rectangle(x + w - dist, y + h - dist, dist, dist);
    }
    return null;
  }

  public int getResizeCursor(MouseEvent me) {
    if (isLocked) {
      return Cursor.DEFAULT_CURSOR;
    }

    Component comp = me.getComponent();
    int w = comp.getWidth();
    int h = comp.getHeight();

    Rectangle bounds = new Rectangle(0, 0, w, h);

    if (!bounds.contains(me.getPoint()))
      return Cursor.DEFAULT_CURSOR;

    Rectangle actualBounds = new Rectangle(dist, dist, w - 2 * dist, h - 2 * dist);
    if (actualBounds.contains(me.getPoint()))
      return Cursor.DEFAULT_CURSOR;

    if (isResizable) {
      for (int i = 0; i < locations.length - 2; i++) {
        Rectangle rect = getRectangle(0, 0, w, h, locations[i]);
        if (rect.contains(me.getPoint()))
          return cursors[i];
      }
    }

    return Cursor.MOVE_CURSOR;
  }

  public final boolean isResizable() {
    return isResizable;
  }

  public final void setResizable(boolean isResizable) {
    this.isResizable = isResizable;
  }

  public void setSelected(boolean isSelected) {
    this.isSelected = isSelected;
  }

  public void setGuide(boolean isGuide) {
    this.isGuide = isGuide;
  }

  public void setLocked(boolean isLocked) {
    this.isLocked = isLocked;
  }

}
