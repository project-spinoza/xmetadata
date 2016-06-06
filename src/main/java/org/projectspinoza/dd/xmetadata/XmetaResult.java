package org.projectspinoza.dd.xmetadata;

public class XmetaResult<T>{
  private int status;
  private String error;
  private String title;
  
  private T result;
  
  public int getStatus() {
    return status;
  }
  public void setStatus(int status) {
    this.status = status;
  }
  public String getError() {
    return error;
  }
  public void setError(String error) {
    this.error = error;
  }
  
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public T getResult() {
    return result;
  }
  public void setResult(T result) {
    this.result = result;
  }
  
  @Override
  public String toString() {
    return "XmetaResult [status=" + status + ", error=" + error + ", title=" + title + "]";
  }
}
