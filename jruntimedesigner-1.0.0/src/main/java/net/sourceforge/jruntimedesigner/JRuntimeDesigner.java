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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.RepaintManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.sourceforge.jruntimedesigner.actions.AddWidgetAction;
import net.sourceforge.jruntimedesigner.actions.AlignBottomAction;
import net.sourceforge.jruntimedesigner.actions.AlignHeightAction;
import net.sourceforge.jruntimedesigner.actions.AlignLeftAction;
import net.sourceforge.jruntimedesigner.actions.AlignRightAction;
import net.sourceforge.jruntimedesigner.actions.AlignSizeAction;
import net.sourceforge.jruntimedesigner.actions.AlignTopAction;
import net.sourceforge.jruntimedesigner.actions.AlignWidthAction;
import net.sourceforge.jruntimedesigner.actions.DesignModeAction;
import net.sourceforge.jruntimedesigner.actions.ExportAction;
import net.sourceforge.jruntimedesigner.actions.ImportAction;
import net.sourceforge.jruntimedesigner.actions.LoadAction;
import net.sourceforge.jruntimedesigner.actions.LockAction;
import net.sourceforge.jruntimedesigner.actions.MoveBackAction;
import net.sourceforge.jruntimedesigner.actions.MoveBackwardsAction;
import net.sourceforge.jruntimedesigner.actions.MoveForwardsAction;
import net.sourceforge.jruntimedesigner.actions.MoveFrontAction;
import net.sourceforge.jruntimedesigner.actions.NewAction;
import net.sourceforge.jruntimedesigner.actions.PreviewModeAction;
import net.sourceforge.jruntimedesigner.actions.RedoAction;
import net.sourceforge.jruntimedesigner.actions.RemoveWidgetAction;
import net.sourceforge.jruntimedesigner.actions.ResetAction;
import net.sourceforge.jruntimedesigner.actions.SaveAction;
import net.sourceforge.jruntimedesigner.actions.ToggleModeAction;
import net.sourceforge.jruntimedesigner.actions.UndoAction;
import net.sourceforge.jruntimedesigner.actions.UnlockAction;
import net.sourceforge.jruntimedesigner.actions.WidgetsAction;
import net.sourceforge.jruntimedesigner.common.AutoscrollSupport;
import net.sourceforge.jruntimedesigner.events.IPanelStateListener;
import net.sourceforge.jruntimedesigner.events.IWidgetStateListener;
import net.sourceforge.jruntimedesigner.provider.ILayoutDataProvider;
import net.sourceforge.jruntimedesigner.provider.LayoutDataProviderException;
import net.sourceforge.jruntimedesigner.selection.WidgetSelectionManager;
import net.sourceforge.jruntimedesigner.utils.XmlException;
import net.sourceforge.jruntimedesigner.utils.XmlUtils;
import net.sourceforge.jruntimedesigner.widgets.IWidget;
import net.sourceforge.jruntimedesigner.widgets.IWidgetProvider;
import net.sourceforge.jruntimedesigner.widgets.WidgetProviderFactoryRegistry;


/**
 * Runtime Designer makes it possible to change the panel's layout at runtime
 * and make the changes happen without the need to restart the dialog. As if you
 * would have an embedded visual editor inside of your application. Please take
 * into account that the widgets are positioned by the absolute coordinates. So
 * no layout manager is supported but also not required for the purpose this
 * designer was developed.
 * 
 * @author ikunin
 * @since 1.0
 */
@SuppressWarnings("serial")
public class JRuntimeDesigner extends JLayeredPane {
  public final static String DESIGN_MODE_PROPERTY = "gui.design";

  private final AutoscrollSupport scrollSupport = new AutoscrollSupport(this, new Insets(10, 10,
      10, 10));
  private boolean isShowGrid;
  private JPopupMenu designPopup;
  private JPopupMenu runtimePopup;
  private Point location;
  private final JRuntimeDesignerController controller;
  private Dimension targetResolution;
  private boolean isDesignMenuEnabled;
  private final BasicStroke gridStroke;
  private WidgetSelectionManager selectionManager;
  private JMenu widgetsMenu;
  private Graphics offScreenGraphics;
  private String layoutName = null;
  private String layoutTitle = IWidgetProvider.PROVIDER_CANVAS;
  private boolean topCanvas = true;

