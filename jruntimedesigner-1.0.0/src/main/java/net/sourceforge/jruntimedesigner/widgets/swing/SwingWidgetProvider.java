package net.sourceforge.jruntimedesigner.widgets.swing;

import java.awt.Dimension;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.sourceforge.jruntimedesigner.utils.PropertyConverter;
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
public abstract class SwingWidgetProvider<T extends JComponent> extends AbstractWidgetProvider {

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
		return new ImageIcon(this.getClass().getResource("bean.gif"));
	}

	public IWidget createWidget() {
		return new SwingWidget<T>(this, getTClass());
	}

	@Override
	public void initWidget(IWidget widget) {
		super.initWidget(widget);
		initialize((T) widget.getComponent());
	}

	protected void initialize(T component) {
		component.setPreferredSize(new Dimension(100, 40));
	}

	public JMenu createSpecializedMenu(T component) {
		return null;
	}

	public void doSpecializedAction(T component, String command) {

	}


	static class SwingWidget<T extends JComponent> extends AbstractWidget {
		private JComponent component;
		private Class<T> clazz;

		public SwingWidget(IWidgetProvider widgetProvider, Class<T> clazz) {
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

		public JMenu createSpecializedMenu() {
			return ((SwingWidgetProvider<T>) getWidgetProvider()).createSpecializedMenu((T) component);
		}

		public void doSpecializedAction(String command) {
			((SwingWidgetProvider<T>) getWidgetProvider()).doSpecializedAction((T) component, command);
		}
	}
	public IWidget fromXML(Element element) throws XmlException {
		IWidget widget = super.fromXML(element);
		JComponent component = widget.getComponent();
		XmlUtils.xmlToJComponent(component, element);
		return widget;
	}

	public Element toXML(Document doc, IWidget widget) {
		Element el = super.toXML(doc, widget);
		JComponent component = widget.getComponent();
		JComponent component0 = createWidget().getComponent();
		XmlUtils.xmlFromJComponent(component, el, component0);
		return el;
	}
}
