package net.sourceforge.jruntimedesigner.widgets.swing;

import java.awt.Dimension;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.sourceforge.jruntimedesigner.JRuntimeDesigner;
import net.sourceforge.jruntimedesigner.common.IWidgetContainer;
import net.sourceforge.jruntimedesigner.utils.XmlException;
import net.sourceforge.jruntimedesigner.utils.XmlUtils;
import net.sourceforge.jruntimedesigner.widgets.AbstractWidget;
import net.sourceforge.jruntimedesigner.widgets.AbstractWidgetProvider;
import net.sourceforge.jruntimedesigner.widgets.IWidget;
import net.sourceforge.jruntimedesigner.widgets.IWidgetProvider;

/**
 * @author wolcen
 *
 */
public abstract class SwingContainerWidgetProvider<T extends JComponent> extends AbstractWidgetProvider {

	@SuppressWarnings("unchecked")
	public Class<T> getTClass() {
		Type type = getClass().getGenericSuperclass();
		Type[] generics = ((ParameterizedType) type).getActualTypeArguments();
		Class<T> mTClass = (Class<T>) (generics[0]);
		return mTClass;
	}

	public String getType() {
		return getTClass().getSimpleName();
	}

	public ImageIcon getIcon() {
		return new ImageIcon(this.getClass().getResource("canvas.gif"));
	}

	public IWidget createWidget() {
		return new SwingContainerWidget<T>(this,getTClass());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initWidget(IWidget widget) {
		super.initWidget(widget);
		initialize((SwingContainerWidget<T>)widget);
	}


	protected void initialize(SwingContainerWidget<T> widget) {
		widget.getComponent().setPreferredSize(new Dimension(100, 40));
	}

	public JMenu createSpecializedMenu(SwingContainerWidget<T> widget) {
		return null;
	}

	public void doSpecializedAction(SwingContainerWidget<T> widget, String command) {

	}

	static class SwingContainerWidget<T extends JComponent> extends AbstractWidget implements IWidgetContainer{
		private int editorIndex = 0;
		private JComponent component;
		private Class<T> clazz;
		private Map<String,JRuntimeDesigner> editors = new HashMap<String,JRuntimeDesigner>();

		public SwingContainerWidget(IWidgetProvider widgetProvider,Class<T> clazz) {
			super(widgetProvider);
			this.clazz = clazz;
		}

		public JComponent getComponent() {
			if (component == null) {
				try {
					component = clazz.newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return component;
		}
		
		public int getNextEditorIndex(){
			editorIndex ++;
			return editorIndex;
		}
		public JRuntimeDesigner createEditor(String layoutName){
			JRuntimeDesigner editor = new JRuntimeDesigner(false);
			editor.setLayoutName(layoutName);
			editor.setLayoutTitle(layoutName);
			editor.setDesignMode(true);
			editor.setShowGrid(false);
			editor.setOpaque(false);
			editors.put(layoutName, editor);
			return editor;
		}
		
		public JRuntimeDesigner getEditor(String layoutName) {
			return editors.get(layoutName);
		}
		public void removeEditor(String layoutName) {
			editors.remove(layoutName);
		}
		
		public Set<String> getEditorKeys() {
			return editors.keySet();
		}
		

		public Map<String, JRuntimeDesigner> getEditors() {
			return editors;
		}

		@Override
		public void setDesignMode(boolean value) {
			super.setDesignMode(value);
			for(JRuntimeDesigner editor:editors.values()){
				editor.setDesignMode(value);
			}
		}

		@Override
		public void setLocked(boolean isLocked) {
			super.setLocked(isLocked);
			for(JRuntimeDesigner editor:editors.values()){
				editor.setLocked(isLocked);
			}
		}


		@Override
		public List<IWidget> getWidgets() {
			List<IWidget> lst = new ArrayList<IWidget>();
			for(JRuntimeDesigner editor:editors.values()){
				lst.addAll(editor.getWidgets());
			}
			return lst;
		}

		@Override
		public JMenu createWidgetsMenu() {
			if(editors.size() == 1){
				for(String layoutName:editors.keySet()){
					JMenu menu = editors.get(layoutName).createWidgetsMenu();
					return menu;
				}
				
			}else if(editors.size() > 1){
				JMenu menu = new JMenu("Add Widget");
				for(String layoutName:editors.keySet()){
			    	JRuntimeDesigner editor = editors.get(layoutName);
					JMenu menu1 = new JMenu(editor.getLayoutTitle());
					List<Action> acts = editor.createWidgetsMenuAction();
			    	for(Action act:acts){
			    		menu1.add(act);
			    	}
					menu.add(menu1);
				 }
				 return menu;
			}
			return null;
		}

		public JMenu createSpecializedMenu() {
			return ((SwingContainerWidgetProvider<T>) getWidgetProvider()).createSpecializedMenu(this);
		}

		public void doSpecializedAction(String command) {
			((SwingContainerWidgetProvider<T>) getWidgetProvider()).doSpecializedAction(this, command);
		}
	}

	public IWidget fromXML(Element element) throws XmlException {
		IWidget widget = super.fromXML(element);
		JComponent component = widget.getComponent();
		XmlUtils.xmlToJComponent(component, element);
		SwingContainerWidget<T> swingContainerWidget = (SwingContainerWidget<T>)widget;
		List<Element> lst = XmlUtils.getElementsByTagName(element, IWidgetProvider.PROVIDER_CANVAS);
		for(Element el:lst){
			String layoutName = XmlUtils.getNEAttribute(el, "layoutName");
			JRuntimeDesigner panel = swingContainerWidget.createEditor(layoutName);
			panel.load(el);
		}
		return widget;
	}

	public Element toXML(Document doc, IWidget widget) {
		Element el = super.toXML(doc, widget);
		JComponent component = widget.getComponent();
		JComponent component0 = createWidget().getComponent();
		XmlUtils.xmlFromJComponent(component, el, component0);
		SwingContainerWidget<T> swingContainerWidget = (SwingContainerWidget<T>)widget;
		for(JRuntimeDesigner panel:swingContainerWidget.getEditors().values()){
			el.appendChild(panel.toXML(doc));
		}
	return el;
	}
}
