package com.product.affiliation.repositories;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@ExtendWith(VertxExtension.class)
@Testcontainers
public abstract class AbstractRepository {
  protected MongoClient client;
    @Container
    private MongoDBContainer mongoContainer = new MongoDBContainer(DockerImageName.parse("mongo:6.0"));

    @BeforeEach
    public void setup(Vertx vertx, VertxTestContext context) {
        JsonObject config = new JsonObject();
      //config.put("connection_string", "mongodb://127.0.0
      // .1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+2.1.1");
      config.put("connection_string", mongoContainer.getReplicaSetUrl());
      config.put("db_name", "products");

        client = MongoClient.createShared(vertx, config);
        context.completeNow();
    }

  @AfterEach
  public void destroy() {
    if (client != null) {
      client.close();
    }
  }
}