  public JRuntimeDesigner() {
	  this(true);
  }
  public JRuntimeDesigner(boolean topCanvas) {
	this.topCanvas = topCanvas;
    RepaintManager currentManager = RepaintManager.currentManager(this);
    currentManager.setDoubleBufferingEnabled(true);
    gridStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
        new float[]{1.0f, 1.0f, 5.0f, 1.0f}, 0.0f);
    setShowGrid(true);
    initLayout();
    controller = new JRuntimeDesignerController(this);
    initActions();
    initPopupMenu();
    setAutoscrolls(true);
    initDnDLoader();
    checkDesignModeSupported();
    addAutoscrollingSupport();
  }
  
  public void setLayoutName(String layoutName) {
	this.layoutName = layoutName;
}
public String getLayoutName(){
	  return layoutName;
  }

  public String getLayoutTitle() {
	return layoutTitle;
}
public void setLayoutTitle(String layoutTitle) {
	this.layoutTitle = layoutTitle;
}
private void addAutoscrollingSupport() {
    setAutoscrolls(true);
    MouseMotionListener doScrollRectToVisible = new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
        ((JComponent) e.getSource()).scrollRectToVisible(r);
      }
    };
    this.addMouseMotionListener(doScrollRectToVisible);
  }

  private void checkDesignModeSupported() {
    String value = System.getProperty(DESIGN_MODE_PROPERTY);
    setDesignModeSupported("true".equalsIgnoreCase(value));
  }

  private void initActions() {
    ActionMap actionMap = getActionMap();
    actionMap.put(NewAction.NAME, new NewAction(controller));
    actionMap.put(LoadAction.NAME, new LoadAction(controller));
    actionMap.put(SaveAction.NAME, new SaveAction(controller));
    actionMap.put(ResetAction.NAME, new ResetAction(controller));
    actionMap.put(RemoveWidgetAction.NAME, new RemoveWidgetAction(controller));
    actionMap.put(AlignTopAction.NAME, new AlignTopAction(controller));
    actionMap.put(AlignBottomAction.NAME, new AlignBottomAction(controller));
    actionMap.put(AlignLeftAction.NAME, new AlignLeftAction(controller));
    actionMap.put(AlignRightAction.NAME, new AlignRightAction(controller));
    actionMap.put(AlignSizeAction.NAME, new AlignSizeAction(controller));
    actionMap.put(AlignWidthAction.NAME, new AlignWidthAction(controller));
    actionMap.put(AlignHeightAction.NAME, new AlignHeightAction(controller));

    actionMap.put(MoveFrontAction.NAME, new MoveFrontAction(controller));
    actionMap.put(MoveBackAction.NAME, new MoveBackAction(controller));
    actionMap.put(MoveForwardsAction.NAME, new MoveForwardsAction(controller));
    actionMap.put(MoveBackwardsAction.NAME, new MoveBackwardsAction(controller));

    actionMap.put(LockAction.NAME, new LockAction(controller));
    actionMap.put(UnlockAction.NAME, new UnlockAction(controller));
    actionMap.put(UndoAction.NAME, new UndoAction(controller));
    actionMap.put(RedoAction.NAME, new RedoAction(controller));
    actionMap.put(DesignModeAction.NAME, new DesignModeAction(controller));
    actionMap.put(PreviewModeAction.NAME, new PreviewModeAction(controller));
    actionMap.put(ToggleModeAction.NAME, new ToggleModeAction(controller));
    actionMap.put(ImportAction.NAME, new ImportAction(controller));
    actionMap.put(ExportAction.NAME, new ExportAction(controller));
  }
  
  public List<Action> createWidgetsMenuAction(){
	  List<Action> acts = new ArrayList<Action>();
	 for(String name:WidgetProviderFactoryRegistry.instance().getWidgetProviderNames(getLayoutName())){
		 acts.add(new AddWidgetAction(WidgetProviderFactoryRegistry.instance().getWidgetProvider(name), controller, location));
	 }
	 return acts;
  }
  
  public JMenu createWidgetsMenu(){
	 JMenu menu = new JMenu(new WidgetsAction(controller));
	 List<Action> acts = createWidgetsMenuAction();
	 for(Action act:acts){
		 menu.add(act);
	 }
	 return menu;
  }

  private void createDesignPopupMenu() {
    designPopup = new JPopupMenu();

    ActionMap actionMap = getActionMap();
    if(topCanvas){
	    designPopup.add(actionMap.get(PreviewModeAction.NAME));
	    designPopup.addSeparator();
	    designPopup.add(actionMap.get(NewAction.NAME));
	    designPopup.add(actionMap.get(LoadAction.NAME));
	    designPopup.add(actionMap.get(SaveAction.NAME));
	    designPopup.addSeparator();
	    designPopup.add(actionMap.get(ImportAction.NAME));
	    designPopup.add(actionMap.get(ExportAction.NAME));
	    designPopup.addSeparator();
	    designPopup.add(actionMap.get(ResetAction.NAME));
	    designPopup.addSeparator();
    }
    widgetsMenu = createWidgetsMenu();
    designPopup.add(widgetsMenu);
    designPopup.addSeparator();
    designPopup.add(actionMap.get(RemoveWidgetAction.NAME));
    designPopup.addSeparator();
    designPopup.add(actionMap.get(AlignTopAction.NAME));
    designPopup.add(actionMap.get(AlignBottomAction.NAME));
    designPopup.add(actionMap.get(AlignLeftAction.NAME));
    designPopup.add(actionMap.get(AlignRightAction.NAME));
    designPopup.addSeparator();
    designPopup.add(actionMap.get(AlignSizeAction.NAME));
    designPopup.add(actionMap.get(AlignWidthAction.NAME));
    designPopup.add(actionMap.get(AlignHeightAction.NAME));
    // designPopup.addSeparator();
    // designPopup.add(new GroupAction(controller));
    // designPopup.add(new UngroupAction(controller));
    designPopup.addSeparator();
    designPopup.add(actionMap.get(MoveFrontAction.NAME));
    designPopup.add(actionMap.get(MoveBackAction.NAME));
    designPopup.add(actionMap.get(MoveForwardsAction.NAME));
    designPopup.add(new MoveBackwardsAction(controller));
    designPopup.addSeparator();
    designPopup.add(actionMap.get(LockAction.NAME));
    designPopup.add(actionMap.get(UnlockAction.NAME));
    designPopup.addSeparator();
    designPopup.add(actionMap.get(UndoAction.NAME));
    designPopup.add(actionMap.get(RedoAction.NAME));
    
  }
  private void createRuntimePopupMenu() {
    runtimePopup = new JPopupMenu();
    ActionMap actionMap = getActionMap();
    runtimePopup.add(actionMap.get(DesignModeAction.NAME));
  }

  private void initDnDLoader() {
    new DropTarget(this, new DropTargetListener() {
      public void dragEnter(DropTargetDragEvent dtde) {}

      public void dragOver(DropTargetDragEvent dtde) {
        if (!controller.isDesignMode()) {
          dtde.rejectDrag();
        }
      }

      public void dropActionChanged(DropTargetDragEvent dtde) {}

      public void dragExit(DropTargetEvent dte) {}

      public void drop(DropTargetDropEvent dtde) {
        try {
          if (!controller.isDesignMode()) {
            dtde.rejectDrop();
            return;
          }
          Transferable tr = dtde.getTransferable();
          DataFlavor[] flavors = tr.getTransferDataFlavors();
          for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].isFlavorJavaFileListType()) {
              dtde.acceptDrop(DnDConstants.ACTION_COPY);
              List<?> list = (List<?>) tr.getTransferData(flavors[i]);
              for (int j = 0; j < list.size(); j++) {
                String filename = list.get(j).toString();
                if (filename != null && filename.length() != 0) {
                  if (filename.endsWith(".xml")) {
                    File f = new File(filename);
                    System.out.println("URL: " + f.toURI().toString());
                    dtde.dropComplete(true);
                    controller.load(new FileInputStream(f));
                  }
                  break;
                }
              }
              return;
            }
          } // for flavors
          // The user must not have dropped a file list.
          dtde.rejectDrop();
        }
        catch (Exception e) {
          dtde.rejectDrop();
        }
      }
    });
  }

  private void initLayout() {
    setOpaque(false);
    setLayout(null);
  }

  private void initPopupMenu() {
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
        new KeyEventDispatcher() {
          public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
              if (e.isShiftDown() && e.isControlDown() && e.isAltDown()) {
                isDesignMenuEnabled = true;
              }
            }
            else if (e.getID() == KeyEvent.KEY_RELEASED) {
              isDesignMenuEnabled = false;
            }
            return false;
          }
        });

    location = new Point();
    addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent me) {
        showPopup(me);
      }

      @Override
      public void mouseReleased(MouseEvent me) {
        showPopup(me);
      }

      private void showPopup(MouseEvent me) {
        if (me.isPopupTrigger()) {
          JPopupMenu currentPopup = getCurrentPopup();
          if (currentPopup != null) {
            location.setLocation(me.getPoint());
            currentPopup.show(JRuntimeDesigner.this, me.getX(), me.getY());
          }
        }
      }
    });
  }

  private JPopupMenu getCurrentPopup() {
    if (controller.isDesignMode()) {
    	if(designPopup == null) createDesignPopupMenu();
      return designPopup;
    }
    else {
      if (controller.isDesignModeSupported() && isDesignMenuEnabled) {
    	  if(runtimePopup == null) createRuntimePopupMenu();
        return runtimePopup;
      }
      else {
        return null;
      }
    }
  }

  public final boolean isShowGrid() {
    return isShowGrid & controller.isDesignMode();
  }

  public final void setShowGrid(boolean isShowGrid) {
    this.isShowGrid = isShowGrid;
    repaint();
  }

 
  @Override
  public void paint(Graphics g) {
    offScreenGraphics = g;

    paintGrid(offScreenGraphics);
    super.paint(offScreenGraphics);
    if (selectionManager != null) {
      selectionManager.paintSelection(offScreenGraphics);
    }

  }

  private void paintGrid(Graphics g) {
    if (!isShowGrid()) {
      return;
    }
    Graphics2D g2d = (Graphics2D) g;

    Rectangle r = getBounds();
    Color c = getBackground();
    if (c == null)
      c = Color.lightGray;
    g.setColor(c);
    if (r != null) {
      g.fillRect(r.x, r.y, r.width, r.height);
    }
    else {
      g.fillRect(0, 0, getWidth(), getHeight());
    }

    Rectangle bounds = getBounds();
    Color color = g2d.getColor();
    g2d.setColor(Color.red);
    Stroke oldStroke = g2d.getStroke();
    g2d.setStroke(gridStroke);
    g2d.setStroke(oldStroke);
    g2d.setColor(Color.lightGray);
    int gridSize = 7;
    for (int x = 0; x < this.getWidth(); x = x + gridSize) {
      g2d.drawLine(x, 0, x, getHeight());
    }
    for (int y = 0; y < this.getHeight(); y = y + gridSize) {
      g2d.drawLine(0, y, bounds.width, y);
    }
    g2d.setColor(color);
  }

  public final JRuntimeDesignerController getWidgetController() {
    return controller;
  }

  public void load(InputStream is) throws XmlException {
    controller.load(is);
  }

  public void load(Element rootElement) throws XmlException {
	String layoutName = XmlUtils.getNEAttribute(rootElement, "layoutName");
	setLayoutName(layoutName!=null?layoutName:"Canvas");
    controller.load(rootElement);
  }

  public void save(OutputStream out) throws XmlException {
    controller.save(out);
  }

  public void load() throws LayoutDataProviderException {
    controller.load();
  }

  public void save() throws LayoutDataProviderException {
    controller.save();
  }

  public Element toXML(Document doc) {
    return controller.toXML(doc,layoutName);
  }

  public void setDesignMode(boolean isDesignMode) {
    controller.setDesignMode(isDesignMode);
  }

  public void setDesignModeSupported(boolean isDesignModeSupported) {
    controller.setDesignModeSupported(isDesignModeSupported);
  }

  public void autoscroll(Point location) {
    scrollSupport.autoscroll(location);
  }

  public Insets getAutoscrollInsets() {
    return scrollSupport.getAutoscrollInsets();
  }

  public JComponent getWidget(String widgetType, String Id) {
    return controller.getModel().getWidget(widgetType, Id);
  }

  public List<JComponent> getWidget(String widgetType) {
    return controller.getModel().getWidget(widgetType);
  }

  public void setTargetResolution(Dimension dimension) {
    targetResolution = dimension;
    setPreferredSize(dimension);
    repaint();
  }

  public Dimension getTargetResolution() {
    return targetResolution;
  }

  public void addPanelStateListener(IPanelStateListener listener) {
    controller.addPanelStateListener(listener);
  }

  public void removePanelStateListener(IPanelStateListener listener) {
    controller.removePanelStateListener(listener);
  }

  public void addWidgetStateListener(Class<JComponent> widgetClass, IWidgetStateListener l) {
    controller.addWidgetStateListener(widgetClass, l);
  }

  public void removeWidgetStateListener(Class<JComponent> widgetClass) {
    controller.removeWidgetStateListener(widgetClass);
  }

  public boolean isDesignMode() {
    return controller.isDesignMode();
  }

  public JRuntimeDesignerModel getModel() {
    return controller.getModel();
  }

  public final WidgetSelectionManager getSelectionManager() {
    return selectionManager;
  }

  public void setSelectionManager(WidgetSelectionManager selectionManager) {
    this.selectionManager = selectionManager;
  }

  public void setLocked(boolean value) {
    controller.setLocked(value);
  }

  public List<IWidget> getWidgets() {
    return controller.getWidgets();
  }

  public boolean hasChanges() {
    return controller.hasChanges();
  }

  public void setLayoutDataProvider(ILayoutDataProvider layoutProvider) {
    controller.setLayoutDataProvider(layoutProvider);
  }

  public Action[] getToolbarActions() {
    ActionMap map = getActionMap();
    return new Action[]{map.get(NewAction.NAME),

    map.get(LoadAction.NAME),

    map.get(SaveAction.NAME),

    null,

    map.get(ImportAction.NAME),

    map.get(ExportAction.NAME),

    null,

    map.get(ResetAction.NAME),

    null,

    map.get(UndoAction.NAME),

    map.get(RedoAction.NAME),

    null,

    map.get(ToggleModeAction.NAME),

    null,

    map.get(RemoveWidgetAction.NAME),

    null,

    map.get(AlignTopAction.NAME),

    map.get(AlignBottomAction.NAME),

    map.get(AlignLeftAction.NAME),

    map.get(AlignRightAction.NAME),

    null,

    map.get(AlignSizeAction.NAME),

    map.get(AlignWidthAction.NAME),

    map.get(AlignHeightAction.NAME),

    null,

    map.get(MoveFrontAction.NAME),

    map.get(MoveBackAction.NAME),

    map.get(MoveForwardsAction.NAME),

    map.get(MoveBackwardsAction.NAME),

    null,

    map.get(LockAction.NAME),

    map.get(UnlockAction.NAME)

    };
  }

  /**
   * By invoking this method you indicate that <code>RuntimeDesigner</code> is
   * able to work in both: design and runtime mode. In a design mode it's
   * possible to change layout but that's impossible in a runtime mode. You have
   * to call this method before creating <code>RuntimeDesigner</code>.
   */
  public static void enableRuntimeModeSwitching() {
    System.setProperty(DESIGN_MODE_PROPERTY, Boolean.TRUE.toString());
  }
}
