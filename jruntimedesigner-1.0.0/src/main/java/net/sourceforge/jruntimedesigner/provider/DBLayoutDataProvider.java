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
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.utils.XmlException;


/**
 * Data provider for loading and storing the layouts in the database table as a
 * BLOB.
 * 
 * @author ikunin
 * @author $Author: ikunin $ (Last change)
 * @version $Revision: 11616 $ $Date: 2007-08-17 15:40:19 +0200 (Fr, 17 Aug
 *          2007) $
 * @since 1.0
 */
public class DBLayoutDataProvider implements ILayoutDataProvider {
  private static final String TABLE_NAME = "GUI_LAYOUT";
  private String layoutID;
  private Connection connection;
  private ILayoutDataProvider fallbackProvider;

  /**
   * Creates a new layout provider for loading and storing the layouts in the
   * database. No fall back data provider is used.
   * 
   * @param layoutID
   * @param connection
   */
  public DBLayoutDataProvider(String layoutID, Connection connection) {
    this(layoutID, connection, null);
  }

  /**
   * Creates a new layout provider for loading and storing the layouts in the
   * database.
   * <p>
   * Optional fall back data provider will be used, if provided, if the layout
   * can't be loaded from the database.
   * </p>
   * 
   * @param layoutID
   * @param connection
   * @param fallbackProvider
   */
  public DBLayoutDataProvider(String layoutID, Connection connection,
      ILayoutDataProvider fallbackProvider) {
    if (layoutID == null || layoutID.length() == 0) {
      throw new IllegalArgumentException("Mandatory parameter layoutID is null or empty!");
    }
    if (connection == null) {
       throw new IllegalArgumentException("Mandatory parameter connection for loading the layout with ID " + layoutID
          + " from database is null!");
    }
    this.layoutID = layoutID;
    this.connection = connection;
    this.fallbackProvider = fallbackProvider;
  }

  public void load(JRuntimeDesignerController controller) throws LayoutDataProviderException {
    if (connection == null) {
      doFallBackLoad(controller);
      return;
    }
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      try {
        ps = connection.prepareStatement("SELECT LAYOUT FROM " + TABLE_NAME + " WHERE ID = ?");
        ps.setString(1, layoutID);
        rs = ps.executeQuery();
        if (rs.next()) {
          Blob blob = (Blob) rs.getObject(1);
          InputStream is = blob.getBinaryStream();
          controller.load(is);
        }
        else {
          throw new LayoutDataProviderException("Layout with the ID [" + layoutID + "] was found!");
        }
      }
      catch (SQLException ex) {
        throw new LayoutDataProviderException("Error loading layout with the ID [" + layoutID
            + "]\nReason: " + ex.getMessage(), ex);
      }
      catch (XmlException ex) {
        throw new LayoutDataProviderException("Error loading layout with the ID [" + layoutID
            + "]\nReason: " + ex.getMessage(), ex);
      }
      finally {
        if (ps != null) {
          try {
            ps.close();
          }
          catch (SQLException ignore) {
          }
        }
        if (rs != null) {
          try {
            rs.close();
          }
          catch (SQLException ignore) {
          }
        }
      }
    }
    catch (LayoutDataProviderException ex) {
      doFallBackLoad(controller);
    }
  }

  protected void doFallBackLoad(JRuntimeDesignerController controller)
      throws LayoutDataProviderException {
    if (fallbackProvider != null) {
      fallbackProvider.load(controller);
    }
  }

  public void save(JRuntimeDesignerController controller) throws LayoutDataProviderException {
    PreparedStatement selectPS = null;
    PreparedStatement insertPS = null;
    ResultSet rs = null;
    OutputStream out = null;
    try {

      // check if the layout is already available
      // connection.setAutoCommit(false);
      selectPS = connection
          .prepareStatement("SELECT LAYOUT FROM " + TABLE_NAME + " WHERE ID = ? for update");
      selectPS.setString(1, layoutID);
      rs = selectPS.executeQuery();
      Blob blob = null;
      if (rs.next()) {
        // the entry is already available
        blob = (Blob) rs.getObject(1);
      }
      else {
        // the new entry is to be inserted
        insertPS = connection.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES(?, empty_blob())");
        insertPS.setString(1, layoutID);
        int result = insertPS.executeUpdate();
        if (result != 1) {
          throw new LayoutDataProviderException(
              "Error saving layout to the database! Can't insert a new row for ID: " + layoutID);
        }
        rs = selectPS.executeQuery();
        if (rs.next()) {
          // the entry is already available
          blob = (Blob) rs.getObject(1);
        }
        else {
          throw new LayoutDataProviderException(
              "Error saving layout to the database! Can't get BLOB access for ID: " + layoutID);
        }
      }
      // ignore the previous stored data
      blob.truncate(0);
      // set the output stream beginning with the first byte
      out = blob.setBinaryStream(1L);
      controller.save(out);
      connection.commit();
    }
    catch (SQLException ex) {
      throw new LayoutDataProviderException("Error saving layout with the ID [" + layoutID
          + "]\nReason: " + ex.getMessage(), ex);
    }
    catch (XmlException ex) {
      throw new LayoutDataProviderException("Error saving layout with the ID [" + layoutID
          + "]\nReason: " + ex.getMessage(), ex);
    }
    finally {
      if (out != null) {
        try {
          out.close();
        }
        catch (IOException ignore) {
        }
      }
      if (selectPS != null) {
        try {
          selectPS.close();
        }
        catch (SQLException ignore) {
        }
      }
      if (insertPS != null) {
        try {
          insertPS.close();
        }
        catch (SQLException ignore) {
        }
      }
      if (rs != null) {
        try {
          rs.close();
        }
        catch (SQLException ignore) {
        }
      }
    }
  }

  protected void doFallBackSave(JRuntimeDesignerController controller)
      throws LayoutDataProviderException {
    if (fallbackProvider != null) {
      fallbackProvider.save(controller);
    }
  }

  public String getLayoutID() {
    return layoutID;
  }

  public Connection getConnection() {
    return connection;
  }
}
