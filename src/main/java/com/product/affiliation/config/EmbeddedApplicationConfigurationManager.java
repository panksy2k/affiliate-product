package com.product.affiliation.config;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.HashMap;
import java.util.Map;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

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

            return appConfig;
        });
    }
}
