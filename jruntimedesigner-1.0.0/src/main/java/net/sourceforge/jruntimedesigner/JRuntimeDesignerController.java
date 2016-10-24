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
package net.sourceforge.jruntimedesigner;

import java.awt.Dimension;
import java.awt.Point;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.sourceforge.jruntimedesigner.actions.DesignModeAction;
import net.sourceforge.jruntimedesigner.actions.ExportAction;
import net.sourceforge.jruntimedesigner.actions.ImportAction;
import net.sourceforge.jruntimedesigner.actions.LoadAction;
import net.sourceforge.jruntimedesigner.actions.NewAction;
import net.sourceforge.jruntimedesigner.actions.PreviewModeAction;
import net.sourceforge.jruntimedesigner.actions.ResetAction;
import net.sourceforge.jruntimedesigner.actions.SaveAction;
import net.sourceforge.jruntimedesigner.actions.ToggleModeAction;
import net.sourceforge.jruntimedesigner.common.IWidgetContainer;
import net.sourceforge.jruntimedesigner.common.IWidgetHolder;
import net.sourceforge.jruntimedesigner.common.WidgetHolder;
import net.sourceforge.jruntimedesigner.events.IListenerActivator;
import net.sourceforge.jruntimedesigner.events.IPanelStateListener;
import net.sourceforge.jruntimedesigner.events.IWidgetStateListener;
import net.sourceforge.jruntimedesigner.events.PanelStateEvent;
import net.sourceforge.jruntimedesigner.events.WidgetStateEvent;
import net.sourceforge.jruntimedesigner.provider.FileLayoutDataProvider;
import net.sourceforge.jruntimedesigner.provider.ILayoutDataProvider;
import net.sourceforge.jruntimedesigner.provider.LayoutDataProviderException;
import net.sourceforge.jruntimedesigner.selection.ISelectableWidgetProvider;
import net.sourceforge.jruntimedesigner.selection.WidgetSelectionManager;
import net.sourceforge.jruntimedesigner.undo.SmartUndoManager;
import net.sourceforge.jruntimedesigner.utils.ModelEvent;
import net.sourceforge.jruntimedesigner.utils.ModelListener;
import net.sourceforge.jruntimedesigner.utils.ResourceUtils;
import net.sourceforge.jruntimedesigner.utils.XmlException;
import net.sourceforge.jruntimedesigner.utils.XmlUtils;
import net.sourceforge.jruntimedesigner.widgets.IWidget;


/**
 * Main controller of the runtime designer.
 * 
 * @author ikunin
 * @since 1.0
 */
public class JRuntimeDesignerController implements ISelectableWidgetProvider, IListenerActivator {
  protected static final ResourceBundle res = ResourceUtils.getBundle(JRuntimeDesigner.class);
  private boolean isDesignMode;
  private boolean isDesignModeSupported;
  private JRuntimeDesigner panel;
  /** List of listeners */
  private EventListenerList panelStateListenerList = new EventListenerList();
  private Map<Class<JComponent>, IWidgetStateListener> widgetStateListenerMap = new HashMap<Class<JComponent>, IWidgetStateListener>();
  private JRuntimeDesignerModel model;
  private Map<IWidget, IWidgetHolder> widgetHolderToWidget;
  private WidgetSelectionManager selectionManager;
  private boolean isDirty;
  private static boolean isEditingWidget;
  private ILayoutDataProvider layoutDataProvider;
  private Element lastLoadedLayout;
  private SmartUndoManager undoManager;
  private ILayoutDataProvider importExportDataProvider;

  public JRuntimeDesignerController(JRuntimeDesigner designer) {
    this.isDesignModeSupported = true;
    this.widgetHolderToWidget = new HashMap<IWidget, IWidgetHolder>();
    this.panel = designer;
    undoManager = new SmartUndoManager();
    selectionManager = new WidgetSelectionManager(panel, this, this);
    selectionManager.addUndoableEditListener(undoManager);
    designer.setSelectionManager(selectionManager);
    model = new JRuntimeDesignerModel();
    model.addModelListener(new ModelListener() {
      public void modelDataChanged(ModelEvent event) {
        renderModel();
      }
    });

    layoutDataProvider = new FileLayoutDataProvider();
    importExportDataProvider = new FileLayoutDataProvider();
  }

