package org.projectspinoza.dd.xmetadata.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.projectspinoza.dd.xmetadata.XmetaApi;
import org.projectspinoza.dd.xmetadata.XmetaResult;


public class MysqlApi implements XmetaApi {
  private static Logger LOG = LogManager.getRootLogger();
  
  private Connection connection;
  
  public MysqlApi(Connection connection){
    this.connection = connection;
  }
  
  @Override
  public XmetaResult listDatabases(){
    
    List<String> databases = new ArrayList<String>();
    XmetaResult<List<String>> response = new XmetaResult<List<String>>();
    response.setStatus(200);
    response.setTitle("ListDatabases");
    ResultSet rs = null;
    try {
      rs = connection.getMetaData().getCatalogs();
      while (rs.next()) {
        databases.add(rs.getString("TABLE_CAT"));
      }
      rs.close();
    } catch (SQLException e){
      LOG.error("Error: {}", e);
      response.setError(String.format("SQLException: %s", e.getMessage()));
      if(rs != null){
        try {rs.close();} catch (SQLException e1) { /** ignore it **/ }
      }
    }
    response.setResult(databases);
    return response;
  }

  @Override
  public XmetaResult listTables(String databaseName) {
    List<String> tables = new ArrayList<String>();
    XmetaResult<List<String>> response = new XmetaResult<List<String>>();
    response.setStatus(200);
    response.setTitle("ListTables[" + databaseName+"]");
    ResultSet rs = null;
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      String catalog = databaseName;
      String schemaPattern = null;
      String tableNamePattern = null;
      String[] types = null;
  
      rs = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);
      while (rs.next()) {
        tables.add(rs.getString(3));
      }
      rs.close();
    } catch (SQLException e){
      LOG.error("Error: {}", e);
      response.setError(String.format("SQLException: %s", e.getMessage()));
      if(rs != null){
        try {rs.close();} catch (SQLException e1) { /** ignore it **/ }
      }
    }
    response.setResult(tables);
    return response;
  }

  @Override
  public XmetaResult listColumns(String databaseName, String tableName) {
    String catalog = databaseName;
    String schemaPattern = null;
    String tableNamePattern = tableName;
    String columnNamePattern = null;
    
    List<String> columns = new ArrayList<String>();
    XmetaResult<List<String>> response = new XmetaResult<List<String>>();
    response.setStatus(200);
    response.setTitle("ListTableColumns[" + databaseName + "." + tableName + "]");
    ResultSet rs = null;
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      rs = databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
      
      while (rs.next()) {
        columns.add(rs.getString(4));
      }
      rs.close();
    } catch (SQLException e){
      LOG.error("Error: {}", e);
      response.setError(String.format("SQLException: %s", e.getMessage()));
      if(rs != null){
        try {rs.close();} catch (SQLException e1) { /** ignore it **/ }
      }
    }
    response.setResult(columns);
    return response;
  }

  @Override
  public XmetaResult listColumnsWithType(String databaseName, String tableName) {
    String catalog = databaseName;
    String schemaPattern = null;
    String tableNamePattern = tableName;
    String columnNamePattern = null;
    
    XmetaResult<Map<String, String>> response = new XmetaResult<Map<String, String>>();
    response.setStatus(200);
    response.setTitle("ListTableColumnsWithType[" + databaseName + "." + tableName + "]");
    ResultSet rs = null;
    Map<String, String> columnWithType = new HashMap<String, String>();
    
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      rs = databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
  
      while (rs.next()) {
        columnWithType.put(rs.getString("COLUMN_NAME"), rs.getString("TYPE_NAME"));
      }
      rs.close();
    } catch (SQLException e){
      LOG.error("Error: {}", e);
      response.setError(String.format("SQLException: %s", e.getMessage()));
      if(rs != null){
        try {rs.close();} catch (SQLException e1) { /** ignore it **/ }
      }
    }
    response.setResult(columnWithType);
    return response;
  }

  @Override
  public XmetaResult getColumnType(String databaseName, String tableName, String columnName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public XmetaResult getPrimaryKey(String databaseName, String tableName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public XmetaResult getForeignKey(String databaseName, String tableName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public XmetaResult getColumnLength(String databaseName, String tableName, String columnName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public XmetaResult getColumnAtPosition(String databaseName, int columnPosition) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public XmetaResult getForeignKeysReferencedTable(String databaseName, String tableName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public XmetaResult getIndexes(String databaseName, String tableName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public XmetaResult getAllIndexesAndReferencedTable(String databaseName, String tableName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean databaseExists(String databaseName) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean tableExists(String databaseName, String tableName) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean columnExists(String databaseName, String tableName, String columnName) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsGroupBy(String databaseName, String tableName) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsOuterJoins(String databaseName, String tableName) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsInnerJoins(String databaseName, String tableName) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public XmetaResult getDatabaseInfo() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public XmetaResult getDatabaseDriverInfo() {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void closeConnection(){
    try {
      connection.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
