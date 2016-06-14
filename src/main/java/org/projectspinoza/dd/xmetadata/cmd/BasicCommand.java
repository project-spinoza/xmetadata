package org.projectspinoza.dd.xmetadata.cmd;

import org.projectspinoza.dd.xmetadata.XmetaApi;

import com.beust.jcommander.Parameter;

public abstract class BasicCommand<T>{
  @Parameter(names = "--help", help = true, description = "Display help for command")
  private boolean help = false;

  public boolean needHelp() {
    return help;
  }
  public void setHelp(boolean help) {
    this.help = help;
  }
  
  public abstract T exec(XmetaApi api);
}
