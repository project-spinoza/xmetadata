package org.projectspinoza.dd.xmetadata.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;


public class XmetaVerticle extends AbstractVerticle {

  private HttpServer httpServer;
  private Router router;

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    super.start(startFuture);

    httpServer = vertx.createHttpServer();
    router = Router.router(vertx);

    router
        .route(HttpMethod.GET, "/xmeta/api/v1/")
        .produces("application/json")
        .handler(routingContext -> {
          Map<String, String> availableRoutes = new HashMap<String, String>();
          
          List<Route> allRoutes = router.getRoutes();
          for(Route r : allRoutes){
            availableRoutes.put(r.getPath(), r.toString());
          }
          
          Buffer responseData = null;
          try {
            responseData = Buffer.buffer(new ObjectMapper().writeValueAsBytes(availableRoutes));
          } catch (Exception e) {
            e.printStackTrace();
          }
          
          HttpServerResponse response = routingContext.response();
          response.setStatusCode(200);
          response.headers()
          .add("Content-Length", responseData.length()+"")
          .add("Content-Type", "application/json");
          response.end(responseData);
        });

    httpServer.requestHandler(router::accept).listen(8080);
  }

  @Override
  public void stop(Future<Void> stopFuture) throws Exception {
    super.stop(stopFuture);
  }

}
