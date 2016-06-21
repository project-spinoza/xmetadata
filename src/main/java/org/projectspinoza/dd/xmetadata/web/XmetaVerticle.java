package org.projectspinoza.dd.xmetadata.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.projectspinoza.dd.xmetadata.RoutingHandler;
import org.projectspinoza.dd.xmetadata.XmetaApi;
import org.projectspinoza.dd.xmetadata.XmetaResult;
import org.projectspinoza.dd.xmetadata.core.PostgreMysqlApi;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;


public class XmetaVerticle extends AbstractVerticle implements RoutingHandler{
  private static Logger LOG = LogManager.getRootLogger();
  private Router router;
  private JWTAuth authProvider;
  
  @Override
  public void start(Future<Void> startFuture) throws Exception {
    super.start(startFuture);
    router = Router.router(vertx);
    
    router.route().handler(BodyHandler.create());
    authProvider = JWTAuth.create(vertx, new JsonObject().put("keyStore", new JsonObject()
        .put("type", "jceks")
        .put("path", "keystore.jceks")
        .put("password", "secret")));
    
    router.route("/xmeta/*").handler(JWTAuthHandler.create(authProvider, "/auth"));
    
    router.post("/auth").consumes("application/json").handler(this::auth);
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
    
    router.get("/xmeta/api/v1/get/supports/:feature").handler(this::supportsFeature);
    
    router.get("/xmeta/api/v1/info/db/").handler(this::getDatabaseInfo);
    router.get("/xmeta/api/v1/info/dbdriver/").handler(this::getDatabaseDriverInfo);
    
    
    vertx.createHttpServer().requestHandler(router::accept).listen(8181);
  }

  @Override
  public void stop(Future<Void> stopFuture) throws Exception {
    super.stop(stopFuture);
  }

  @Override
  public void info(RoutingContext routingContext) {
    
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
    
    response.end(responseData);
  }

  @Override
  public void listDatabases(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    Buffer responseData = toBuffer(api.listDatabases());
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    api.closeConnection();
    response.end(responseData);
  }

  @Override
  public void listTables(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    Buffer responseData = toBuffer(api.listTables(routingContext.request().getParam("database")));
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
   
    api.closeConnection();
    response.end(responseData);
  }

  @Override
  public void listColumns(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    Buffer responseData = toBuffer(api.listColumns(routingContext.request().getParam("database"), routingContext.request().getParam("table")));
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    api.closeConnection();
    response.end(responseData);
    
  }

  @Override
  public void listColumnsWithType(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    Buffer responseData = toBuffer(api.listColumnsWithType(routingContext.request().getParam("database"), routingContext.request().getParam("table")));
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    api.closeConnection();
    response.end(responseData);
  }

  @Override
  public void getColumnType(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    Buffer responseData = toBuffer(api.getColumnType(routingContext.request().getParam("database"), routingContext.request().getParam("table"),routingContext.request().getParam("column")));
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    api.closeConnection();
    response.end(responseData);
  }

  @Override
  public void getPrimaryKey(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    Buffer responseData = toBuffer(api.getPrimaryKey(routingContext.request().getParam("database"), routingContext.request().getParam("table")));
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    api.closeConnection();
    response.end(responseData);
  }

  @Override
  public void getForeignKey(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    Buffer responseData = toBuffer(api.getForeignKey(routingContext.request().getParam("database"), routingContext.request().getParam("table")));
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");

    api.closeConnection();
    response.end(responseData);
  }

  @Override
  public void getColumnLength(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    Buffer responseData = toBuffer(api.getColumnLength(routingContext.request().getParam("database"), routingContext.request().getParam("table"),routingContext.request().getParam("column")));
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    api.closeConnection();
    response.end(responseData);
    
  }

  @Override
  public void getColumnAtPosition(RoutingContext routingContext) {
    // TODO Auto-generated method stub
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    Buffer responseData = toBuffer(api.getColumnAtPosition(routingContext.request().getParam("database"), routingContext.request().getParam("table"), Integer.parseInt(routingContext.request().getParam("columnIndex"))));
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    api.closeConnection();
    response.end(responseData);
  }

