package com.product.affiliation.modules;

import com.google.inject.AbstractModule;
import com.product.affiliation.MainVerticle;
import com.product.affiliation.config.ApplicationConfig;
import com.product.affiliation.exceptions.DependencyCreationException;
import com.product.affiliation.models.Monitor;
import com.product.affiliation.models.RefreshRate;
import com.product.affiliation.models.ResponseTime;
import com.product.affiliation.models.ScreenSize;
import com.product.affiliation.repositories.MonitorRepository;
import com.product.affiliation.repositories.MonitorRepositoryImpl;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * This class does the IOC using Google guice and ensures all dependencues are
 * correctly abstracted with one that is binded at the top for exposure (via verticle)
 */
public class ProductModule extends AbstractModule {

  /**
   * Currently we're abstracting mongoclient - change this as we built up layers
   */
  private final MongoClient mongoClient;
  private final MonitorRepository monitorRepository;

  public ProductModule(Vertx vtx, ApplicationConfig appConfig) throws DependencyCreationException {
    JsonObject mongoDBConfig = appConfig.getMongoDBConfig();

    if (mongoDBConfig == null || mongoDBConfig.isEmpty()) {
      throw new DependencyCreationException("mongoDB config not available - cannot instantiate mongoclient");
    }

    mongoClient = MongoClient.createShared(vtx, mongoDBConfig);

    establishCollection(mongoClient, MonitorRepository.COLLECTION_NAME);
    establishIndexes(mongoClient, MonitorRepository.COLLECTION_NAME);
    monitorRepository = new MonitorRepositoryImpl(mongoClient);

    monitorRepository.createMonitor(createSampleMonitor())
      .onSuccess(h -> {
        System.out.println("Monitor successfully created " + h.getId());
      })
      .onFailure(t -> System.err.println(t));
  }

  private void establishIndexes(MongoClient mongoClient, String collectionName) {
    mongoClient.createIndex(collectionName,
      new JsonObject().put("refreshRate", 1).put("responseTime", 1).put("screenSize", 1),
      h -> {
        if (h.succeeded()) {
          System.out.println("Created index on multiple fields ");
        } else {
          System.out.println("Compound index failed");
        }
      });

    mongoClient.createIndex(collectionName, new JsonObject().put("productType", 1),
      h -> {
        if (h.succeeded()) {
          System.out.println("Created index on productType fields");
        } else {
          System.out.println("Index on productType failed!");
        }
      });
  }

  @Override
  protected void configure() {
    bind(MainVerticle.class).toInstance(new MainVerticle(monitorRepository));
  }

  private void establishCollection(MongoClient mc, String collectionName) {
    mc.createCollection(collectionName, h -> {
      if (h.succeeded()) {
        System.out.println("Created DB " + collectionName);
      } else {
        System.out.println("Failed to create DB " + collectionName);
        h.cause().printStackTrace();
      }
    });
  }

  private Monitor createSampleMonitor() {
    Monitor temp = new Monitor(null, "66F6UAC3UK");
    temp.setScreenSize(new ScreenSize(27f, ScreenSize.ScreenUnit.Inches));
    temp.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp.setResponseTime(new ResponseTime(0.5f, ResponseTime.Measurement.Milliseconds));

    return temp;
  }
}
