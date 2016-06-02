package org.projectspinoza.dd.xmetadata;

public interface RestApi {
  
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
  public boolean supportsGroupBy(String databaseName, String tableName);
  public boolean supportsOuterJoins(String databaseName, String tableName);
  public boolean supportsInnerJoins(String databaseName, String tableName);
  
  /*** BASIC DATABASE INFO ***/
  public XmetaResult getDatabaseInfo();
  
  /*** BASIC DATABASE DRIVER INFO ***/
  public XmetaResult getDatabaseDriverInfo();
  
}
