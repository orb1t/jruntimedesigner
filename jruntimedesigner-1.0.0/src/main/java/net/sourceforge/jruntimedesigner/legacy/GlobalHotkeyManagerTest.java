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
package net.sourceforge.jruntimedesigner.legacy;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class GlobalHotkeyManagerTest extends JFrame {
    private final static String UIROBOT_KEY = "UIRobot";
    private final KeyStroke uirobotHotkey = KeyStroke.getKeyStroke(
      KeyEvent.VK_M, KeyEvent.CTRL_MASK + KeyEvent.ALT_MASK, false);
    private final Action uirobot = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        setEnabled(false); // stop any other events from interfering
        JOptionPane.showMessageDialog(GlobalHotkeyManagerTest.this,
          "UIRobot Hotkey was pressed");
        setEnabled(true);
      }
    };
    public GlobalHotkeyManagerTest() {
      super("Global Hotkey Manager Test");
      setSize(500,400);
      getContentPane().setLayout(new FlowLayout());
      getContentPane().add(new JButton("Button 1"));
      getContentPane().add(new JTextField(20));
      getContentPane().add(new JButton("Button 2"));
      GlobalHotkeyManager hotkeyManager = GlobalHotkeyManager.getInstance();
      hotkeyManager.getInputMap().put(uirobotHotkey, UIROBOT_KEY);
      hotkeyManager.getActionMap().put(UIROBOT_KEY, uirobot);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // JDK 1.3
      setVisible(true);
    }
    public static void main(String[] args) {
      new GlobalHotkeyManagerTest();
    }
  }
