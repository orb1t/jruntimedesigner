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

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
 * Adds a bird view to any scroll pane activated by small button located in a
 * bottom right corner of the scroll pane.
 *
 * @author SwingLabs
 * @version $Revision: 12655 $ $Date: 2007-06-15 11:17:12 +0200 (Fr, 15 Jun
 *          2007) $
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ScrollPaneBirdViewEnabler extends JComponent {

  private static final Icon LAUNCH_SELECTOR_ICON = new Icon() {

    public void paintIcon(Component c, Graphics g, int x, int y) {
      Color tmpColor = g.getColor();
      g.setColor(Color.BLACK);
      g.drawRect(2, 2, 10, 10);
      g.drawRect(4, 5, 6, 4);
      g.setColor(tmpColor);
    }

    public int getIconWidth() {
      return 15;
    }

    public int getIconHeight() {
      return 15;
    }
  };
  // private static Map theInstalledScrollPaneSelectors = new HashMap();
  private LayoutManager formerLayoutManager;
  private JScrollPane scrollPane;
  private JComponent component;
  private final JPopupMenu popupMenu;
  private final JComponent button;
  private BufferedImage image;
  private Rectangle startRectangle;
  private Rectangle rectangle;
  private Point startPoint;
  private double scale;
  private final PropertyChangeListener componentOrientationListener;
  private final ContainerAdapter viewPortViewListener;
  private Robot robot;

  public static synchronized void install(JScrollPane aScrollPane) {
    if (aScrollPane == null) {
      return;
    }
    // if (!theInstalledScrollPaneSelectors.containsKey(aScrollPane)) {
    ScrollPaneBirdViewEnabler scrollPaneBirdViewEnabler = new ScrollPaneBirdViewEnabler();
    scrollPaneBirdViewEnabler.installOnScrollPane(aScrollPane);
    // theInstalledScrollPaneSelectors.put(aScrollPane,
    // scrollPaneBirdViewEnabler);
    // }
  }

  // public static synchronized void uninstallScrollPaneSelector(JScrollPane
  // aScrollPane) {
  // if (aScrollPane == null)
  // return;
  // ScrollPaneBirdViewEnabler scrollPaneBirdViewEnabler =
  // (ScrollPaneBirdViewEnabler) theInstalledScrollPaneSelectors
  // .get(aScrollPane);
  // if (scrollPaneBirdViewEnabler == null) {
  // return;
  // }
  // else {
  // scrollPaneBirdViewEnabler.uninstallFromScrollPane();
  // theInstalledScrollPaneSelectors.remove(aScrollPane);
  // return;
  // }
  // }

  private ScrollPaneBirdViewEnabler() {
    setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    scrollPane = null;
    image = null;
    startRectangle = null;
    rectangle = null;
    startPoint = null;
    scale = 0.0D;
    try {
      robot = new Robot();
    }
    catch (AWTException ex) {
      ex.printStackTrace();
    }
    button = new JLabel(LAUNCH_SELECTOR_ICON);
    javax.swing.event.MouseInputListener mil = new MouseInputAdapter() {

      @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        SwingUtilities.convertPointToScreen(p, button);
        display(p);
      }

      @Override
    public void mouseReleased(MouseEvent e) {
        if (startPoint != null) {
          Point newPoint = e.getPoint();
          SwingUtilities.convertPointToScreen(newPoint, (Component) e.getSource());
          int deltaX = (int) ((newPoint.x - startPoint.x) / scale);
          int deltaY = (int) ((newPoint.y - startPoint.y) / scale);
          scroll(deltaX, deltaY);
        }
        startPoint = null;
        startRectangle = rectangle;
      }

      @Override
    public void mouseDragged(MouseEvent e) {
        if (startPoint == null) {
          return;
        }
        else {
          Point newPoint = e.getPoint();
          SwingUtilities.convertPointToScreen(newPoint, (Component) e.getSource());
          moveRectangle(newPoint.x - startPoint.x, newPoint.y - startPoint.y);
          return;
        }
      }
    };
    button.addMouseListener(mil);
    button.addMouseMotionListener(mil);
    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    popupMenu = new JPopupMenu();
    popupMenu.setLayout(new BorderLayout());
    popupMenu.add(this, BorderLayout.CENTER);
    componentOrientationListener = new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        if (scrollPane == null) {
          return;
        }
        else {
          scrollPane.setCorner("LOWER_LEADING_CORNER", null);
          scrollPane.setCorner("LOWER_TRAILING_CORNER", button);
          return;
        }
      }
    };
    viewPortViewListener = new ContainerAdapter() {
      @Override
    public void componentAdded(ContainerEvent e) {
        if (popupMenu.isVisible())
          popupMenu.setVisible(false);
        Component comp = scrollPane.getViewport().getView();
        component = (comp instanceof JComponent) ? (JComponent) comp : null;
      }
    };
  }

  @Override
public Dimension getPreferredSize() {
    if (image == null || rectangle == null) {
      return new Dimension();
    }
    else {
      Insets insets = getInsets();
      return new Dimension(image.getWidth(null) + insets.left + insets.right, image
          .getHeight(null)
          + insets.top + insets.bottom);
    }
  }

  @Override