  private void renderModel() {
    // logger.debug("Rendering widgets...");
    panel.removeAll();
    selectionManager.resetSelection();
    widgetHolderToWidget.clear();
    Iterator<IWidget> itWidgets = model.getWidgets().iterator();
    while (itWidgets.hasNext()) {
      IWidget widget = itWidgets.next();
      widget.setDesignMode(isDesignMode);
      if (widget.isResizable()) {
        widget.getComponent().setPreferredSize(
            new Dimension(widget.getSize().width - WidgetHolder.BORDER_WIDTH * 2,
                widget.getSize().height - WidgetHolder.BORDER_WIDTH * 2));
      }
      else {
        widget.getComponent().setPreferredSize(widget.getComponent().getPreferredSize());
      }

      WidgetHolder widgetHolder = new WidgetHolder(widget, this);
      selectionManager.register(widgetHolder);
      if (!widgetHolderToWidget.containsKey(widget)) {
        widgetHolderToWidget.put(widget, widgetHolder);
      }
      widgetHolder.setDesignMode(isDesignMode());
      widgetHolder.setLocation(widget.getLocation());
      // if (widget.isResizable()) {
      // widgetHolder.setSize(widget.getSize());
      // }
      // else {
      // widgetHolder.setSize(WidgetHolder.adjustSize(widget.getComponent()
      // .getPreferredSize(), WidgetHolder.BORDER_WIDTH * 2 +10));
      // }

      widgetHolder.setLocked(widget.isLocked());
      panel.add(widgetHolder, new Integer(widget.getLayer()));
      widgetHolder.invalidate();
    }
    panel.setShowGrid(model.isShowGrid());
    panel.repaint();
    panel.revalidate();
  }


  public void addWidget(IWidget widget, Point location) {
    widget.getComponent();
 
    WidgetHolder widgetHolder = new WidgetHolder(widget, this);
    widgetHolder.setLocation(location);
    widgetHolder.recalcSize();
    widget.setDesignMode(isDesignMode());
    selectionManager.register(widgetHolder);
    if (!widgetHolderToWidget.containsKey(widgetHolder.getWidget())) {
      widgetHolderToWidget.put(widgetHolder.getWidget(), widgetHolder);
    }
    model.addWidget(widgetHolder.getWidget());
    panel.add(widgetHolder, new Integer(widgetHolder.getWidget().getLayer()));
    panel.repaint();
    widgetHolder.invalidate();
    panel.revalidate();
    setDirty();
  }

  public void removeWidget(IWidget widget) {
    IWidgetHolder widgetHolder = getWidgetHolder(widget);
    selectionManager.unregister(widgetHolder);
    model.removeWidget(widgetHolder.getWidget());
    // FIXME no dependency to WidgetHolder
    if (widgetHolder instanceof WidgetHolder) {
      ((WidgetHolder) widgetHolder).removeUndoableEditListener(undoManager);
      panel.remove(((WidgetHolder) widgetHolder));
    }
    widgetHolderToWidget.remove(widget);
  }

  public IWidgetHolder getWidgetHolder(IWidget widget) {
    if (!widgetHolderToWidget.containsKey(widget)) {
      return null;
    }
    return widgetHolderToWidget.get(widget);
  }

  public void addPanelStateListener(IPanelStateListener l) {
    panelStateListenerList.add(IPanelStateListener.class, l);
  }

  public void removePanelStateListener(IPanelStateListener l) {
    panelStateListenerList.remove(IPanelStateListener.class, l);
  }

