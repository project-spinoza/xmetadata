package org.projectspinoza.dd.xmetadata.cmd;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.projectspinoza.dd.xmetadata.XmetaApi;

import com.beust.jcommander.JCommander;

public class CommandLineDriver {
  private static XmetaApi apiHandler = null;
  
  public static void main(String[] args) {
    CommandLineDriver clitest = new CommandLineDriver();
    Scanner scanner = new Scanner(new InputStreamReader(System.in));
    System.out.println("WELCOME TO XMETA[MD EXTRACTION]");
    while(true){
      System.out.print("xmeta>");
      String cmd = scanner.nextLine();
      if(clitest.validated(cmd)){
        if(cmd.equalsIgnoreCase("exit")){
          System.exit(0);
        }
        clitest.exec(cmd.split(" "));
      }else{
        System.out.println("for help type: xmeta --help OR xmeta <commandName> --help");
      }
    }
  }
  
  public void exec(String[] args){
    JCommander rootCommander = new JCommander();
    registerCommands(rootCommander);
    
    rootCommander.parse(args);
    CommandXmeta xmetaCommand = (CommandXmeta) rootCommander.getCommands().get("xmeta").getObjects().get(0);
    if (xmetaCommand.needHelp()) {
      rootCommander.usage();
      return;
    }
    
    String parsedCommand = rootCommander.getCommands().get("xmeta").getParsedCommand();
    BasicCommand basicCmd = (BasicCommand) rootCommander.getCommands().get("xmeta").getCommands().get(parsedCommand).getObjects().get(0);
    if (basicCmd.needHelp()) {
      JCommander xmetaCmd = rootCommander.getCommands().get("xmeta");  
      xmetaCmd.usage(xmetaCmd.getParsedCommand());
      return;
    }
    try {
      exec(rootCommander, parsedCommand);
    } catch (Exception e){
      e.printStackTrace();
    }
    
  }
  
