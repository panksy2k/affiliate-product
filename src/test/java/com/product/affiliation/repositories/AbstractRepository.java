package com.product.affiliation.repositories;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@ExtendWith(VertxExtension.class)
@Testcontainers
public abstract class AbstractRepository {
    @Container
    private MongoDBContainer mongoContainer = new MongoDBContainer(DockerImageName.parse("mongo:focal"));

    MongoClient client;

    @BeforeEach
    void setup(Vertx vertx, VertxTestContext context){
        JsonObject config = new JsonObject();
        String dbHost = mongoContainer.getHost();
        Integer dbPort = mongoContainer.getFirstMappedPort();

        config.put("port", dbPort);
        config.put("host", dbHost);

        client = MongoClient.createShared(vertx, config);
        context.completeNow();
    }
}