  /**
   * Notifies all listeners that the model data has changed.
   * 
   * @see ModelEvent
   * @see EventListenerList
   */
  private synchronized void firePanelStateChanged(final boolean isDesignMode) {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          firePanelStateChangedInternal(isDesignMode);
        }
      });
    }
    else {
      firePanelStateChangedInternal(isDesignMode);
    }
  }

  private void firePanelStateChangedInternal(boolean isDesignMode) {
    Object[] listeners = panelStateListenerList.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == IPanelStateListener.class) {
        if (isDesignMode) {
          ((IPanelStateListener) listeners[i + 1]).panelDeactivated(new PanelStateEvent(panel,
              isDesignMode));
        }
        else {
          ((IPanelStateListener) listeners[i + 1]).panelActivated(new PanelStateEvent(panel,
              isDesignMode));
        }
      }
    }
    // fireWidgetStateChanged(isDesignMode);
  }

  public void addWidgetStateListener(Class<JComponent> widgetClass, IWidgetStateListener l) {
    if (!widgetStateListenerMap.containsKey(widgetClass)) {
      widgetStateListenerMap.put(widgetClass, l);
    }
  }

  public void removeWidgetStateListener(Class<JComponent> widgetClass) {
    if (widgetStateListenerMap.containsKey(widgetClass)) {
      widgetStateListenerMap.remove(widgetClass);
    }
  }

  private synchronized void fireWidgetStateChanged(boolean isDesignMode) {
    fireWidgetStateChanged(model.getWidgets(), isDesignMode);
  }

  private synchronized void fireWidgetStateChanged(List<IWidget> widgets, boolean isDesignMode) {
    Iterator<IWidget> itWidgets = widgets.iterator();
    while (itWidgets.hasNext()) {
      IWidget widget = itWidgets.next();
      if (widget instanceof IWidgetContainer) {
        IWidgetContainer container = (IWidgetContainer) widget;
        fireWidgetStateChanged(container.getWidgets(), isDesignMode);
      }
      else {
        fireWidgetStateChanged(widget, isDesignMode);
      }
    }
  }

  /**
   * Notifies all listeners that the model data has changed.
   * 
   * @see ModelEvent
   * @see EventListenerList
   */
  private synchronized void fireWidgetStateChanged(final IWidget widget, final boolean isDesignMode) {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          fireWidgetStateChangedInternal(widget, isDesignMode);
        }
      });
    }
    else {
      fireWidgetStateChangedInternal(widget, isDesignMode);
    }
  }

  private synchronized void fireWidgetStateChangedInternal(IWidget widget, boolean isDesignMode) {
    Class<? extends JComponent> componentClass = widget.getComponent().getClass();
    if (!widgetStateListenerMap.containsKey(componentClass)) {
      return;
    }
    IWidgetStateListener listener = widgetStateListenerMap.get(componentClass);
    if (isDesignMode) {
      listener
          .widgetDeactivated(new WidgetStateEvent(panel, widget.getComponent(), widget.getId()));
    }
    else {
      listener.widgetActivated(new WidgetStateEvent(panel, widget.getComponent(), widget.getId()));
    }
  }

  public void save(OutputStream out) throws XmlException {
    model.save(out,panel.getLayoutName());
    resetDirty();
  }

  public void load(InputStream is) throws XmlException {
    Element rootElement = XmlUtils.load(is);
    load(rootElement);
  }

  public void load(Element rootElement) throws XmlException {
    if (isDirty) {
      if (!confirmAction(res.getString("confirmation.discardChanges"))) {
        return;
      }
    }

    // logger.debug("Loading...");
    if (isDesignMode) {
      firePanelStateChanged(false);
      fireWidgetStateChanged(false);
    }
    widgetHolderToWidget.clear();
    panel.removeAll();
    panel.repaint();
    panel.revalidate();
    model.load(rootElement);
    if (isDesignMode) {
      firePanelStateChanged(isDesignMode);
      fireWidgetStateChanged(isDesignMode);
    }
    else {
      fireWidgetStateChanged(isDesignMode);
      firePanelStateChanged(isDesignMode);
    }
    resetDirty();
    this.lastLoadedLayout = rootElement;
    updateActionState();
  }

  /**
   * Reloads the last loaded layout.
   * 
   * @throws XmlException
   */
  public void reset() throws XmlException {
    if (lastLoadedLayout != null) {
      load(lastLoadedLayout);
    }
  }

  /**
   * 
   */
  public void clear() {
    if (isDirty) {
      if (!confirmAction(res.getString("clearAction.discardChanges"))) {
        return;
      }
    }
    else {
      if (!confirmAction(res.getString("clearAction.confirmation"))) {
        return;
      }
    }
    panel.removeAll();
    panel.repaint();
    panel.revalidate();
    model.reset();
    selectionManager.resetSelection();
    widgetHolderToWidget.clear();
    resetDirty();
  }

  public void setDesignMode(boolean isDesignMode) {
    this.isDesignMode = isDesignMode;
    for (IWidget widget : model.getWidgets()) {
      widget.setDesignMode(isDesignMode);
    }
    if (!isDesignMode) {
      selectionManager.resetSelection();
    }
    panel.repaint();
    if (isDesignMode) {
      firePanelStateChanged(isDesignMode);
      fireWidgetStateChanged(isDesignMode);
    }
    else {
      fireWidgetStateChanged(isDesignMode);
      firePanelStateChanged(isDesignMode);
    }
    updateActionState();
  }

  private void updateActionState() {
    boolean isEnabled = isDesignMode;
    ActionMap map = panel.getActionMap();
    map.get(NewAction.NAME).setEnabled(isEnabled);
    map.get(LoadAction.NAME).setEnabled(isEnabled);
    map.get(SaveAction.NAME).setEnabled(isEnabled);
    map.get(ImportAction.NAME).setEnabled(isEnabled);
    map.get(ExportAction.NAME).setEnabled(isEnabled);
    map.get(ResetAction.NAME).setEnabled(isEnabled);
    map.get(ToggleModeAction.NAME).setEnabled(isDesignModeSupported);
    map.get(PreviewModeAction.NAME).setEnabled(isEnabled);
    map.get(DesignModeAction.NAME).setEnabled(!isEnabled);
  }

  public final boolean isDesignMode() {
    return isDesignMode;
  }

  public void toggleDesignMode() {
    setDesignMode(!isDesignMode());
  }

  public final JRuntimeDesigner getPanel() {
    return panel;
  }

  public final boolean isDesignModeSupported() {
    return isDesignModeSupported;
  }

  public final void setDesignModeSupported(boolean isDesignModeSupported) {
    this.isDesignModeSupported = isDesignModeSupported;
  }

  public JRuntimeDesignerModel getModel() {
    return model;
  }

  public WidgetSelectionManager getSelectionManager() {
    return selectionManager;
  }

  public void setDirty() {
    isDirty = true;
  }

  private void resetDirty() {
    isDirty = false;
    undoManager.discardAllEdits();
  }

  public boolean hasChanges() {
    return isDirty;
  }

  protected boolean confirmAction(String msg) {
    return JOptionPane.showConfirmDialog(null, msg, res.getString("confirmation.Title"),
        JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
  }

  public void setLocked(boolean value) {
    if (selectionManager.hasSelection()) {
      for (IWidgetHolder selectedWidget : selectionManager.getSelectedWidgets()) {
        selectedWidget.setLocked(value);
      }
      selectionManager.resetSelection();
    }
    panel.repaint();
    setDirty();
  }

  public Element toXML(Document doc,String layout) {
    return model.toXML(doc,layout);
  }


  public void undo() {
    undoManager.undo();
  }

  public void redo() {
    undoManager.redo();
  }

  public static boolean isEditingWidget() {
    return isEditingWidget;
  }

  public static void setEditingWidget(boolean value) {
    isEditingWidget = value;
  }

  public List<IWidget> getWidgets() {
    return model.getWidgets();
  }

  public ILayoutDataProvider getLayoutDataProvider() {
    if (layoutDataProvider == null) {
      layoutDataProvider = new FileLayoutDataProvider();
    }
    return layoutDataProvider;
  }

  public void setLayoutDataProvider(ILayoutDataProvider layoutProvider) {
    this.layoutDataProvider = layoutProvider;
  }

  public void load() throws LayoutDataProviderException {
    if (getLayoutDataProvider() != null) {
      getLayoutDataProvider().load(this);
    }
  }

  public void save() throws LayoutDataProviderException {
    if (getLayoutDataProvider() != null) {
      getLayoutDataProvider().save(this);
    }
  }

  public SmartUndoManager getUndoManager() {
    return undoManager;
  }

  public void importLayout() throws LayoutDataProviderException {
    if (importExportDataProvider != null) {
      importExportDataProvider.load(this);
    }
  }

  public void exportLayout() throws LayoutDataProviderException {
    if (importExportDataProvider != null) {
      importExportDataProvider.save(this);
    }
  }

  /*-------------------------------[ IListenerActivator ]-----------------------------*/
  public boolean isListenerEnabled() {
    return isDesignMode;
  }

  /*-------------------------------[ ISelectableWidgetProvider ]-----------------------------*/
  public Iterator<IWidgetHolder> getSelectableWidgets() {
    return widgetHolderToWidget.values().iterator();
  }

}
