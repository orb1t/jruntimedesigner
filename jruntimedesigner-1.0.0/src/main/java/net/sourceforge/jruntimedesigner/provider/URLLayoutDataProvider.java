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

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.utils.XmlException;


/**
 * Layout provider that stores the layouts as a file and offers dialogs for
 * selection the file to open or selecting the name of the file for saving.
 * 
 * @author ikunin
 * @author $Author: ikunin $ (Last change)
 * @version $Revision: 10780 $ $Date: 2007-08-17 14:32:53 +0200 (Fr, 17 Aug
 *          2007) $
 * @since 1.0
 */
public class URLLayoutDataProvider implements ILayoutDataProvider {
  private URL fileURL;

  public URLLayoutDataProvider(URL fileURL) {
    this.fileURL = fileURL;

  }

  public void load(JRuntimeDesignerController controller)
      throws LayoutDataProviderException {
    try {
      controller.load(fileURL.openStream());
    }
    catch (IOException ex) {
      throw new LayoutDataProviderException(ex.getMessage(), ex);
    }
    catch (XmlException ex) {
      throw new LayoutDataProviderException(ex.getMessage(), ex);
    }
  }

  public void save(JRuntimeDesignerController controller)
      throws LayoutDataProviderException {
    try {
      URLConnection urlCon = fileURL.openConnection();
      urlCon.setDoOutput(true);
      controller.save(fileURL.openConnection().getOutputStream());
    }
    catch (IOException ex) {
      throw new LayoutDataProviderException(ex.getMessage(), ex);
    }
    catch (XmlException ex) {
      throw new LayoutDataProviderException(ex.getMessage(), ex);
    }
  }
}
