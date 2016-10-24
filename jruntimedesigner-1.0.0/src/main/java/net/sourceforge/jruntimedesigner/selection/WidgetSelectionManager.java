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
package net.sourceforge.jruntimedesigner.selection;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;

import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.common.IWidgetHolder;
import net.sourceforge.jruntimedesigner.common.WidgetHolder;
import net.sourceforge.jruntimedesigner.events.DetachableKeyEventDispatcher;
import net.sourceforge.jruntimedesigner.events.DetachableMouseListener;
import net.sourceforge.jruntimedesigner.events.DetachableMouseMotionListener;
import net.sourceforge.jruntimedesigner.events.IListenerActivator;
import net.sourceforge.jruntimedesigner.undo.DeselectWidgetUndoableEdit;
import net.sourceforge.jruntimedesigner.undo.SelectWidgetUndoableEdit;
import net.sourceforge.jruntimedesigner.undo.SetGuideWidgetUndoableEdit;
import net.sourceforge.jruntimedesigner.undo.UndoRedoProgress;
import net.sourceforge.jruntimedesigner.undo.UndoableModelSupport;
import net.sourceforge.jruntimedesigner.undo.UnsetGuideWidgetUndoableEdit;
import net.sourceforge.jruntimedesigner.widgets.IWidget;


/**
 * WidgetSelectionManager is responsible for managing the selection and the
 * visualization of it. The following functions are supported:
 * <ul>
 * <li>Selecting and deselecting the widget by the mouse click.</li>
 * <li>Selecting multiple widgets by selecting the widgets with the mouse
 * (marquee selection)</li>
 * <li>Adding a new selection to a already defined by one of the previous
 * methods with a pressed Ctrl key</li>
 * <li>Auto selection of the guide widget, used for the group operations like
 * alignment.</li>
 * <li>Manual selection of an guide widget with a mouse with pressed Ctrl and
 * Shift keys.</li>
 * <li>Removing the selection by clicking with the left mouse key outside of
 * any widgets. It will not happen if the Ctrl key is pressed.</li>
 * </ul>
 * 
 * @author ikunin
 * @author $Author: ikunin $ (Last change)
 * @version $Revision: 11618 $ $Date: 2007-08-07 15:17:04 +0200 (Di, 07 Aug
 *          2007) $
 * @since 1.0
 */
public class WidgetSelectionManager {
  private final BasicStroke selectionStroke;
  private boolean isSelecting;
  private Point selectionStart;
  private Point selectionEnd;
  private ISelectableWidgetProvider widgetProvider;
  private IListenerActivator activator;
  private JComponent component;
  private final AlphaComposite composite;
  private List<IWidgetHolder> selectedWidgets;
  private boolean isShiftDown;
  private boolean isCtrlDown;
  private boolean isGuideFound;
  private EventListenerList widgetSelectionListenerList = new EventListenerList();
  private UndoableModelSupport undoableModelSupport;
  private UndoRedoProgress progress;
  private IWidgetHolder guideWidget;

