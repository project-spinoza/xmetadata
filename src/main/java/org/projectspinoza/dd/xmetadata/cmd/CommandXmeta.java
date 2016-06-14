package org.projectspinoza.dd.xmetadata.cmd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.projectspinoza.dd.xmetadata.XmetaApi;
import org.projectspinoza.dd.xmetadata.XmetaResult;
import org.projectspinoza.dd.xmetadata.core.PostgreMysqlApi;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "xmeta", commandDescription = "root command name")
public class CommandXmeta extends BasicCommand{
  @Override
  public Object exec(XmetaApi api){
    return null;
  }
}

@Parameters(commandNames = "connect", commandDescription = "database connector")
class CommandConnect extends BasicCommand<Object>{
  
  @Parameter(names = "-host", description = "database host", required = true)
  private String host;
  @Parameter(names = "-port", description = "database port", required = true)
  private int port;
  @Parameter(names = "-user", description = "database user", required = true)
  private String user;
  @Parameter(names = "-pass", description = "database pass", required = true)
  private String pass;
  @Parameter(names = "-type", description = "database type e.g. mysql, postgre etc.", required = true)
  private String type;
  @Parameter(names = "-db", description = "database name", required = false)
  private String db;
  
  @Override
  public Object exec(XmetaApi api){
    return null;
  }
  public XmetaApi getApiHandler() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
    XmetaApi xmeta = null;
    DBMS dbms = DBMS.valueOf(type);
    switch(dbms){
    case mysql:
      xmeta = new PostgreMysqlApi(connect(dbms.getDriver(), dbms.name(), db == null ? "mysql" : db));
      break;
    case postgres:
      xmeta = new PostgreMysqlApi(connect(dbms.getDriver(), dbms.name(), db == null ? "postgres" : db));
      break;
    default:
      break;
    }
    return xmeta;
  }
  
  /**
   * creates and returns conneciton on the basis of the following parameters.
   * 
   * @param driver
   * @param dbtype
   * @param dbname
   * @return Connection Object
   * @throws ClassNotFoundException 
   * @throws IllegalAccessException 
   * @throws InstantiationException 
   * @throws SQLException 
   */
  private Connection connect(String driver, String dbtype, String dbname) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
    Class.forName(driver).newInstance();
    String dbURL = buildDbUrl(dbtype, host, port, dbname);
    System.err.println("Returning connection object");
    return DriverManager.getConnection(dbURL, user, pass);
  }
  private String buildDbUrl(String dbms, String host, int port, String dbName){
    return "jdbc:" + dbms + "://" + host + ":" + port + "/" + dbName + "?autoReconnect=true&useSSL=false";
  }
  
  @Override
  public String toString() {
    return "XmetaCommand [host=" + host + ", port=" + port + ", user=" + user + ", pass=" + pass + ", type=" + type
        + ", db=" + db + "]";
  }
}

@Parameters(commandNames = "showDatabases", commandDescription = "shows available databases")
class CommandShowDatabases extends BasicCommand<List<String>> {

  @Override
  public List<String> exec(XmetaApi api) {
    XmetaResult r = api.listDatabases();
    return (List<String>) r.getResult();
  }
}

@Parameters(commandNames = "showTables", commandDescription = "shows available tables")
class CommandShowTables extends BasicCommand<List<String>> {
  @Parameter(names = "-db", description = "database name", required = true)
  private String db;
  
  @Override
  public List<String> exec(XmetaApi api) {
    XmetaResult r = api.listTables(db);
    return (List<String>) r.getResult();
  }
}

@Parameters(commandNames = "showColumns", commandDescription = "shows columns")
class CommandShowColumns extends BasicCommand<List<String>> {
  @Parameter(names = "-db", description = "database name", required = true)
  private String db;
  
  @Parameter(names = "-tbl", description = "table name", required = true)
  private String tbl;
  
  @Override
  public List<String> exec(XmetaApi api) {
    XmetaResult r = api.listColumns(db, tbl);
    return (List<String>) r.getResult();
  }
}

@Parameters(commandNames = "showColsWithType", commandDescription = "shows columns with type")
class CommandShowColsWithType extends BasicCommand<Map<String, String>> {
  @Parameter(names = "-db", description = "database name", required = true)
  private String db;
  
  @Parameter(names = "-tbl", description = "table name", required = true)
  private String tbl;
  
  @Override
  public Map<String, String> exec(XmetaApi api) {
    XmetaResult r = api.listColumnsWithType(db, tbl);
    return (Map<String, String>) r.getResult();
  }
}

@Parameters(commandNames = "getColLength", commandDescription = "get column length")
class CommandGetColLength extends BasicCommand<Map<String, Integer>> {
  @Parameter(names = "-db", description = "database name", required = true)
  private String db;
  
  @Parameter(names = "-tbl", description = "table name", required = true)
  private String tbl;
  
  @Parameter(names = "-col", description = "column name", required = true)
  private String col;
  
  @Override
  public Map<String, Integer> exec(XmetaApi api) {
    XmetaResult r = api.getColumnLength(db, tbl, col);
    return (Map<String, Integer>) r.getResult();
  }
}

@Parameters(commandNames = "getColType", commandDescription = "get column type")
class CommandGetColType extends BasicCommand<Map<String, String>> {
  @Parameter(names = "-db", description = "database name", required = true)
  private String db;
  
  @Parameter(names = "-tbl", description = "table name", required = true)
  private String tbl;
  
  @Parameter(names = "-col", description = "column name", required = true)
  private String col;
  
  @Override
  public Map<String, String> exec(XmetaApi api) {
    XmetaResult r = api.getColumnType(db, tbl, col);
    return (Map<String, String>) r.getResult();
  }
}

@Parameters(commandNames = "existsDatabase", commandDescription = "database exists: true|false")
class CommandExistsDatabase extends BasicCommand<Boolean> {
  @Parameter(names = "-db", description = "database name", required = true)
  private String db;
  
  @Override
  public Boolean exec(XmetaApi api) {
    return api.databaseExists(db);
  }
}

@Parameters(commandNames = "existsTable", commandDescription = "table exists: true|false")
class CommandExistsTable extends BasicCommand<Boolean> {
  @Parameter(names = "-db", description = "database name", required = true)
  private String db;
  
  @Parameter(names = "-tbl", description = "table name", required = true)
  private String tbl;
  
  @Override
  public Boolean exec(XmetaApi api) {
    return api.tableExists(db, tbl);
  }
}

@Parameters(commandNames = "existsColumn", commandDescription = "column exists: true|false")
class CommandExistsColumn extends BasicCommand<Boolean> {
  @Parameter(names = "-db", description = "database name", required = true)
  private String db;
  
  @Parameter(names = "-tbl", description = "table name", required = true)
  private String tbl;
  
  @Parameter(names = "-col", description = "column name", required = true)
  private String col;
  
  @Override
  public Boolean exec(XmetaApi api) {
    return api.columnExists(db, tbl, col);
  }
}

enum DBMS{
  mysql("com.mysql.jdbc.Driver"), postgres("org.postgresql.Driver");
  
  private final String driver;
  
  DBMS(String driver){
    this.driver = driver;
  }
  
  public String getDriver(){
    return driver;
  }
}
