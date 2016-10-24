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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * @author ikunin
 * @since 1.0
 */
public class XmlUtils {
	private static DocumentBuilderFactory factory = null;
	private static DocumentBuilder builder = null;

	public static synchronized javax.xml.parsers.DocumentBuilder getDomBuilder() throws XmlException {
		try {
			if (builder == null) {
				if (factory == null) {
					factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
					factory.setNamespaceAware(true);
					// factory.setValidating(true);
				}
				builder = factory.newDocumentBuilder();
			}
			return builder;
		} catch (javax.xml.parsers.ParserConfigurationException e) {
			throw new XmlException(e);
		}
	}

	public static synchronized org.w3c.dom.Document getNewDocument() throws XmlException {
		return getDomBuilder().newDocument();
	}

	public static Element load(String filename) throws XmlException {
		try {
			return load(new FileInputStream(new File(filename)));
		} catch (java.io.IOException e) {
			throw new XmlException(e);
		}
	}

	public static Element load(InputStream is) throws XmlException {
		try {
			DocumentBuilder builder = getDomBuilder();
			XmlValidatorResult result = new XmlValidatorResult();
			builder.setErrorHandler(new SummaryErrorHandler(result));
			org.w3c.dom.Document doc = builder.parse(is);
			if (result.hasError()) {
				throw new XmlException(result.toString());
			} else {
				return doc.getDocumentElement();
			}
		} catch (org.xml.sax.SAXException e) {
			throw new XmlException(e);
		} catch (java.io.IOException e) {
			throw new XmlException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignore) {
				}
			}
		}
	}

	public static void save(Document doc, String filename, String encoding) throws XmlException {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(new File(filename)));
			save(doc, out, encoding);
		} catch (java.io.IOException e) {
			throw new XmlException(e.getMessage(), e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException ignore) {
				}
			}
		}
	}

	public static void save(Document doc, OutputStream out, String encoding) throws XmlException {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			if(encoding !=null) t.setOutputProperty(OutputKeys.ENCODING,encoding);
			t.setOutputProperty(OutputKeys.INDENT,"yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			t.transform(new DOMSource(doc), new StreamResult(out));
		} catch (Exception e) {
			throw new XmlException(e.getMessage(), e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException ignore) {
				}
			}
		}
	}

	public static String toString(Document doc, String encoding) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			save(doc,bos,encoding);
			return bos.toString();
		} catch (Exception e) {
			return "Error: " + e.getMessage();
		}
	}

	public static String getAttrString(Element root, String attrName, boolean isOptional) throws XmlException {
		String value = root.getAttribute(attrName);
		if (!isOptional && (value == null || value.length() == 0))
			throw new XmlException("Mandatory attribute [" + attrName + "] doesn't exist!");
		return value;
	}

	/**
	 * Get non empty attribute. If it is null or empty a <code>null</code> value
	 * is returned.
	 * 
	 * @return
	 */
	public static String getNEAttribute(Element element, String field) {
		if (element == null || field == null || element.getAttribute(field) == null
				|| element.getAttribute(field).equals("")) {
			return null;
		}

		return element.getAttribute(field);
	}

	public static Integer getAttrInteger(Element root, String attrName, boolean isOptional) throws XmlException {
		String value = getAttrString(root, attrName, isOptional);
		if (value == null || value.length() == 0) {
			return null;
		}
		int val = 0;
		try {
			val = Integer.parseInt(value);
			return new Integer(val);
		} catch (NumberFormatException ex) {
			if (isOptional)
				throw new XmlException("Optional attribute [" + attrName + "] of element [" + root.getNodeName()
						+ "] is not an integer number!");
			else
				throw new XmlException("Mandatory attribute [" + attrName + "] of element [" + root.getNodeName()
						+ "] is not an integer number!");
		}
	}

	public static Element getElementByTagName(Element rootElement, String name) {
		NodeList elementsByTagName = rootElement.getElementsByTagName(name);
		for (int i = 0; i < elementsByTagName.getLength(); i++) {
			Node childElement = elementsByTagName.item(i);
			if (childElement.getNodeType() != Node.ELEMENT_NODE)
				continue;

			return (Element) childElement;
		}
		return null;
	}

	public static List<Element> getElementsByTagName(Element rootElement, String name) {
		List<Element> lst = new ArrayList<Element>();
		NodeList elementsByTagName = rootElement.getElementsByTagName(name);
		for (int i = 0; i < elementsByTagName.getLength(); i++) {
			Node childElement = elementsByTagName.item(i);
			if (childElement.getNodeType() != Node.ELEMENT_NODE)
				continue;
		      Node parentNode = childElement.getParentNode();
		      if (parentNode != rootElement) {
		        continue;
		      }
			lst.add((Element) childElement);
		}
		return lst;
	}
	public static void xmlToJComponent(JComponent component,Element element){
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(component.getClass());
			for(PropertyDescriptor pd:beanInfo.getPropertyDescriptors()){
				if(pd.getWriteMethod() != null && 
					pd.getWriteMethod().getParameterTypes().length == 1 && 
					PropertyConverter.isSupport(pd.getPropertyType()) &&
					element.hasAttribute(pd.getName())){
					try {
						Object value = PropertyConverter.toObject(element.getAttribute(pd.getName()), pd.getPropertyType());
						if(value != null){
							pd.getWriteMethod().invoke(component, value);
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
			
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void xmlFromJComponent(JComponent component,Element element,JComponent component0){
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(component.getClass());
			for(PropertyDescriptor pd:beanInfo.getPropertyDescriptors()){
				if(pd.getReadMethod() != null && 
					pd.getReadMethod().getParameterTypes().length == 0 && 
					PropertyConverter.isSupport(pd.getPropertyType())){
					try {
						Object value = pd.getReadMethod().invoke(component);
						Object value0 = pd.getReadMethod().invoke(component0);
						if(value != null && !value.equals(value0)){
							element.setAttribute(pd.getName(), PropertyConverter.fromObject(value));
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
			
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * @author ikunin
	 * @author $Author: $ (Last change)
	 * @version $Revision: 1 $ $Date: $
	 * @since 1.0
	 */
	static class SummaryErrorHandler implements ErrorHandler {

		private XmlValidatorResult result;

		SummaryErrorHandler(XmlValidatorResult result) {
			this.result = result;
		}

		public void error(SAXParseException ex) {
			addError(ex, XmlValidationError.ERROR_TYPE_ERROR);
		}

		public void fatalError(SAXParseException ex) {
			addError(ex, XmlValidationError.ERROR_TYPE_FATAL);
		}

		public void warning(SAXParseException ex) {
			addError(ex, XmlValidationError.ERROR_TYPE_WARNING);
		}

		private void addError(SAXParseException ex, int errorType) {
			result.addError(errorType, ex.getMessage(), ex.getLineNumber(), ex.getColumnNumber());
		}
	}

	static class XmlValidationError {
		public static final int ERROR_TYPE_UNKNOWN = 0;
		public static final int ERROR_TYPE_ERROR = 1;
		public static final int ERROR_TYPE_FATAL = 2;
		public static final int ERROR_TYPE_WARNING = 3;
		static final String[] ERROR_TYPE_NAMES = new String[] { "UNKNOWN", "ERROR", "FATAL", "WARNING" };

		private int errorType;

		private String error;
		private int errorLine;
		private int errorPosition;

		/**
		 * Interner Konstruktor
		 * 
		 * @param error
		 *            Fehlermeldung
		 * @param errorLine
		 *            Zeilennummer, wo der Fehler aufgetreten ist
		 * @param errorPosition
		 *            Zeichennummer innerhalb der fehlerhaften Zeile
		 */
		XmlValidationError(int errorType, String error, int errorLine, int errorPosition) {
			this.errorType = errorType;
			this.error = error;
			this.errorLine = errorLine;
			this.errorPosition = errorPosition;
		}

		/**
		 * @return Liefert die Fehlermeldung zur�ck
		 */
		public String getError() {
			return error;
		}

		/**
		 * @return Liefert die Zeilennummer zur�ck, wo der Fehler aufgetreten ist
		 */
		public int getErrorLine() {
			return errorLine;
		}

		/**
		 * @return Liefert die Zeichennummer innerhalb der fehlerhaften Zeile
		 *         zur�ck
		 */
		public int getErrorPosition() {
			return errorPosition;
		}

		/**
		 * @return Returns the errorType.
		 */
		public int getErrorType() {
			return errorType;
		}
	}

	static class XmlValidatorResult {

		private ArrayList<XmlValidationError> validationErrors;

		/**
		 * Es gibt keinen Fehler. Die Eigenschaft hasError wird auf false
		 * gesetzt.
		 */
		public XmlValidatorResult() {
			validationErrors = new ArrayList<XmlValidationError>();
		}

		/**
		 * @return gibt an, ob es bei der Validierung Fehler gab
		 */
		public boolean hasError() {
			return validationErrors.size() != 0;
		}

		/**
		 * F�gt eine neue Fehlermeldung zum Validierungsergebnis hinzu.
		 * 
		 * @param error
		 *            Fehlermeldung
		 * @param errorLine
		 *            Zeilennummer, wo der Fehler aufgetreten ist
		 * @param errorPosition
		 *            Zeichennummer innerhalb der fehlerhaften Zeile
		 */
		public void addError(int errorType, String error, int errorLine, int errorPosition) {
			validationErrors.add(new XmlValidationError(errorType, error, errorLine, errorPosition));
		}

		/**
		 * Liefert einen Iterator zur�ck, zum Auflisten s�mtlicher
		 * Fehlermeldungen. Die Elemente der Auflistung sind vom Type
		 * <code>XmlValidationError</code>
		 * 
		 * @return
		 */
		public Iterator<XmlValidationError> listErrors() {
			return validationErrors.iterator();
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			if (!hasError())
				sb.append("XML document is valid.\n");
			else {
				sb.append("XML document is not valid!\n");
				Iterator<XmlValidationError> it = listErrors();
				int max_error = 0;
				while (it.hasNext()) {
					XmlValidationError error = it.next();
					sb.append(XmlValidationError.ERROR_TYPE_NAMES[error.getErrorType()]);
					sb.append(" [ ").append(Integer.toString(error.getErrorLine())).append(", ");
					sb.append(Integer.toString(error.getErrorPosition())).append(" ] ");
					sb.append(error.getError()).append("\n");
					max_error--;
					if (max_error >= 5) {
						sb.append("...").append("\n");
						break;
					}
				}
			}

			return sb.toString();
		}
	}
}
