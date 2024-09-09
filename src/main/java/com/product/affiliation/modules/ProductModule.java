package com.product.affiliation.modules;

import com.google.inject.AbstractModule;
import com.product.affiliation.MainVerticle;
import com.product.affiliation.config.ApplicationConfig;
import com.product.affiliation.exceptions.DependencyCreationException;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.Map;

/**
 * This class does the IOC using Google guice and ensures all dependencues are
 * correctly abstracted with one that is binded at the top for exposure (via verticle)
 */
public class ProductModule extends AbstractModule {

    /**
     * Currently we're abstracting mongoclient - change this as we built up layers
     */
    private MongoClient mongoClient;

    public ProductModule(Vertx vtx, ApplicationConfig appConfig) throws DependencyCreationException {
        JsonObject mongoDBConfig = appConfig.getMongoDBConfig();

        if(mongoDBConfig == null || mongoDBConfig.isEmpty()) {
            throw new DependencyCreationException("mongoDB config not available - cannot instantiate mongoclient");
        }

        mongoClient = MongoClient.createShared(vtx, mongoDBConfig);
    }

    @Override
    protected void configure() {
        bind(MainVerticle.class).toInstance(new MainVerticle(mongoClient));
    }
}