  @Override
  public void getForeignKeysReferencedTable(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    Buffer responseData = toBuffer(api.getForeignKeysReferencedTable(routingContext.request().getParam("database"), routingContext.request().getParam("table")));
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    api.closeConnection();
    response.end(responseData);
  }

  @Override
  public void getIndexes(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    Buffer responseData = toBuffer(api.getIndexes(routingContext.request().getParam("database"), routingContext.request().getParam("table")));
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    api.closeConnection();
    response.end(responseData);
  }

  @Override
  public void getAllIndexesAndReferencedTable(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    Buffer responseData = toBuffer(api.getAllIndexesAndReferencedTable(routingContext.request().getParam("database"), routingContext.request().getParam("table")));
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");

    api.closeConnection();
    response.end(responseData);
  }

  @Override
  public void databaseExists(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    String databaseName =routingContext.request().getParam("db").toString();
    XmetaResult<Map<String,String>> r = new XmetaResult<Map<String,String>>();
    Map<String,String> responsedata = new HashMap<String, String>();
    boolean exists = api.databaseExists(databaseName);
    r.setStatus(200);
    r.setTitle("DataBaseExists");
    responsedata.put(databaseName, String.valueOf(exists));
    r.setResult(responsedata);
    Buffer responseData = toBuffer(r);
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    api.closeConnection();
    response.end(responseData);
  }

  @Override
  public void tableExists(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    String tableName =routingContext.request().getParam("table").toString();
    String databaseName = "testDb";
    XmetaResult<Map<String,String>> r = new XmetaResult<Map<String,String>>();
    Map<String,String> responsedata = new HashMap<String, String>();
    boolean exists = api.tableExists(databaseName,tableName);
    r.setStatus(200);
    r.setTitle("TableExists");
    responsedata.put(tableName, String.valueOf(exists));
    r.setResult(responsedata);
    Buffer responseData = toBuffer(r);
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");

    api.closeConnection();
    response.end(responseData);
  }

  @Override
  public void columnExists(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    String columnName =routingContext.request().getParam("column").toString();
    String databaseName = "testDb";
    String tableName = "company";
    XmetaResult<Map<String,String>> r = new XmetaResult<Map<String,String>>();
    Map<String,String> responsedata = new HashMap<String, String>();
    boolean exists = api.columnExists(databaseName,tableName,columnName);
    r.setStatus(200);
    r.setTitle("ColumnExists");
    responsedata.put(columnName, String.valueOf(exists));
    r.setResult(responsedata);
    Buffer responseData = toBuffer(r);
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");

    api.closeConnection();
    response.end(responseData);
  }

  @Override
  public void supportsFeature(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    XmetaResult<Map<String,String>> r = new XmetaResult<Map<String,String>>();
    Map<String,String> responsedata = new HashMap<String, String>();
    
    String featureLabel = routingContext.request().getParam("feature");
    Feature feature = Feature.none;
    try {
      feature = Feature.valueOf(featureLabel);
    } catch (IllegalArgumentException | NullPointerException ex) {
      ex.printStackTrace();
    } 
    
    boolean featureSupported = false;
    switch (feature) {
      case group_by:
        featureSupported = api.supportsGroupBy();
        break;
      case outer_join:
        featureSupported = api.supportsOuterJoins();
        break;
      case union:
        featureSupported = api.supportsUnion();
        break;
      case union_all:
        featureSupported = api.supportsUnionAll();
        break;
      case none:
        break;
    }
    if(feature.equals(Feature.none)){
      r.setError("Invalid Feature -" + featureLabel);
      r.setStatus(200);
    }else{
      r.setStatus(200);
      r.setTitle("Supports " + featureLabel);
      responsedata.put(featureLabel, String.valueOf(featureSupported));
      r.setResult(responsedata);  
    }
    
    Buffer responseData = toBuffer(r);
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    api.closeConnection();
    response.end(responseData);
  }
  
  @Override
  public void getDatabaseInfo(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    Buffer responseData = toBuffer(api.getDatabaseInfo());
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");
    
    api.closeConnection();
    response.end(responseData);
  }

