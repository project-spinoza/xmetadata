package org.projectspinoza.dd.xmetadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App {
  private static Logger LOG = LogManager.getRootLogger();

  public static void main(String[] args) {
    LOG.info("Hello World!");

    Properties settings = new Properties();
    settings.put("jdbc_driver", "com.mysql.jdbc.Driver");
    settings.put("db_host", "localhost");
    settings.put("db_port", 3306);
    settings.put("db_name", "student");
    settings.put("db_user", "root");
    settings.put("db_pass", "");

    XmetaTest xMeta = null;
    try {
      xMeta = new XmetaTest(settings);
      xMeta.connect();

      //. printInfo2(xMeta.getConnection());
      printInfo(xMeta.getConnection());

      xMeta.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private static void printInfo(Connection connection) throws Exception {

    DatabaseMetaData databaseMetaData = connection.getMetaData();

    /*** basic database info ***/
    int majorVersion = databaseMetaData.getDatabaseMajorVersion();
    int minorVersion = databaseMetaData.getDatabaseMinorVersion();

    String productName = databaseMetaData.getDatabaseProductName();
    String productVersion = databaseMetaData.getDatabaseProductVersion();

    LOG.info("majorVersion: {}, minorVersion: {}, productName: {}, productVersion: {}", majorVersion, minorVersion, productName, productVersion);

    /*** basic database driver info ***/
    int driverMajorVersion = databaseMetaData.getDriverMajorVersion();
    int driverMinorVersion = databaseMetaData.getDriverMinorVersion();

    LOG.info("driverMajorInfo: {}, driverMinorVersion: {}", driverMajorVersion, driverMinorVersion);

    /*** list dbs ***/
    ResultSet rs = connection.getMetaData().getCatalogs();

    while (rs.next()) {
      LOG.info("TABLE_CAT = {}", rs.getString("TABLE_CAT"));
    }
    rs.close();

    /*** db exists ***/
    String dbToSearch = "student";
    boolean found = false;
    rs = connection.getMetaData().getCatalogs();
    while (rs.next()) {
      // Get the database name, which is at position 1
      String databaseName = rs.getString(1);
      if (databaseName.equals(dbToSearch)) {
        found = true;
        break;
      }
    }
    rs.close();
    if (found) {
      LOG.info("{}, exists", dbToSearch);
    } else {
      LOG.info("{}, does not exists", dbToSearch);
    }

    /*** listing tables ***/
    String catalog = null;
    String schemaPattern = null;
    String tableNamePattern = null;
    String[] types = null;

    rs = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);
    while (rs.next()) {
      // . This ResultSet contains 10 columns, which each contain information
      // about the given table. The column with index 3 contains the table name
      // itself
      String tableName = rs.getString(3);
      LOG.info(tableName);
    }
    rs.close();

    /*** Listing column types in a table ***/
    catalog = null;
    schemaPattern = null;
    tableNamePattern = "student";
    String columnNamePattern = null;

    rs = databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);

    while (rs.next()) {
      String columnName = rs.getString(4);
      // . columnType is enumeration of java.sql.Types
      int columnType = rs.getInt(5);
      LOG.info("columnName: {}, columnType: {}", columnName, columnType);
    }
    rs.close();

    /*** primary key for table ***/
    catalog = null;
    String schema = null;
    String tableName = "student";

    rs = databaseMetaData.getPrimaryKeys(catalog, schema, tableName);

    while (rs.next()) {
      String primaryKey = rs.getString(4);
      LOG.info("PrimaryKey: {}", primaryKey);
    }
    rs.close();

    /*** supported features ***/
    boolean supportsGetGeneratedKeys = databaseMetaData.supportsGetGeneratedKeys();
    boolean supportsGroupBy = databaseMetaData.supportsGroupBy();
    boolean supportsOuterJoins = databaseMetaData.supportsOuterJoins();

    /*** table exists ***/
    DatabaseMetaData meta = connection.getMetaData();
    rs = meta.getTables("student", null, "exprements", new String[] { "TABLE" });
    while (rs.next()) {
      System.out.println("   " + rs.getString("TABLE_CAT") + ", "
          + rs.getString("TABLE_SCHEM") + ", " + rs.getString("TABLE_NAME")
          + ", " + rs.getString("TABLE_TYPE") + ", " + rs.getString("REMARKS"));
    }
    rs.close();

    LOG.info("supportsGetGeneratedkeys: {}, supportsGroupBy: {}, supportsOuterJoins: {}", supportsGetGeneratedKeys, supportsGroupBy, supportsOuterJoins);
  }

  private static void printInfo2(Connection connection) throws Exception {
    DatabaseMetaData databaseMetaData = connection.getMetaData();

    /*** Listing column types in a table ***/
    String catalog = null;
    String schemaPattern = null;
    String tableNamePattern = "student";
    String columnNamePattern = null;

    ResultSet rs = databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);

    while (rs.next()) {
      LOG.info("COLUMN_NAME: {}, TYPE_NAME: {}", rs.getString("COLUMN_NAME"), rs.getString("TYPE_NAME"));
    }
    rs.close();
  }

  public void sqlTypes() {
  }
}
