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

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Utility class for resources.
 * 
 * @author ikunin
 */
public class ResourceUtils {

  public static ResourceBundle getActionsBundle(Class<?> _class) {
        return ResourceBundle.getBundle(_class.getPackage().getName() + ".Actions");
    }

    public static ResourceBundle getBundle(Class<?> _class) {
        return ResourceBundle.getBundle(_class.getName());
    }
   
    /**
     * Loading icon by relative path. If the icon can't be found
     * <code>null</code> is returned.
     * 
     * @param absolutePath
     * @return
     */
    public static Icon getIcon(String absolutePath) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL imageUrl = classLoader.getResource(absolutePath);
        if (imageUrl == null) {
            return null;
        }

        Icon icon = new ImageIcon(imageUrl);
        return icon;
    }
}
