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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.utils.XmlException;


/**
 * Layout provider that stores the layouts as a file and offers dialogs for
 * selection the file to open or selecting the name of the file for saving.
 * 
 * @author ikunin
 * @author $Author: ikunin $ (Last change)
 * @version $Revision: 10780 $ $Date: 2007-08-21 11:40:44 +0200 (Di, 21 Aug
 *          2007) $
 * @since 1.0
 */
public class FileLayoutDataProvider implements ILayoutDataProvider {
  private boolean useSelectionDialog;
  private File file;

  public FileLayoutDataProvider() {
    useSelectionDialog = true;
  }

  public FileLayoutDataProvider(File file) {
    useSelectionDialog = false;
    if (file == null) {
      throw new IllegalArgumentException("Mandatory parameter 'file' is null!");
    }
    this.file = file;
  }

  protected File getFileForLoading(JRuntimeDesignerController controller) {
    if (useSelectionDialog) {
      JFileChooser fileChooser = new JFileChooser(new File("."));
      fileChooser.setFileFilter(new FileFilter() {
        public boolean accept(File f) {
          if (f.getName().endsWith(".xml")) {
            return true;
          }
          return false;
        }

        public String getDescription() {
          return "Runtime designer file (*.xml)";
        }
      });
      if (fileChooser.showOpenDialog(controller.getPanel()) == JFileChooser.APPROVE_OPTION) {
        file = fileChooser.getSelectedFile();
      }
      else {
        file = null;
      }
    }
    return file;
  }

  public void load(JRuntimeDesignerController controller)
      throws LayoutDataProviderException {
    File selectedFile = getFileForLoading(controller);
    if (selectedFile != null) {
      try {
        controller.load(new FileInputStream(selectedFile));
      }
      catch (FileNotFoundException ex) {
        throw new LayoutDataProviderException(ex.getMessage(), ex);
      }
      catch (XmlException ex) {
        throw new LayoutDataProviderException(ex.getMessage(), ex);
      }
    }
  }

  protected File getFileForSaving(JRuntimeDesignerController controller) {
    if (useSelectionDialog) {
      JFileChooser fileChooser = new JFileChooser(new File("."));
      fileChooser.setFileFilter(new FileFilter() {
        public boolean accept(File f) {
          if (f.getName().endsWith(".xml")) {
            return true;
          }
          return false;
        }

        public String getDescription() {
          return "Runtime designer file (*.xml)";
        }
      });
      if (fileChooser.showSaveDialog(controller.getPanel()) == JFileChooser.APPROVE_OPTION) {
        file = fileChooser.getSelectedFile();
      }
      else {
        file = null;
      }
    }
    return file;
  }

  public void save(JRuntimeDesignerController controller)
      throws LayoutDataProviderException {
    File selectedFile = getFileForSaving(controller);
    if (selectedFile != null) {
      try {
        String filename = selectedFile.getCanonicalPath();
        if (!filename.endsWith(".xml")) {
          filename += ".xml";
        }
        controller.save(new FileOutputStream(filename));
      }
      catch (IOException ex) {
        throw new LayoutDataProviderException(ex.getMessage(), ex);
      }
      catch (XmlException ex) {
        throw new LayoutDataProviderException(ex.getMessage(), ex);
      }
    }
  }

  public boolean isUseSelectionDialog() {
    return useSelectionDialog;
  }

  public void setUseSelectionDialog(boolean useSelectionDialog) {
    this.useSelectionDialog = useSelectionDialog;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

}