  private void exec(JCommander rootCommander, String parsedCommand) throws Exception{
    switch(AvailCommand.valueOf(parsedCommand)){
    case connect:
      CommandConnect cmdConn = (CommandConnect) rootCommander.getCommands().get("xmeta").getCommands().get(parsedCommand).getObjects().get(0);
      System.out.println(cmdConn.toString());
      try{
        apiHandler = cmdConn.getApiHandler();
        System.err.println("Connected!");
      }catch(Exception e){
        e.printStackTrace();
      }
      break;
    case showDatabases:
      CommandShowDatabases cmdlistdb = (CommandShowDatabases) rootCommander.getCommands().get("xmeta").getCommands().get(parsedCommand).getObjects().get(0);
      List<String> dbs = cmdlistdb.exec(apiHandler);
      for(String db : dbs){
        System.out.print(db + ", ");
      }
      System.out.println();
      break;
    case showTables:
      CommandShowTables cmdlisttbl = (CommandShowTables) rootCommander.getCommands().get("xmeta").getCommands().get(parsedCommand).getObjects().get(0);
      List<String> tbls = cmdlisttbl.exec(apiHandler);
      for(String tbl : tbls){
        System.out.print(tbl + ", ");
      }
      System.out.println();
      break;
    case showColumns:
      CommandShowColumns cmdlistCol = (CommandShowColumns) rootCommander.getCommands().get("xmeta").getCommands().get(parsedCommand).getObjects().get(0);
      List<String> cols = cmdlistCol.exec(apiHandler);
      for(String col : cols){
        System.out.print(col + ", ");
      }
      break;
    case showColsWithType:
      CommandShowColsWithType cmdlistColWithType = (CommandShowColsWithType) rootCommander.getCommands().get("xmeta").getCommands().get(parsedCommand).getObjects().get(0);
      Map<String, String> colsWithType = cmdlistColWithType.exec(apiHandler);
      for(String key : colsWithType.keySet()){
        System.out.print(key + ":" + colsWithType.get(key) + ", ");
      }
      System.out.println();
      break;
    case getColLength:
      CommandGetColLength cmdColLen = (CommandGetColLength) rootCommander.getCommands().get("xmeta").getCommands().get(parsedCommand).getObjects().get(0);
      Map<String, Integer> colsLenRes = cmdColLen.exec(apiHandler);
      for(String key : colsLenRes.keySet()){
        System.out.print(key + ":" + colsLenRes.get(key));
      }
      break;
    case getColType:
      CommandGetColType cmdColType = (CommandGetColType) rootCommander.getCommands().get("xmeta").getCommands().get(parsedCommand).getObjects().get(0);
      Map<String, String> colType = cmdColType.exec(apiHandler);
      for(String key : colType.keySet()){
        System.out.print(key + ":" + colType.get(key) + ", ");
      }
      System.out.println();
      break;
    case getColAtPos:
      CommandGetColAtPos cmdGetColAtPos = (CommandGetColAtPos) rootCommander.getCommands().get("xmeta").getCommands().get(parsedCommand).getObjects().get(0);
      List<String> getColAtPos = cmdGetColAtPos.exec(apiHandler);
      for(String key : getColAtPos){
        System.out.println(key + ", ");
      }
      break;
    case getPK:
      CommandGetPK cmdGetPK = (CommandGetPK) rootCommander.getCommands().get("xmeta").getCommands().get(parsedCommand).getObjects().get(0);
      List<String> getPK = cmdGetPK.exec(apiHandler);
      for(String key : getPK){
        System.out.println(key + ", ");
      }
      break;
    case getFK:
      CommandGetFK cmdGetFK = (CommandGetFK) rootCommander.getCommands().get("xmeta").getCommands().get(parsedCommand).getObjects().get(0);
      List<String> getFK = cmdGetFK.exec(apiHandler);
      for(String key : getFK){
        System.out.println(key + ", ");
      }
      break;
    case getFKRefTables:
      CommandGetFKRefTables cmdGetFKRefTables = (CommandGetFKRefTables) rootCommander.getCommands().get("xmeta").getCommands().get(parsedCommand).getObjects().get(0);
      Map<String, String> GetFKRefTables = cmdGetFKRefTables.exec(apiHandler);
      for(String key : GetFKRefTables.keySet()){
        System.out.print(key + ":" + GetFKRefTables.get(key) + ", ");
      }
      System.out.println();
      break;
    case getIndexes:
      CommandGetIndexes cmdGetindexes = (CommandGetIndexes) rootCommander.getCommands().get("xmeta").getCommands().get(parsedCommand).getObjects().get(0);
      List<String> getindexes = cmdGetindexes.exec(apiHandler);
      for(String key : getindexes){
        System.out.println(key + ", ");
      }
      break;
    case getAllIdxRefTable:
      break;
    case existsDatabase:
      CommandExistsDatabase existsDb = (CommandExistsDatabase) rootCommander.getCommands().get("xmeta").getCommands().get(parsedCommand).getObjects().get(0);
      System.out.println(existsDb.exec(apiHandler));
      break;
    case existsTable:
      CommandExistsTable existsTbl = (CommandExistsTable) rootCommander.getCommands().get("xmeta").getCommands().get(parsedCommand).getObjects().get(0);
      System.out.println(existsTbl.exec(apiHandler));
      break;
    case existsColumn:
      CommandExistsColumn existsCol = (CommandExistsColumn) rootCommander.getCommands().get("xmeta").getCommands().get(parsedCommand).getObjects().get(0);
      System.out.println(existsCol.exec(apiHandler));
      break;
    case isSupport:
      break;
    default:
      break;
    }
  }
  private void registerCommands(JCommander rootCommander){
    rootCommander.addCommand("xmeta", new CommandXmeta());
    rootCommander.getCommands().get("xmeta").addCommand(new CommandConnect());
    rootCommander.getCommands().get("xmeta").addCommand(new CommandShowDatabases());
    rootCommander.getCommands().get("xmeta").addCommand(new CommandShowTables());
    rootCommander.getCommands().get("xmeta").addCommand(new CommandShowColumns());
    rootCommander.getCommands().get("xmeta").addCommand(new CommandShowColsWithType());
    rootCommander.getCommands().get("xmeta").addCommand(new CommandGetColLength());
    rootCommander.getCommands().get("xmeta").addCommand(new CommandGetColType());
    rootCommander.getCommands().get("xmeta").addCommand(new CommandExistsDatabase());
    rootCommander.getCommands().get("xmeta").addCommand(new CommandExistsTable());
    rootCommander.getCommands().get("xmeta").addCommand(new CommandExistsColumn());
    rootCommander.getCommands().get("xmeta").addCommand(new CommandGetPK());
    rootCommander.getCommands().get("xmeta").addCommand(new CommandGetFK());
    rootCommander.getCommands().get("xmeta").addCommand(new CommandGetFKRefTables());
    rootCommander.getCommands().get("xmeta").addCommand(new CommandGetIndexes());
    rootCommander.getCommands().get("xmeta").addCommand(new CommandGetColAtPos());
  }

  private boolean validated(String arg){
    if(arg == null)
      return false;
    if(arg.trim().length() < 2)
      return false;
    
    return true;
  }
}

enum AvailCommand{
  connect, showDatabases, showTables, showColumns, showColsWithType, getColLength, getColType, getColAtPos, getPK, getFK, getFKRefTables, getIndexes, getAllIdxRefTable, existsDatabase, existsTable, existsColumn, isSupport;
}
