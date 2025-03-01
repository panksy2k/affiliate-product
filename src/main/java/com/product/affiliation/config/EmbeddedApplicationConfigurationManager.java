package com.product.affiliation.config;

import com.hazelcast.config.Config;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.spi.cluster.hazelcast.ConfigUtil;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class EmbeddedApplicationConfigurationManager implements ApplicationConfigurationManager {

  private final Vertx vtx;

  public EmbeddedApplicationConfigurationManager(Vertx vtx) {
    this.vtx = vtx;
  }

  @Override
  public Future<ApplicationConfig> retrieveApplicationConfiguration() {
    return this.vtx.executeBlocking(promiseHandler -> {
      JsonObject mongoCfg = new JsonObject();
      mongoCfg.put("connection_string", "mongodb://localhost:27017");
      mongoCfg.put("db_name", "products");

      //complete
      promiseHandler.complete(mongoCfg);

    }).map(mongocfg -> {
      ApplicationConfig appConfig = new ApplicationConfig();
      appConfig.setMongoDBConfig(JsonObject.mapFrom(mongocfg));

      Config hazelcastConfig = ConfigUtil.loadConfig();
      hazelcastConfig.setClusterName("products-cluster");
      HazelcastClusterManager manager = new HazelcastClusterManager(hazelcastConfig);

      appConfig.setClusterManager(manager);
      return appConfig;
    });
  }
}
