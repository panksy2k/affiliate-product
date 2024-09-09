package com.product.affiliation.config;

import io.vertx.core.json.JsonObject;
import java.util.Map;

public class ApplicationConfig {
    private JsonObject mongoDBConfig;

    public JsonObject getMongoDBConfig() {
        return mongoDBConfig;
    }

    public void setMongoDBConfig(JsonObject mongoDBConfig) {
        this.mongoDBConfig = mongoDBConfig;
    }
}
