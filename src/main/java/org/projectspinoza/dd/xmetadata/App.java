package org.projectspinoza.dd.xmetadata;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.projectspinoza.dd.xmetadata.cmd.CommandLineDriver;
import org.projectspinoza.dd.xmetadata.web.HttpServerMain;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * Meta Data Extraction
 *
 */
public class App {
  private static Logger LOG = LogManager.getRootLogger();
  
  @Parameter(names={"--mode", "-m"}, required = true)
  private String mode;
  
  public static void main(String[] args) {
    try{
      App app = new App();
      new JCommander(app, args);
      APP_MODE m = APP_MODE.valueOf(app.mode);
      switch(m){
      case cmd:
        CommandLineDriver.main(args);
        break;
      case rest:
        HttpServerMain.main(args);
        break;
      }
    }catch(Exception e){
      LOG.error("something went wrong!");
    }
  }
}

enum APP_MODE{cmd, rest}
