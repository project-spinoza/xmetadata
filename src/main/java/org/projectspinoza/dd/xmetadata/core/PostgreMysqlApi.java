package org.projectspinoza.dd.xmetadata.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
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


public class PostgreMysqlApi implements XmetaApi {
  private static Logger LOG = LogManager.getRootLogger();
  
  private Connection connection;
  
  public PostgreMysqlApi(Connection connection){
    this.connection = connection;
  }
  
  @Override
  public XmetaResult listDatabases(){
    
    List<String> databases = new ArrayList<String>();
    PreparedStatement preparedStatement = null;
    XmetaResult<List<String>> response = new XmetaResult<List<String>>();
    response.setStatus(200);
    response.setTitle("ListDatabases");
    ResultSet rs = null;
    String query =null;
    try {
      String productName = connection.getMetaData().getDatabaseProductName().toLowerCase();
      if(productName.equals("mysql")){
        query = "SHOW DATABASES;";
      }else if(productName.equals("postgresql")) {
        query = "SELECT datname FROM pg_database WHERE datistemplate = false;";
      }
      preparedStatement = connection.prepareStatement(query);
      rs = preparedStatement.executeQuery();
      while (rs.next()) {
        databases.add(rs.getString(1));
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
      String[] types ={ "TABLE" };
  
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
    
    String catalog = databaseName;
    String schemaPattern = null;
    String tableNamePattern = tableName;
    String columnNamePattern =columnName;
    
    XmetaResult<Map<String, String>> response = new XmetaResult<Map<String, String>>();
    response.setStatus(200);
    response.setTitle("getColumnType[" + databaseName + "." + tableName + "." + columnName +"]");
    ResultSet rs = null;
   Map<String, String> columnType = new HashMap<String, String>();
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      rs = databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
  
      while (rs.next()) {
        if(rs.getString("COLUMN_NAME").equalsIgnoreCase(columnName)){
          columnType.put(rs.getString("COLUMN_NAME"), rs.getString("TYPE_NAME"));
        }
      }
      
      if(columnType.isEmpty()){
        columnType.put(columnName, "unkown column");
        response.setError("Unkown Column [" + columnName + "]");
      }
      rs.close();
    } catch (SQLException e){
      LOG.error("Error: {}", e);
      response.setError(String.format("SQLException: %s", e.getMessage()));
      if(rs != null){
        try {rs.close();} catch (SQLException e1) { /** ignore it **/ }
      }
    }
    response.setResult(columnType);
    return response;
  }

  @Override
  public XmetaResult getPrimaryKey(String databaseName, String tableName) {
    
    String catalog = databaseName;
    String schemaPattern = null;
    String tableNamePattern = tableName;
    
    XmetaResult<List<String>> response = new XmetaResult<List<String>>();
    response.setStatus(200);
    response.setTitle("ListTableColumnWithPrimarykey[" + databaseName + "." + tableName + "]");
    ResultSet rs = null;
    List<String> columnWithPrimaryKey = new ArrayList<String>();
    
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      rs = databaseMetaData.getPrimaryKeys(catalog, schemaPattern, tableNamePattern);//(catalog, schemaPattern, tableNamePattern, columnNamePattern);
  
      while (rs.next()) {
        columnWithPrimaryKey.add( rs.getString(4));
      }
      rs.close();
    } catch (SQLException e){
      LOG.error("Error: {}", e);
      response.setError(String.format("SQLException: %s", e.getMessage()));
      if(rs != null){
        try {rs.close();} catch (SQLException e1) { /** ignore it **/ }
      }
    }
    response.setResult(columnWithPrimaryKey);
    return response;

  }