protected void paintComponent(Graphics g1D) {
    if (image == null || rectangle == null) {
      return;
    }
    else {
      Graphics2D g = (Graphics2D) g1D;
      Insets insets = getInsets();
      int xOffset = insets.left;
      int yOffset = insets.top;
      int availableWidth = getWidth() - insets.left - insets.right;
      int availableHeight = getHeight() - insets.top - insets.bottom;
      g.drawImage(image, xOffset, yOffset, null);
      Color tmpColor = g.getColor();
      Area area = new Area(new Rectangle(xOffset, yOffset, availableWidth,
          availableHeight));
      area.subtract(new Area(rectangle));
      g.setColor(new Color(200, 200, 200, 128));
      g.fill(area);
      g.setColor(Color.BLACK);
      g.draw(rectangle);
      g.setColor(tmpColor);
      return;
    }
  }

  private void installOnScrollPane(JScrollPane aScrollPane) {
    if (scrollPane != null)
      uninstallFromScrollPane();
    scrollPane = aScrollPane;
    formerLayoutManager = scrollPane.getLayout();
    scrollPane.setLayout(new ScrollPaneBirdViewLayout());
    scrollPane.addPropertyChangeListener("componentOrientation",
        componentOrientationListener);
    scrollPane.getViewport().addContainerListener(viewPortViewListener);
    scrollPane.setCorner("LOWER_RIGHT_CORNER", button);
    Component comp = scrollPane.getViewport().getView();
    component = (comp instanceof JComponent) ? (JComponent) comp : null;
  }

  private void uninstallFromScrollPane() {
    if (scrollPane == null)
      return;
    if (popupMenu.isVisible()) {
      popupMenu.setVisible(false);
    }
    scrollPane.setCorner("LOWER_RIGHT_CORNER", null);
    scrollPane.removePropertyChangeListener("componentOrientation",
        componentOrientationListener);
    scrollPane.getViewport().removeContainerListener(viewPortViewListener);
    scrollPane.setLayout(formerLayoutManager);
    scrollPane = null;
  }

  private void display(Point pointOnScreen) {
    if (component == null) {
      return;
    }
    double compWidth = component.getWidth();
    double compHeight = component.getHeight();
    double scaleX = 600D / compWidth;
    double scaleY = 600D / compHeight;
    scale = Math.min(scaleX, scaleY);
    image = new BufferedImage((int) (component.getWidth() * scale),
        (int) (component.getHeight() * scale), 1);
    Graphics2D g = image.createGraphics();
    g.scale(scale, scale);
    component.paint(g);
    startRectangle = component.getVisibleRect();
    Insets insets = getInsets();
    startRectangle.x = (int) (scale * startRectangle.x + insets.left);
    startRectangle.y = (int) (scale * startRectangle.y + insets.right);
    startRectangle.width *= scale;
    startRectangle.height *= scale;
    rectangle = startRectangle;
    Dimension pref = popupMenu.getPreferredSize();
    Point buttonLocation = button.getLocationOnScreen();
    Point popupLocation = new Point((button.getWidth() - pref.width) / 2, (button
        .getHeight() - pref.height) / 2);
    Point centerPoint = new Point(buttonLocation.x + popupLocation.x + rectangle.x
        + rectangle.width / 2, buttonLocation.y + popupLocation.y + rectangle.y
        + rectangle.height / 2);
    if (robot != null) {
      try {
        robot.mouseMove(centerPoint.x, centerPoint.y);
        startPoint = centerPoint;
      }
      catch (Exception ex) {
        startPoint = pointOnScreen;
        popupLocation.x += startPoint.x - centerPoint.x;
        popupLocation.y += startPoint.y - centerPoint.y;
      }
    }
    else {
      startPoint = pointOnScreen;
      popupLocation.x += startPoint.x - centerPoint.x;
      popupLocation.y += startPoint.y - centerPoint.y;
    }
    popupMenu.show(button, popupLocation.x, popupLocation.y);
    Point adjustedPopupLocation = popupMenu.getLocationOnScreen();
    centerPoint = new Point(adjustedPopupLocation.x + rectangle.x
        + rectangle.width / 2, adjustedPopupLocation.y + rectangle.y
        + rectangle.height / 2);
    if (robot != null) {
      try {
        robot.mouseMove(centerPoint.x, centerPoint.y);
        startPoint = centerPoint;
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
      }
      catch (Exception ex) {
      }
    }
  }

  private void moveRectangle(int aDeltaX, int aDeltaY) {
    if (startRectangle != null) {
      Insets insets = getInsets();
      Rectangle newRect = new Rectangle(startRectangle);
      newRect.x += aDeltaX;
      newRect.y += aDeltaY;
      newRect.x = Math.min(Math.max(newRect.x, insets.left), getWidth() - insets.right
          - newRect.width);
      newRect.y = Math.min(Math.max(newRect.y, insets.right), getHeight() - insets.bottom
          - newRect.height);
      Rectangle clip = new Rectangle();
      Rectangle.union(rectangle, newRect, clip);
      clip.grow(2, 2);
      rectangle = newRect;
      paintImmediately(clip);
    }
  }

  private void scroll(int aDeltaX, int aDeltaY) {
    if (component != null) {
      Rectangle rect = component.getVisibleRect();
      rect.x += aDeltaX;
      rect.y += aDeltaY;
      component.scrollRectToVisible(rect);
      popupMenu.setVisible(false);
    }
  }

}
