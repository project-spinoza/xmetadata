package org.projectspinoza.dd.xmetadata.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertx.core.Vertx;

public class HttpServerMain {
  private static Logger LOG = LogManager.getRootLogger();
  
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new XmetaVerticle(), stringAsyncResult -> {
      LOG.info("Xmeta verticle has deployed successfully!");
    });
  }

}