  @Override
  public void getDatabaseDriverInfo(RoutingContext routingContext) {
    XmetaApi api = getApiHandler(routingContext.user().principal());
    
    Buffer responseData = toBuffer(api.getDatabaseDriverInfo());
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers()
    .add("Content-Length", responseData.length()+"")
    .add("Content-Type", "application/json");

    api.closeConnection();
    response.end(responseData);
  }
  
  private void auth(RoutingContext routingContext){
    JsonObject postJson = routingContext.getBodyAsJson();
    JsonObject user = new JsonObject()
        .put("db_host", postJson.getString("db_host"))
        .put("db_port", postJson.getInteger("db_port"))
        .put("db_user", postJson.getString("db_user"))
        .put("db_pass", postJson.getString("db_pass"))
        .put("db_type", postJson.getString("db_type"));
    Long tokenExpireMins = 60L;
    if(postJson.containsKey("token_expires_min")){
      tokenExpireMins = postJson.getInteger("token_expires_min").longValue();
    }
    
    if(postJson.containsKey("db_name")){
      user.put("db_name", postJson.getString("db_name"));
    }
    XmetaApi xmeta = null;
    if((xmeta = getApiHandler(user)) == null){
      routingContext.fail(401);
    }else{
      xmeta.closeConnection();
      
      Map<String,String> responsedata = new HashMap<String, String>();
      responsedata.put("token", authProvider.generateToken(user, new JWTOptions().setExpiresInMinutes(tokenExpireMins)));
      XmetaResult<Map<String, String>> r = new XmetaResult<Map<String, String>>();
      r.setStatus(200);
      r.setTitle("Authentication[SUCCESS]");
      r.setResult(responsedata);
      
      Buffer responseData = toBuffer(r);
      HttpServerResponse response = routingContext.response();
      response.setStatusCode(200);
      response.headers()
      .add("Content-Length", responseData.length()+"")
      .add("Content-Type", "application/json");
      response.end(responseData);;
    } 
  }
  
  /**
   * creates and returns conneciton on the basis of the following parameters.
   * 
   * @param info
   * @param driver
   * @param dbtype
   * @param dbname
   * @return Connection Object
   * @throws ClassNotFoundException 
   * @throws IllegalAccessException 
   * @throws InstantiationException 
   * @throws SQLException 
   */
  private Connection connect(JsonObject settings, String driver, String dbtype, String dbname) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
    LOG.info("settings {}", settings);
    Class.forName(driver).newInstance();
    String dbURL = buildDbUrl(dbtype, settings.getString("db_host"), settings.getInteger("db_port"), dbname);
    return DriverManager.getConnection(dbURL, settings.getString("db_user"), settings.getString("db_pass"));
  }
  
  private String buildDbUrl(String dbms, String host, int port, String dbName){
    return "jdbc:" + dbms + "://" + host + ":" + port + "/" + dbName + "?autoReconnect=true&useSSL=false";
  }
  
  private XmetaApi getApiHandler(JsonObject credentials){
    XmetaApi xmeta = null;
    String dbname = credentials.containsKey("db_name") ? credentials.getString("db_name") : null;
    try{
      DBMS dbms = DBMS.valueOf(credentials.getString("db_type"));
      switch(dbms){
      case mysql:
        xmeta = new PostgreMysqlApi(connect(credentials, dbms.getDriver(), dbms.name(), dbname == null ? "mysql" : dbname));
        break;
      case postgresql:
        xmeta = new PostgreMysqlApi(connect(credentials, dbms.getDriver(), dbms.name(), dbname == null ? "postgres" : dbname));
        break;
      default:
        break;
      }
    }catch(InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e){
      LOG.error("failed connecting to database[{}]", dbname);
      e.printStackTrace();
    }
    return xmeta;
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

enum Feature{
  outer_join, group_by, union, union_all, none;
}

enum DBMS{
  mysql("com.mysql.jdbc.Driver"), postgresql("org.postgresql.Driver");
  
  private final String driver;
  
  DBMS(String driver){
    this.driver = driver;
  }
  
  public String getDriver(){
    return driver;
  }
}