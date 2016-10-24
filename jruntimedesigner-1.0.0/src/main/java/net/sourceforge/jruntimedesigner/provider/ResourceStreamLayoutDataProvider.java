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

import java.io.InputStream;

import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.utils.XmlException;


/**
 * Data provider for loading the layouts from a resource loaded by a class
 * loader of a provided class.
 * 
 * @author dleszyk
 * @since 1.0
 */
public class ResourceStreamLayoutDataProvider implements ILayoutDataProvider {

  private final Class<?> relativeClass;
  private final String resourceName;

  public ResourceStreamLayoutDataProvider(Class<?> relativeClass, String resourceName) {
    this.relativeClass = relativeClass;
    this.resourceName = resourceName;
  }

  public void load(JRuntimeDesignerController controller) throws LayoutDataProviderException {
    InputStream stream = relativeClass.getResourceAsStream(resourceName);
    if (stream == null) {
        throw new IllegalStateException("Can't load resource: " + resourceName);
    }
    
    try {
      controller.load(stream);
    }
    catch (XmlException e) {
      throw new LayoutDataProviderException(e.getCause());
    }
  }

  public void save(JRuntimeDesignerController controller) throws LayoutDataProviderException {
    throw new UnsupportedOperationException("ResourceStreamLayoutProvider doesn't support saving!");
  }

}
