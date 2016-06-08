package org.projectspinoza.dd.xmetadata;


public interface XmetaApi {
  
  /*** LIST ***/ 
  public XmetaResult listDatabases();
  public XmetaResult listTables(String databaseName);
  public XmetaResult listColumns(String databaseName, String tableName);
  public XmetaResult listColumnsWithType(String databaseName, String tableName);
  
  /*** GET ***/
  public XmetaResult getColumnType(String databaseName, String tableName, String columnName);
  public XmetaResult getPrimaryKey(String databaseName, String tableName);
  public XmetaResult getForeignKey(String databaseName, String tableName);
  public XmetaResult getColumnLength(String databaseName, String tableName, String columnName);
  public XmetaResult getColumnAtPosition(String databaseName, int columnPosition);
  public XmetaResult getForeignKeysReferencedTable(String databaseName, String tableName);
  public XmetaResult getIndexes(String databaseName, String tableName);
  public XmetaResult getAllIndexesAndReferencedTable(String databaseName, String tableName);
  
  /*** EXISTS ***/
  public boolean databaseExists(String databaseName);
  public boolean tableExists(String databaseName, String tableName);
  public boolean columnExists(String databaseName, String tableName, String columnName);
  
  /*** SUPPORTS ***/
  public boolean supportsGroupBy();
  public boolean supportsOuterJoins();
  public boolean supportsUnion();
  public boolean supportsUnionAll();
  
  
  /*** BASIC DATABASE INFO ***/
  public XmetaResult getDatabaseInfo();
  
  /*** BASIC DATABASE DRIVER INFO ***/
  public XmetaResult getDatabaseDriverInfo();
  
  /*** temp will be removed ***/
  public void closeConnection();
  
}