  @Override
  public XmetaResult getForeignKey(String databaseName, String tableName) {
    
    String catalog = databaseName;
    String schemaPattern = null;
    String tableNamePattern = tableName;
    
    XmetaResult<List<String>> response = new XmetaResult<List<String>>();
    response.setStatus(200);
    response.setTitle("ListTableColumnWithPrimarykey[" + databaseName + "." + tableName + "]");
    ResultSet rs = null;
    List<String> columnWithForeignKey = new ArrayList<String>();
    
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      rs = databaseMetaData.getImportedKeys(catalog, schemaPattern, tableNamePattern);//(catalog, schemaPattern, tableNamePattern, columnNamePattern);
      
      while (rs.next()) {
        columnWithForeignKey.add( rs.getString("FKCOLUMN_NAME"));
      }
      rs.close();
    } catch (SQLException e){
      LOG.error("Error: {}", e);
      response.setError(String.format("SQLException: %s", e.getMessage()));
      if(rs != null){
        try {rs.close();} catch (SQLException e1) { /** ignore it **/ }
      }
    }
    response.setResult(columnWithForeignKey);
    return response;
  }

  @Override
  public XmetaResult getColumnLength(String databaseName, String tableName, String columnName) {
    
    String catalog = databaseName;
    String schemaPattern = null;
    String tableNamePattern = tableName;
    String columnNamePattern =columnName;
    
    XmetaResult<Map<String, Integer>> response = new XmetaResult<Map<String, Integer>>();
    response.setStatus(200);
    response.setTitle("ListTableColumnsWithType[" + databaseName + "." + tableName + "]");
    ResultSet rs = null;
    Map<String, Integer> columnLength = new HashMap<String, Integer>();
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      rs = databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
  
      while (rs.next()) {
        if(rs.getString("COLUMN_NAME").equalsIgnoreCase(columnName)){
          columnLength.put(rs.getString("COLUMN_NAME"), rs.getInt("COLUMN_SIZE"));
        }
      }
      rs.close();
      if(columnLength.isEmpty()){
        System.out.println("Unkown Column [" + columnName + "]");
        response.setError("Unkown Column [" + columnName + "]");
      }
    } catch (SQLException e){
      LOG.error("Error: {}", e);
      response.setError(String.format("SQLException: %s", e.getMessage()));
      if(rs != null){
        try {rs.close();} catch (SQLException e1) { /** ignore it **/ }
      }
    }
    response.setResult(columnLength);
    return response;
  }

  @Override
  public XmetaResult getColumnAtPosition(String databaseName, String tableName, int columnPosition) {
    String catalog = databaseName;
    String schemaPattern = null;
    String tableNamePattern = tableName;
    
    int i=0 ;
    XmetaResult<List<String>> response = new XmetaResult<List<String>>();
    response.setStatus(200);
    response.setTitle("ColumnAtPosition[" + databaseName + "." + tableName + "."  +  columnPosition + "]");
    ResultSet rs = null;
    List<String> columnatgivenposition = new ArrayList<String>();
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      rs = databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern,null);
      
      while (rs.next()) {
        if(i==columnPosition){
          columnatgivenposition.add(rs.getString(4));
        }
        i++;
      }
      rs.close();
    } catch (SQLException e){
      LOG.error("Error: {}", e);
      response.setError(String.format("SQLException: %s", e.getMessage()));
      if(rs != null){
        try {rs.close();} catch (SQLException e1) { /** ignore it **/ }
      }
    }
    response.setResult(columnatgivenposition);
    return response;
  }

  @Override
  public XmetaResult getForeignKeysReferencedTable(String databaseName, String tableName) {
    
    String catalog = databaseName;
    String schemaPattern = null;
    String tableNamePattern = tableName;
    
    XmetaResult<Map<String,String>> response = new XmetaResult<Map<String,String>>();
    response.setStatus(200);
    response.setTitle("ForeignKeyReferencedTable[" + databaseName + "." + tableName + "]");
    ResultSet rs = null;
    Map<String,String> ForeignKeysReferencedTable = new HashMap<String,String>();
    
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      rs = databaseMetaData.getImportedKeys(catalog, schemaPattern, tableNamePattern);//(catalog, schemaPattern, tableNamePattern, columnNamePattern);
      
      while (rs.next()) {
        ForeignKeysReferencedTable.put(rs.getString("FKCOLUMN_NAME"),rs.getString("PKTABLE_NAME"));
      }
      rs.close();
    } catch (SQLException e){
      LOG.error("Error: {}", e);
      response.setError(String.format("SQLException: %s", e.getMessage()));
      if(rs != null){
        try {rs.close();} catch (SQLException e1) { /** ignore it **/ }
      }
    }
    response.setResult(ForeignKeysReferencedTable);
    return response;
  }

  @Override
  public XmetaResult getIndexes(String databaseName, String tableName) {
    
    String catalog = databaseName;
    String schemaPattern = null;
    String tableNamePattern = tableName;
    
    XmetaResult<List<String>> response = new XmetaResult<List<String>>();
    response.setStatus(200);
    response.setTitle("ListIndexes[" + databaseName + "." + tableName + "]");
    ResultSet rs = null;
    List<String> TableIndexes = new ArrayList<String>();
    
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      rs = databaseMetaData.getIndexInfo(catalog, schemaPattern, tableNamePattern,true,true);//(catalog, schemaPattern, tableNamePattern, columnNamePattern);
      
      while (rs.next()) {
        TableIndexes.add( rs.getString("INDEX_NAME"));
      }
      rs.close();
    } catch (SQLException e){
      LOG.error("Error: {}", e);
      response.setError(String.format("SQLException: %s", e.getMessage()));
      if(rs != null){
        try {rs.close();} catch (SQLException e1) { /** ignore it **/ }
      }
    }
    response.setResult(TableIndexes);
    return response;
  }

  @Override
  public XmetaResult getAllIndexesAndReferencedTable(String databaseName, String tableName) {
    
    String catalog = databaseName;
    String schemaPattern = null;
    String tableNamePattern = tableName;
    
    XmetaResult<Map<String,String>> response = new XmetaResult<Map<String,String>>();
    response.setStatus(200);
    response.setTitle("ListIndexes[" + databaseName + "." + tableName + "]");
    ResultSet rs = null;
    Map<String,String> TableIndexesandreferencedtable = new HashMap<String,String>();
    
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      rs = databaseMetaData.getImportedKeys(catalog, schemaPattern, tableNamePattern);
      while (rs.next()) {
        TableIndexesandreferencedtable.put("Referenced Table", rs.getString("PKTABLE_NAME"));
        
      }
      rs.close();
      rs = databaseMetaData.getIndexInfo(catalog, schemaPattern, tableNamePattern,true,true);
      while (rs.next()) {
        TableIndexesandreferencedtable.put("Index",rs.getString("COLUMN_NAME"));
      }
      rs.close();
    } catch (SQLException e){
      LOG.error("Error: {}", e);
      response.setError(String.format("SQLException: %s", e.getMessage()));
      if(rs != null){
        try {rs.close();} catch (SQLException e1) { /** ignore it **/ }
      }
    }
    response.setResult(TableIndexesandreferencedtable);
    return response;
  }

  @Override
  public boolean databaseExists(String databaseName) {
    
    String dbToSearch = databaseName;
    ResultSet rs = null;
    boolean found = false;
    try{
    rs = connection.getMetaData().getCatalogs();
    while (rs.next()) {
      // Get the database name, which is at position 1
      String dbName = rs.getString(1);
      if (dbName.equals(dbToSearch)) {
        found = true;
        break;
      }
    }
    rs.close();
   }
    catch(Exception e){
      e.printStackTrace();
    }
    return found;
  }

  @Override
  public boolean tableExists(String databaseName, String tableName) {
    
    String catalog = databaseName;
    String tableNamePattern = tableName;
    String schemaPattern = null;
    ResultSet rs = null;
    boolean found = false;
    try{
      DatabaseMetaData meta = connection.getMetaData();
      rs = meta.getTables(catalog, schemaPattern, tableNamePattern, new String[] { "TABLE" });
    while (rs.next()) {
      String tablename = rs.getString(3);
      if (tableNamePattern.equals(tablename)) {
        found = true;
        break;
      }
    }
    rs.close();
   }
    catch(Exception e){
      e.printStackTrace();
    }
    return found;
  }

  @Override
  public boolean columnExists(String databaseName, String tableName, String columnName) {
    
    String catalog = databaseName;
    String tableNamePattern = tableName;
    String schemaPattern = null;
    String columnNamePattern = columnName;
    ResultSet rs = null;
    boolean found = false;
    try{
      DatabaseMetaData meta = connection.getMetaData();
      rs = meta.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
    while (rs.next()) {
      // Get the database name, which is at position 1
      String columnname = rs.getString(4);
      if (columnNamePattern.equals(columnname)) {
        found = true;
        break;
      }
    }
    rs.close();
   }
    catch(Exception e){
      e.printStackTrace();
    }
    return found;
  }

  @Override
  public boolean supportsGroupBy() {
    
    boolean supportsgroupBy = false;
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      supportsgroupBy = databaseMetaData.supportsGroupBy(); 
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return supportsgroupBy;
  }

  @Override
  public boolean supportsOuterJoins() {
    boolean supportsOuterJoins = false;
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      supportsOuterJoins = databaseMetaData.supportsOuterJoins(); 
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return supportsOuterJoins;
  }
  
  @Override
  public boolean supportsUnion() {
    boolean supportsUnion = false;
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      supportsUnion = databaseMetaData.supportsUnion(); 
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return supportsUnion;
  }

  @Override
  public boolean supportsUnionAll() {
    boolean supportsUnionAll = false;
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      supportsUnionAll = databaseMetaData.supportsUnionAll(); 
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return supportsUnionAll;
  }
  @Override
  public XmetaResult getDatabaseInfo() {
    XmetaResult<Map<String,String>> response = new XmetaResult<Map<String,String>>();
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData(); 
      String Url = databaseMetaData.getURL();
      String userName = databaseMetaData.getUserName();
      String databaseName = databaseMetaData.getDatabaseProductName();
      String databaseVersion = databaseMetaData.getDatabaseProductVersion();
      
      response.setStatus(200);
      response.setTitle("DataBaseInfo");
      ResultSet rs = null;
      Map<String,String> databaseInfo = new HashMap<String,String>();
      databaseInfo.put("URL", Url );
      databaseInfo.put("UserName", userName);
      databaseInfo.put("DataBaseName", databaseName);
      databaseInfo.put("DataBaseVersion", databaseVersion);
      
      response.setResult(databaseInfo);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return response;
  }

  @Override
  public XmetaResult getDatabaseDriverInfo() {
    XmetaResult<Map<String,String>> response = new XmetaResult<Map<String,String>>();
    try {
      DatabaseMetaData databaseMetaData = connection.getMetaData(); 
      String driverName = databaseMetaData.getDriverName();
      String driverVersion = databaseMetaData.getDriverVersion();
      String supportSQLKeywords = databaseMetaData.getSQLKeywords();
      
      response.setStatus(200);
      response.setTitle("DataBaseDriverInfo");
      ResultSet rs = null;
      Map<String,String> databaseInfo = new HashMap<String,String>();
      databaseInfo.put("DriverName", driverName );
      databaseInfo.put("DriverVersion", driverVersion);
      databaseInfo.put("SupportSQLKeywords", supportSQLKeywords);
      response.setResult(databaseInfo);
   
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return response;
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
