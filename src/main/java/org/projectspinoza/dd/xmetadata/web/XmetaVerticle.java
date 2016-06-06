package org.projectspinoza.dd.xmetadata.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.projectspinoza.dd.xmetadata.RoutingHandler;
import org.projectspinoza.dd.xmetadata.XmetaApi;
import org.projectspinoza.dd.xmetadata.XmetaResult;
import org.projectspinoza.dd.xmetadata.core.MysqlApi;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;


public class XmetaVerticle extends AbstractVerticle implements RoutingHandler{
  private static Logger LOG = LogManager.getRootLogger();
  private Router router;
  
  /*** to be removed ***/
  private XmetaApi xmeta;
  private Properties settings;
  
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
    init();
    List<String> availableRoutes = new ArrayList<String>();
    
    List<Route> allRoutes = router.getRoutes();
    for(Route r : allRoutes){
      if(r.getPath() != null && !(r.getPath().isEmpty())){
        availableRoutes.add(r.getPath());
      }
    }
    System.out.println("allRoutes: " + availableRoutes.size());
    XmetaResult<List<String>> r = new XmetaResult<List<String>>();
    r.setStatus(200);
    r.setTitle("ListRoutes");
    r.setResult(availableRoutes);
    Buffer responseData = toBuffer(r);
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    //. close connection
    xmeta.closeConnection();
    
    response.end(responseData);
  }

  @Override
  public void listDatabases(RoutingContext routingContext) {
    init();
    Buffer responseData = toBuffer(xmeta.listDatabases());
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    //. close connection
    xmeta.closeConnection();
    
    response.end(responseData);
  }

  @Override
  public void listTables(RoutingContext routingContext) {
    init();
    
    Buffer responseData = toBuffer(xmeta.listTables(routingContext.request().getParam("database")));
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    
    //. close connection
    xmeta.closeConnection();
    
    response.end(responseData);
  }

  @Override
  public void listColumns(RoutingContext routingContext) {
    init();
    
    Buffer responseData = toBuffer(xmeta.listColumns(routingContext.request().getParam("database"), routingContext.request().getParam("table")));
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    //. close connection
    xmeta.closeConnection();
    
    response.end(responseData);
    
  }

  @Override
  public void listColumnsWithType(RoutingContext routingContext) {
    init();
    
    Buffer responseData = toBuffer(xmeta.listColumnsWithType(routingContext.request().getParam("database"), routingContext.request().getParam("table")));
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    //. close connection
    xmeta.closeConnection();
    
    response.end(responseData);
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
  
  private void connect(){
    try {
      LOG.debug("connecting to database: {}", settings);
      String db_url = getDbUrl();
      LOG.debug("db_url: {}", db_url);
      Class.forName(settings.getProperty("jdbc_driver")).newInstance();
      Connection connection = DriverManager.getConnection(db_url, settings.getProperty("db_user"), settings.getProperty("db_pass"));
      LOG.debug("connection succeeded");
      xmeta = new MysqlApi(connection);
    } catch (Exception e){
      e.printStackTrace();
      LOG.error("cannot connecto to database");
    }
  }
  
  private String getDbUrl() {
    return "jdbc:mysql://" + settings.getProperty("db_host") + ":"
        + settings.get("db_port") + "/" + settings.getProperty("db_name")
        + "?autoReconnect=true&useSSL=false";
  }
  
  public void init(){
    settings = new Properties();
    settings.put("jdbc_driver", "com.mysql.jdbc.Driver");
    settings.put("db_host", "localhost");
    settings.put("db_port", 3306);
    settings.put("db_name", "fynder_website");
    settings.put("db_user", "root");
    settings.put("db_pass", "1234");
    
    connect();
  }
  
  private Buffer toBuffer(XmetaResult result){
    Buffer responseData = null;
    try {
      String jsonString = new ObjectMapper().writeValueAsString(result);
      responseData = Buffer.buffer(jsonString.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return responseData;
  }
}
