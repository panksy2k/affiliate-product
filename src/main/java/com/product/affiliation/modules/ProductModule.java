package com.product.affiliation.modules;

import com.google.inject.AbstractModule;
import com.product.affiliation.MainVerticle;
import com.product.affiliation.config.ApplicationConfig;
import com.product.affiliation.exceptions.DependencyCreationException;
import com.product.affiliation.models.ConditionProduct;
import com.product.affiliation.models.Monitor;
import com.product.affiliation.models.ProductPrice;
import com.product.affiliation.models.ProductType;
import com.product.affiliation.models.ProductWarranty;
import com.product.affiliation.models.RefreshRate;
import com.product.affiliation.models.ResponseTime;
import com.product.affiliation.models.ScreenSize;
import com.product.affiliation.repositories.MonitorRepository;
import com.product.affiliation.repositories.MonitorRepositoryImpl;
import com.product.affiliation.web.MonitorController;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 * This class does the IOC using Google guice and ensures all dependencies are
 * correctly abstracted with one that is binded at the top for exposure (via verticle)
 */
public class ProductModule extends AbstractModule {

  /**
   * We've abstracted MonitorController into MainVerticle
   */
  private final MongoClient mongoClient;
  private final MonitorRepository monitorRepository;
  private final MonitorController monitorController;

  public ProductModule(Vertx vtx, ApplicationConfig appConfig) throws DependencyCreationException {
    JsonObject mongoDBConfig = appConfig.getMongoDBConfig();

    if (mongoDBConfig == null || mongoDBConfig.isEmpty()) {
      throw new DependencyCreationException("mongoDB config not available - cannot instantiate mongoclient");
    }

    this.mongoClient = MongoClient.createShared(vtx, mongoDBConfig);

    establishCollection(mongoClient, MonitorRepository.COLLECTION_NAME);
    establishIndexes(mongoClient, MonitorRepository.COLLECTION_NAME);

    this.monitorRepository = new MonitorRepositoryImpl(mongoClient);
    this.monitorRepository.createMonitor(createSampleMonitor())
      .onSuccess(h -> {
        System.out.println("Monitor successfully created " + h.getId());
      })
      .onFailure(t -> System.err.println(t));

    this.monitorController = new MonitorController(this.monitorRepository);

    createCacheCluster(appConfig.getClusterManager());
  }

  private void createCacheCluster(HazelcastClusterManager clusterMgr) {
    Vertx.builder().withClusterManager(clusterMgr).buildClustered().onComplete(res -> {
      if (res.succeeded()) {
        System.out.println("Hazelcast cluster established!");
      } else {
        System.err.println("Hazelcast cluster formation failed!");
      }
    });
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
    bind(MainVerticle.class).toInstance(new MainVerticle(this.monitorController));
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
    Monitor temp =
      new Monitor(null, "16F6UAC3UK", new ProductPrice(297.65, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "BenQ 24 Inch Gaming Monitor");
    temp.setScreenSize(new ScreenSize(24f, ScreenSize.ScreenUnit.Inches));
    temp.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 200));
    temp.setResponseTime(new ResponseTime(0.75f, ResponseTime.Measurement.Milliseconds));
    temp.setProductCondition(ConditionProduct.New);
    temp.setWarranty(new ProductWarranty(18, ProductWarranty.Warranty.Months));
    temp.setAffiliateLink("http://pankajpardasani.co.uk");

    return temp;
  }
}
