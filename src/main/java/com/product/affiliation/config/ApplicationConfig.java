package com.product.affiliation.config;

import io.vertx.core.json.JsonObject;

public class ApplicationConfig {
    private JsonObject mongoDBConfig;

    public JsonObject getMongoDBConfig() {
        return mongoDBConfig;
    }

    public void setMongoDBConfig(JsonObject mongoDBConfig) {
        this.mongoDBConfig = mongoDBConfig;
    }
}