  public WidgetSelectionManager(JComponent component, ISelectableWidgetProvider widgetProvider,
      IListenerActivator activator) {
    this.widgetProvider = widgetProvider;
    this.activator = activator;
    this.component = component;
    this.selectedWidgets = new ArrayList<IWidgetHolder>();
    this.progress = new UndoRedoProgress();
    this.undoableModelSupport = new UndoableModelSupport();
    composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
    selectionStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
        new float[]{1.0f, 5.0f, 5.0f, 1.0f}, 0.0f);
    initListener();
  }

  private void initListener() {
    component.addMouseListener(new DetachableMouseListener(new MouseListener() {

      public void mouseReleased(MouseEvent e) {
        if (e.getButton() != 3 && isSelecting) {
          isSelecting = false;
          isGuideFound = false;
          selectionStart = null;
          selectionEnd = null;
          component.repaint();
        }
      }

      public void mousePressed(MouseEvent e) {
        if (e.getButton() != 3) { // isPopupTrigger()
          // logger.debug("Starting selection...");
          isSelecting = true;
          isGuideFound = false;
          selectionStart = e.getPoint();
          selectionEnd = e.getPoint();
          component.repaint();
        }
      }

      public void mouseExited(MouseEvent e) {}

      public void mouseEntered(MouseEvent e) {}

      public void mouseClicked(MouseEvent e) {
        if (!isCtrlDown) {
          resetSelection();
        }
      }
    }, activator));

    component.addMouseMotionListener(new DetachableMouseMotionListener(new MouseMotionListener() {

      public void mouseMoved(MouseEvent e) {

      }

      public void mouseDragged(MouseEvent e) {
        if (isSelecting) {
          selectionEnd = e.getPoint();
          markSelectedWidgets();
          component.repaint();
        }
      }

    }, activator));

    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
        new DetachableKeyEventDispatcher(new KeyEventDispatcher() {
          public boolean dispatchKeyEvent(KeyEvent e) {
            if (!hasSelection()) {
              return false;
            }

            if (e.getID() == KeyEvent.KEY_PRESSED) {
              isShiftDown = e.isShiftDown();
              isCtrlDown = e.isControlDown();
              int step = isShiftDown ? 1 : 5;
              if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (isCtrlDown) {
                  resizeWidgets(0, step);
                }
                else {
                  moveWidgets(0, step);
                }
                e.consume();
              }
              else if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (isCtrlDown) {
                  resizeWidgets(0, -step);
                }
                else {
                  moveWidgets(0, -step);
                }
                e.consume();
              }
              else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (isCtrlDown) {
                  resizeWidgets(-step, 0);
                }
                else {
                  moveWidgets(-step, 0);
                }
                e.consume();
              }
              else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (isCtrlDown) {
                  resizeWidgets(step, 0);
                }
                else {
                  moveWidgets(step, 0);
                }
                e.consume();
              }
            }
            else if (e.getID() == KeyEvent.KEY_RELEASED) {
              isShiftDown = e.isShiftDown();
              isCtrlDown = e.isControlDown();
            }

            return false;
          }
        }, new KeyEventDispatcherActivator(activator)));
  }

  class KeyEventDispatcherActivator implements IListenerActivator {
    private IListenerActivator activator;

    public KeyEventDispatcherActivator(IListenerActivator activator) {
      this.activator = activator;
    }

    public boolean isListenerEnabled() {
      return activator.isListenerEnabled() && !JRuntimeDesignerController.isEditingWidget();
    }
  }

  /**
   * 
   * @return true if at least on widget is selected.
   */
  public boolean hasSelection() {
    return selectedWidgets.size() != 0;
  }

  /**
   * 
   * @return the list of selected widgets.
   */
  public List<IWidgetHolder> getSelectedWidgets() {
    return Collections.unmodifiableList(selectedWidgets);
  }

  /**
   * 
   * @return the array of selected widgets.
   */
  public IWidgetHolder[] getSelectedWidgetsAsArray() {
    return selectedWidgets.toArray(new IWidgetHolder[selectedWidgets.size()]);
  }

  private Rectangle getSelectionRectangle() {
    int x = 0;
    int y = 0;
    int width = 0;
    int height = 0;
    if (selectionStart != null && selectionEnd != null) {
      x = Math.min(selectionStart.x, selectionEnd.x);
      y = Math.min(selectionStart.y, selectionEnd.y);
      width = Math.abs(selectionStart.x - selectionEnd.x);
      height = Math.abs(selectionStart.y - selectionEnd.y);
    }
    return new Rectangle(x, y, width, height);
  }

  protected void markSelectedWidgets() {
    IWidgetHolder guideWidget = getGuideWidget();
    if (!isCtrlDown) {
      resetSelection();
    }
    Rectangle selectionRectangle = getSelectionRectangle();
    boolean isWidgetFound = false;
    if (guideWidget != null) {
      // check if guide widget still selected
      if (SwingUtilities.isRectangleContainingRectangle(selectionRectangle, new Rectangle(
          guideWidget.getWidgetLocation(), guideWidget.getWidgetSize()))) {
        guideWidget.setSelected(true);
        guideWidget.setGuide(true);
        selectedWidgets.add(guideWidget);
        isGuideFound = true;
        isWidgetFound = true;
      }
    }
    Iterator<IWidgetHolder> itWidgets = widgetProvider.getSelectableWidgets();
    while (itWidgets.hasNext()) {
      IWidgetHolder widget = itWidgets.next();
      if (guideWidget != null && guideWidget == widget) {
        continue;
      }
      if (SwingUtilities.isRectangleContainingRectangle(selectionRectangle, new Rectangle(widget
          .getWidgetLocation(), widget.getWidgetSize()))) {
        isWidgetFound = true;
        widget.setSelected(true);
        selectedWidgets.add(widget);
        if (!isGuideFound) {
          isGuideFound = true;
          if (!hasGuide()) {
            widget.setGuide(true);
          }
        }
      }
      else {
        if (!isCtrlDown) {
          selectedWidgets.remove(widget);
          widget.setSelected(false);
        }
      }
    }
    if (!isWidgetFound) {
      isGuideFound = false;
    }
    assureHasGuide();
    fireWidgetSelectionChanged();
  }

  public void resetSelection() {
    Iterator<IWidgetHolder> itWidgets = selectedWidgets.iterator();
    while (itWidgets.hasNext()) {
      IWidgetHolder widget = itWidgets.next();
      widget.setSelected(false);
    }
    selectedWidgets.clear();
    resetGuide();
    fireWidgetSelectionChanged();
  }

  public void deselect(IWidget widget) {
    selectedWidgets.remove(widget);
    assureHasGuide();
    fireWidgetSelectionChanged();
  }

  private void assureHasGuide() {
    if (hasSelection() && !hasGuide()) {
      IWidgetHolder widget = selectedWidgets.get(0);
      setGuide(widget);
    }
  }

  protected void resetGuide() {
    if (guideWidget != null) {
      guideWidget.setGuide(false);
      guideWidget = null;
      isGuideFound = false;
    }
  }

  protected boolean hasGuide() {
    return guideWidget != null;
  }

  public void paintSelection(Graphics g) {
    if (!activator.isListenerEnabled()) {
      return;
    }
    Graphics2D g2 = (Graphics2D) g;
    Composite oldComposite = g2.getComposite();
    g2.setComposite(composite);

    Color oldColor = g2.getColor();
    Stroke oldStroke = g2.getStroke();
    g2.setColor(Color.black);
    g2.setStroke(selectionStroke);
    int x = 0;
    int y = 0;
    int width = 0;
    int height = 0;
    if (selectionStart != null && selectionEnd != null) {
      x = Math.min(selectionStart.x, selectionEnd.x);
      y = Math.min(selectionStart.y, selectionEnd.y);
      width = Math.abs(selectionStart.x - selectionEnd.x);
      height = Math.abs(selectionStart.y - selectionEnd.y);
    }
    g2.drawRect(x, y, width, height);
    g2.setColor(oldColor);
    g2.setStroke(oldStroke);
    g2.setComposite(oldComposite);
  }

  public void moveWidgets(int dx, int dy) {
    Iterator<IWidgetHolder> itWidgets = selectedWidgets.iterator();
    while (itWidgets.hasNext()) {
      IWidgetHolder widget = itWidgets.next();
      if (widget.isLocked()) {
        continue;
      }
      widget.moveSingleWidget(dx, dy);
    }
  }

  public void resizeWidgets(int dx, int dy) {
    Iterator<IWidgetHolder> itWidgets = selectedWidgets.iterator();
    while (itWidgets.hasNext()) {
      IWidgetHolder widget = itWidgets.next();
      if (!widget.isResizable()) {
        continue;
      }
      if (widget.isLocked()) {
        continue;
      }
      widget.resizeWidget(dx, dy);
    }
  }

  public void register(WidgetHolder widgetHolder) {
    widgetHolder.addMouseListener(new DetachableMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        IWidgetHolder widgetHolder = (IWidgetHolder) e.getSource();
        if (isShiftDown && isCtrlDown) {
          boolean newSelectedState = !widgetHolder.isSelected();
          boolean newGuideState = !widgetHolder.isGuide();

          if (newSelectedState) {
            selectedWidgets.add(widgetHolder);
            widgetHolder.setSelected(newSelectedState);
          }
          else if (selectedWidgets.size() == 2 && guideWidget == widgetHolder) {
            selectedWidgets.remove(widgetHolder);
            widgetHolder.setSelected(newSelectedState);
            unsetGuide(widgetHolder);
          }
          if (newGuideState) {
            setGuide(widgetHolder);
          }
          else {
            unsetGuide(widgetHolder);
          }
          assureHasGuide();
        }
        else {
          boolean newSelectedState = !widgetHolder.isSelected();
          if (!isCtrlDown) {
            resetSelection();
          }

          if (newSelectedState) {
            selectedWidgets.add(widgetHolder);
          }
          else {
            selectedWidgets.remove(widgetHolder);
          }
          widgetHolder.setSelected(newSelectedState);
          assureHasGuide();
        }
        fireWidgetSelectionChanged();
      }
    }, activator));
  }

  public void unregister(IWidgetHolder widgetHolder) {
    if (widgetHolder.isSelected()) {
      widgetHolder.setSelected(false);
      widgetHolder.setGuide(false);
      selectedWidgets.remove(widgetHolder.getWidget());
      fireWidgetSelectionChanged();
    }
  }

  public IWidgetHolder getGuideWidget() {
    if (hasSelection()) {
      return guideWidget;
    }
    else {
      return null;
    }
  }

  public void addWidgetSelectionListener(IWidgetSelectionListener l) {
    widgetSelectionListenerList.add(IWidgetSelectionListener.class, l);
  }

  public void removeWidgetSelectionListener(IWidgetSelectionListener l) {
    widgetSelectionListenerList.remove(IWidgetSelectionListener.class, l);
  }

  /**
   * Notifies all listeners that the selection has changed.
   */
  private synchronized void fireWidgetSelectionChanged() {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          fireWidgetSelectionChangedInternal();
        }
      });
    }
    else {
      fireWidgetSelectionChangedInternal();
    }
  }

  /**
   * Notifies all listeners that the selection has changed.
   */
  private void fireWidgetSelectionChangedInternal() {
    Object[] listeners = widgetSelectionListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == IWidgetSelectionListener.class) {
        ((IWidgetSelectionListener) listeners[i + 1])
            .widgetSelectionChanged(new WidgetSelectionEvent(this));
      }
    }
  }

  public void selectWidget(IWidgetHolder widget) {
    selectedWidgets.add(widget);
    widget.setSelected(true);
    assureHasGuide();
    fireWidgetSelectionChanged();
    if (!progress.isInProgress()) {
      fireUndoableEditHappened(new SelectWidgetUndoableEdit(progress, this, widget));
    }
  }

  public void deselectWidget(IWidgetHolder widget) {
    selectedWidgets.remove(widget);
    widget.setSelected(false);
    assureHasGuide();
    fireWidgetSelectionChanged();
    if (!progress.isInProgress()) {
      fireUndoableEditHappened(new DeselectWidgetUndoableEdit(progress, this, widget));
    }
  }

  public void setGuide(IWidgetHolder widget) {
    if (hasGuide()) {
      resetGuide();
    }
    widget.setGuide(true);
    guideWidget = widget;
    isGuideFound = true;
    if (!progress.isInProgress()) {
      fireUndoableEditHappened(new SetGuideWidgetUndoableEdit(progress, this, widget));
    }
  }

  public void unsetGuide(IWidgetHolder widget) {
    widget.setGuide(false);
    guideWidget = null;
    assureHasGuide();
    if (!progress.isInProgress()) {
      fireUndoableEditHappened(new UnsetGuideWidgetUndoableEdit(progress, this, widget));
    }
  }

  /*-------------------------------[ UndoableModel ]-----------------------------*/
  public void addUndoableEditListener(UndoableEditListener listener) {
    undoableModelSupport.addUndoableEditListener(listener);
  }

  public void removeUndoableEditListener(UndoableEditListener listener) {
    undoableModelSupport.removeUndoableEditListener(listener);
  }

  private void fireUndoableEditHappened(UndoableEdit edit) {
    undoableModelSupport.fireUndoableEditHappened(edit);
  }

  public int getSelectedWidgetCount() {
    return selectedWidgets.size();
  }

}
