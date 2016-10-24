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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Runner {

  public static void showComponent(JComponent component) {
    showComponent(component, null);
  }

  
  public static void showComponent(JComponent centerComponent, JComponent topComponent) {
    if (centerComponent == null) {
      throw new IllegalArgumentException("Component must not be null.");
    }

    JFrame frame = new JFrame();
    Container contentPane = frame.getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(centerComponent, BorderLayout.CENTER);
    if (topComponent != null) {
      contentPane.add(topComponent, BorderLayout.NORTH);
    }

    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    centerOnScreen(frame);
    frame.setVisible(true);
  }
  
  public static void centerOnScreen(Window window) {
    try {
      Dimension screenSize = window.getToolkit().getScreenSize();
      Dimension windowSize = window.getSize();
      int x = (screenSize.width - windowSize.width) / 2;
      int y = (screenSize.height - windowSize.height) / 2;
      window.setLocation(x, y);
    }
    catch (Exception e) {
    }
  }

}
