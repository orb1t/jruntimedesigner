package net.sourceforge.jruntimedesigner.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import com.l2fprod.common.beans.BeanBinder;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;

import net.sourceforge.jruntimedesigner.common.WidgetHolder;
import net.sourceforge.jruntimedesigner.widgets.IWidget;

public class WidgetPropertiesDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private PropertySheetPanel propsheetWidget;
	private JPanel buttonPane;
	private JButton okButton;
	private JButton cancelButton;
	private WidgetHolder widgetHolder;
	private JTabbedPane tabbedPane;


	public WidgetPropertiesDialog(JComponent comp, String title) {
		super(getFrame(comp), title);
		initComponents();
	}
	private void initComponents() {
		setModal(true);
		setBounds(100, 100, 427, 477);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPanel.add(this.tabbedPane);
		propsheetWidget = new PropertySheetPanel();
		this.tabbedPane.addTab("Widget", null, this.propsheetWidget, null);
		this.propsheetWidget.setMode(PropertySheet.VIEW_AS_CATEGORIES);
		this.propsheetWidget.setToolBarVisible(true);
		this.propsheetWidget.setSortingProperties(true);
		this.propsheetWidget.setSortingCategories(true);
		this.propsheetWidget.setSorting(true);
		this.propsheetWidget.setDescriptionVisible(false);
		
		buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		okButton = new JButton("OK");
		this.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_okButton_actionPerformed(arg0);
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_cancelButton_actionPerformed(arg0);
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
	}

	protected void do_cancelButton_actionPerformed(ActionEvent arg0) {
		this.dispose();
	}
	protected void do_okButton_actionPerformed(ActionEvent arg0) {
		this.dispose();
	}
	
	public void initialize(WidgetHolder widgetHolder){
		this.widgetHolder = widgetHolder;
		final IWidget widget = widgetHolder.getWidget();
		
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(widget.getClass());
			propsheetWidget.setBeanInfo(beanInfo);
	        Property[] properties = propsheetWidget.getProperties();
	        for (int i = 0, c = properties.length; i < c; i++) {
	            try {
	                properties[i].readFromObject(widget);
	            } catch (Exception e) {
	            }
	        }
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		propsheetWidget.addPropertySheetChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
                Property prop = (Property) evt.getSource();
                prop.writeToObject(widget);
				doPropertyChange(evt);
			}
		});
		
			PropertySheetPanel propsheetComponent = new PropertySheetPanel();
			propsheetComponent.setToolBarVisible(true);
			propsheetComponent.setSortingProperties(true);
			propsheetComponent.setSortingCategories(true);
			propsheetComponent.setSorting(true);
			propsheetComponent.setMode(1);
			propsheetComponent.setDescriptionVisible(false);
			tabbedPane.addTab("Component", null, propsheetComponent, null);
			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(widget.getComponent().getClass());
				propsheetComponent.setBeanInfo(beanInfo);
		        Property[] properties = propsheetComponent.getProperties();
		        for (int i = 0, c = properties.length; i < c; i++) {
		            try {
		                properties[i].readFromObject(widget.getComponent());
		            } catch (Exception e) {
		            }
		        }
			} catch (IntrospectionException e) {
				e.printStackTrace();
			}
			
			propsheetComponent.addPropertySheetChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
	                Property prop = (Property) evt.getSource();
	                prop.writeToObject(widget.getComponent());
					doPropertyChange(evt);
				}
			});
		
	}
	
	public void doPropertyChange(PropertyChangeEvent evt) {
		System.out.println(evt.getPropertyName()+" -- " + evt.getNewValue());
		widgetHolder.repaint();
	}
	
	static public Frame getFrame(Component c) {
		if (c instanceof Frame || null == c)
			return c == null ? null : (Frame) c;
		return getFrame(c.getParent());
	}
}
