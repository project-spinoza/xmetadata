package org.projectspinoza.dd.xmetadata.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import org.projectspinoza.dd.xmetadata.RoutingHandler;

import com.fasterxml.jackson.databind.ObjectMapper;


public class XmetaVerticle extends AbstractVerticle implements RoutingHandler{
  
  private Router router;

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    super.start(startFuture);
    router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
    
    router.get("/xmeta/api/v1/").handler(this::info);
    router.get("/xmeta/api/v1/list/db").handler(this::listDatabases);
    router.get("/xmeta/api/v1/list/table/:database").handler(this::listTables);
    router.get("/xmeta/api/v1/list/column/:database/:table").handler(this::listColumns);
    router.get("/xmeta/api/v1/list/columnWithType/:database/:table").handler(this::listColumnsWithType);
    
    router.get("/xmeta/api/v1/get/columnType/:database/:table/:column").handler(this::getColumnType);
    router.get("/xmeta/api/v1/get/columnLength/:database/:table/:column").handler(this::getColumnLength);
    router.get("/xmeta/api/v1/get/columnAtPos/:database/:table/:columnIndex").handler(this::getColumnAtPosition);
    router.get("/xmeta/api/v1/get/primaryKey/:database/:table").handler(this::getPrimaryKey);
    router.get("/xmeta/api/v1/get/foreignKey/:database/:table").handler(this::getForeignKey);
    router.get("/xmeta/api/v1/get/foreignKeysRefTable/:database/:table").handler(this::getForeignKeysReferencedTable);
    router.get("/xmeta/api/v1/get/indexes/:database/:table").handler(this::getIndexes);
    router.get("/xmeta/api/v1/get/allIdxRefTable/:database/:table").handler(this::getAllIndexesAndReferencedTable);
    
    router.get("/xmeta/api/v1/exists/db/:db").handler(this::databaseExists);
    router.get("/xmeta/api/v1/exists/table/:table").handler(this::tableExists);
    router.get("/xmeta/api/v1/exists/column/:column").handler(this::columnExists);
    
    router.get("/xmeta/api/v1/get/supports/:feature/:database/:table").handler(this::supportsFeature);
    
    router.get("/xmeta/api/v1/info/db/").handler(this::getDatabaseInfo);
    router.get("/xmeta/api/v1/info/dbdriver/").handler(this::getDatabaseDriverInfo);
    
    
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }

  @Override
  public void stop(Future<Void> stopFuture) throws Exception {
    super.stop(stopFuture);
  }

  @Override
  public void info(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    Map<String, String> availableRoutes = new HashMap<String, String>();
    
    List<Route> allRoutes = router.getRoutes();
    for(Route r : allRoutes){
      if(r.getPath() != null && !(r.getPath().isEmpty())){
        availableRoutes.put(r.getPath(), r.toString());
      }
    }
    System.out.println("allRoutes: " + availableRoutes.size());
    Buffer responseData = null;
    try {
      String result = new ObjectMapper().writeValueAsString(availableRoutes);
      responseData = Buffer.buffer(result.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    response.end(responseData);
  }

  @Override
  public void listDatabases(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void listTables(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void listColumns(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void listColumnsWithType(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void getColumnType(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void getPrimaryKey(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void getForeignKey(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void getColumnLength(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void getColumnAtPosition(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void getForeignKeysReferencedTable(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void getIndexes(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void getAllIndexesAndReferencedTable(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void databaseExists(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void tableExists(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void columnExists(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void supportsFeature(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void getDatabaseInfo(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void getDatabaseDriverInfo(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    
  }

}
