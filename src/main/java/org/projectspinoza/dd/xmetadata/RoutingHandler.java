package org.projectspinoza.dd.xmetadata;

import io.vertx.ext.web.RoutingContext;

public interface RoutingHandler {
  
  public void info(RoutingContext routingContext);
  
  /*** LIST ***/ 
  public void listDatabases(RoutingContext routingContext);
  public void listTables(RoutingContext routingContext);
  public void listColumns(RoutingContext routingContext);
  public void listColumnsWithType(RoutingContext routingContext);
  
  /*** GET ***/
  public void getColumnType(RoutingContext routingContext);
  public void getPrimaryKey(RoutingContext routingContext);
  public void getForeignKey(RoutingContext routingContext);
  public void getColumnLength(RoutingContext routingContext);
  public void getColumnAtPosition(RoutingContext routingContext);
  public void getForeignKeysReferencedTable(RoutingContext routingContext);
  public void getIndexes(RoutingContext routingContext);
  public void getAllIndexesAndReferencedTable(RoutingContext routingContext);
  
  /*** EXISTS ***/
  public void databaseExists(RoutingContext routingContext);
  public void tableExists(RoutingContext routingContext);
  public void columnExists(RoutingContext routingContext);
  
  /*** SUPPORTS ***/
  public void supportsFeature(RoutingContext routingContext);
 
  /*** BASIC DATABASE INFO ***/
  public void getDatabaseInfo(RoutingContext routingContext);
  
  /*** BASIC DATABASE DRIVER INFO ***/
  public void getDatabaseDriverInfo(RoutingContext routingContext);
  
}
