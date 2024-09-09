package com.product.affiliation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.product.affiliation.config.ApplicationConfigurationManager;
import com.product.affiliation.config.EmbeddedApplicationConfigurationManager;
import com.product.affiliation.exceptions.DependencyCreationException;
import com.product.affiliation.modules.ProductModule;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class MainVerticle extends AbstractVerticle {
    private final MongoClient mongoClient;

    public MainVerticle(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public static void main(String args[]) throws Exception {
        Vertx vertx = Vertx.vertx();

        //Retrieve the application configuration - pertaining to database etc
        ApplicationConfigurationManager configManager = new EmbeddedApplicationConfigurationManager(vertx);

        configManager.retrieveApplicationConfiguration()
                .compose(appCfg -> {
                    try {
                        ProductModule layers = new ProductModule(vertx, appCfg);
                        return Future.succeededFuture(layers);
                    } catch (DependencyCreationException e) {
                        return Future.failedFuture(e);
                    }
                })
                .map(prdModule -> {
                    Injector injector = Guice.createInjector(prdModule);
                    MainVerticle mainVerticle = injector.getInstance(MainVerticle.class);
                    return mainVerticle;
                })
                .compose(vertx::deployVerticle)
                .onFailure(err -> {
                    System.out.println(err.getMessage());
                    vertx.close();
                })
                .onSuccess(handler -> {
                    System.out.println(
                            "Product Verticle is successfully instantiated and deployed all its dependencies!");
                });
    }
}
