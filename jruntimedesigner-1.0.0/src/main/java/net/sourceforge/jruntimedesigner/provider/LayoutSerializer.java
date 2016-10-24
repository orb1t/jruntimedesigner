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
package net.sourceforge.jruntimedesigner.provider;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class LayoutSerializer {

  public void encode() {
    try {
      // Serialize object into XML
      XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(
          "outfilename.xml")));
      encoder.writeObject(null);
      encoder.close();
    }
    catch (FileNotFoundException e) {
    }
  }

  public Object decode() throws FileNotFoundException {
    // Deserialize an object
      XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(
          "infilename.xml")));

      // MyClass is declared in e7 Serializing a Bean to XML
      Object o = (Object) decoder.readObject();
      decoder.close();
      return o;
  }

  public BeanInfo getBeanInfo(Class<?> _class) {
    try {
      // Make the props property transient
      BeanInfo info = Introspector.getBeanInfo(_class);
      PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
      for (int i = 0; i < propertyDescriptors.length; ++i) {
        PropertyDescriptor pd = propertyDescriptors[i];
        if (pd.getName().equals("props")) {
          pd.setValue("transient", Boolean.TRUE);
        }
      }
      return info;
    }
    catch (IntrospectionException e) {
      return null;
    }
  }
}
