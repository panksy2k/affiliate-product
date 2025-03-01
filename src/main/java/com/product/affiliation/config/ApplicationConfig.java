package com.product.affiliation.config;

import io.vertx.core.json.JsonObject;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class ApplicationConfig {
  private JsonObject mongoDBConfig;
  private HazelcastClusterManager clusterManager;

  public JsonObject getMongoDBConfig() {
    return mongoDBConfig;
  }

  public void setMongoDBConfig(JsonObject mongoDBConfig) {
    this.mongoDBConfig = mongoDBConfig;
  }

  public HazelcastClusterManager getClusterManager() {
    return clusterManager;
  }

  public void setClusterManager(HazelcastClusterManager clusterManager) {
    this.clusterManager = clusterManager;
  }
}
