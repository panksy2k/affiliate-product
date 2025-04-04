package com.product.affiliation;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import com.product.affiliation.models.Monitor;
import com.product.affiliation.models.ProductPrice;
import com.product.affiliation.models.ProductQuery;
import com.product.affiliation.models.ProductType;
import com.product.affiliation.models.RefreshRate;
import com.product.affiliation.models.ResponseTime;
import com.product.affiliation.models.ScreenSize;
import com.product.affiliation.repositories.MonitorRepository;
import com.product.affiliation.web.MonitorController;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(VertxExtension.class)
@ExtendWith(MockitoExtension.class)
public class MainVerticleTest {
  WebClient client;
  @Mock
  private MonitorRepository mockedRepo;
  @InjectMocks
  private MonitorController controller;

  @BeforeEach
  void setup(Vertx vertx, VertxTestContext context) {
    MainVerticle SUT = new MainVerticle(controller);
    client = WebClient.create(vertx);
    vertx.deployVerticle(SUT).onFailure(context::failNow).onSuccess(r -> context.completeNow());
  }

  @Test
  public void createMonitorEndpointTest(Vertx vertx, VertxTestContext context) {
    //Given
    Monitor temp =
      new Monitor(null, "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "23 Inch Monitor");
    temp.setScreenSize(new ScreenSize(27f, ScreenSize.ScreenUnit.Inches));
    temp.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp.setResponseTime(new ResponseTime(0.5f, ResponseTime.Measurement.Milliseconds));

    Monitor expectedPayload = Monitor.withId("1", temp);
    when(mockedRepo.createMonitor(Mockito.any(Monitor.class))).thenReturn(Future.succeededFuture(expectedPayload));

    //When
    JsonObject payload = JsonObject.mapFrom(temp);
    context.verify(() -> {
      client.postAbs("http://localhost:8080/api/monitor").sendJsonObject(payload)
        .onFailure(context::failNow)
        .onSuccess(result -> {
          System.out.println(result.bodyAsString());
          int responseCode = result.statusCode();
          Assertions.assertEquals(201, responseCode);
          JsonObject responseBodyJsonObject = result.bodyAsJsonObject();

          Assertions.assertNotNull(responseBodyJsonObject.getString("id"));
          Assertions.assertEquals(expectedPayload.getProductType(), responseBodyJsonObject.getString("productType"));
          Assertions.assertEquals(expectedPayload.getModelName(), responseBodyJsonObject.getString("modelName"));

          Assertions.assertNotNull(responseBodyJsonObject.getJsonObject("screenSize"));
          Assertions.assertEquals(27.0, responseBodyJsonObject.getJsonObject("screenSize").getValue("size"));
          Assertions.assertEquals(ScreenSize.ScreenUnit.Inches.name(),
            responseBodyJsonObject.getJsonObject("screenSize").getValue("unit"));

          Assertions.assertNotNull(responseBodyJsonObject.getJsonObject("refreshRate"));
          Assertions.assertEquals(165, responseBodyJsonObject.getJsonObject("refreshRate").getValue("value"));
          Assertions.assertEquals("HERTZ", responseBodyJsonObject.getJsonObject("refreshRate").getValue("measure"));

          Assertions.assertNotNull(responseBodyJsonObject.getJsonObject("responseTime"));
          Assertions.assertEquals(0.5, responseBodyJsonObject.getJsonObject("responseTime").getValue("value"));
          Assertions.assertEquals("Milliseconds",
            responseBodyJsonObject.getJsonObject("responseTime").getValue("measurement"));

          context.completeNow();
        });
    });
  }

  @Test
  public void testRemoveMonitor(VertxTestContext context) {
    //Given
    when(mockedRepo.removeMonitor(anyString())).thenReturn(Future.succeededFuture(true));

    //When
    context.verify(() -> client.deleteAbs("http://localhost:8080/api/monitor/1")
      .send()
      .onFailure(context::failNow)
      .onSuccess(h -> {
        int statusCode = h.statusCode();
        Assertions.assertEquals(200, statusCode);
        JsonObject entries = h.bodyAsJsonObject();
        Assertions.assertEquals(true, entries.getBoolean("deleted"));
        context.completeNow();
      })
    );
  }

  @Test
  public void testFindMonitorByIdFound(VertxTestContext context) {
    //Given
    Monitor temp =
      new Monitor(null, "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "23 Inch Monitor");
    temp.setScreenSize(new ScreenSize(27f, ScreenSize.ScreenUnit.Inches));
    temp.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp.setResponseTime(new ResponseTime(0.5f, ResponseTime.Measurement.Milliseconds));

    Monitor expectedPayload = Monitor.withId("1", temp);
    when(mockedRepo.findMonitorById(anyString())).thenReturn(Future.succeededFuture(Optional.of(expectedPayload)));

    //When + Then
    context.verify(() -> {
      client.getAbs("http://localhost:8080/api/monitor/1")
        .send()
        .onFailure(context::failNow)
        .onSuccess(result -> {
          int responseCode = result.statusCode();
          Assertions.assertEquals(200, responseCode);

          JsonObject responseBodyJsonObject = result.bodyAsJsonObject();
          Assertions.assertNotNull(responseBodyJsonObject.getString("id"));
          Assertions.assertEquals(expectedPayload.getProductType(), responseBodyJsonObject.getString("productType"));
          Assertions.assertEquals(expectedPayload.getModelName(), responseBodyJsonObject.getString("modelName"));

          Assertions.assertNotNull(responseBodyJsonObject.getJsonObject("screenSize"));
          Assertions.assertEquals(27.0, responseBodyJsonObject.getJsonObject("screenSize").getValue("size"));
          Assertions.assertEquals(ScreenSize.ScreenUnit.Inches.name(),
            responseBodyJsonObject.getJsonObject("screenSize").getValue("unit"));

          Assertions.assertNotNull(responseBodyJsonObject.getJsonObject("refreshRate"));
          Assertions.assertEquals(165, responseBodyJsonObject.getJsonObject("refreshRate").getValue("value"));
          Assertions.assertEquals("HERTZ", responseBodyJsonObject.getJsonObject("refreshRate").getValue("measure"));

          Assertions.assertNotNull(responseBodyJsonObject.getJsonObject("responseTime"));
          Assertions.assertEquals(0.5, responseBodyJsonObject.getJsonObject("responseTime").getValue("value"));
          Assertions.assertEquals("Milliseconds",
            responseBodyJsonObject.getJsonObject("responseTime").getValue("measurement"));
          context.completeNow();
        });
    });
  }

  @Test
  public void testFindMonitorByIdNotFound(VertxTestContext context) {
    //Given
    when(mockedRepo.findMonitorById(anyString())).thenReturn(Future.succeededFuture(Optional.empty()));

    //When + Then
    context.verify(() -> {
      client.getAbs("http://localhost:8080/api/monitor/1")
        .send()
        .onFailure(context::failNow)
        .onSuccess(result -> {
          int responseCode = result.statusCode();
          Assertions.assertEquals(404, responseCode);

          context.completeNow();
        });
    });
  }

  @Test
  public void testFindMonitorByAttributeName(VertxTestContext context) {
    //Given
    when(mockedRepo.findProductAttributes("refreshRate", "MONITOR")).thenReturn(
      Future.succeededFuture(Arrays.asList("50 HERTZ", "150 HERTZ")));

    //When + Then
    context.verify(() -> {
      client.getAbs("http://localhost:8080/api/monitor/attr/refreshRate")
        .send()
        .onFailure(context::failNow)
        .onSuccess(result -> {
          int responseCode = result.statusCode();
          Assertions.assertEquals(200, responseCode);

          JsonArray responseBodyJsonObject = result.bodyAsJsonArray();
          System.out.println(responseBodyJsonObject);
          Assertions.assertTrue(responseBodyJsonObject.size() > 0);
          Assertions.assertEquals("50 HERTZ", responseBodyJsonObject.getString(0));

          context.completeNow();
        });
    });
  }

  @Test
  public void testFindMonitorsOnCriteria(VertxTestContext context) {
    //Given
    Monitor temp_1 =
      new Monitor(null, "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "23 Inch Monitor");
    temp_1.setScreenSize(new ScreenSize(27f, ScreenSize.ScreenUnit.Inches));
    temp_1.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp_1.setResponseTime(new ResponseTime(0.5f, ResponseTime.Measurement.Milliseconds));

    Monitor temp_2 =
      new Monitor(null, "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "23 Inch Monitor");
    temp_2.setScreenSize(new ScreenSize(27f, ScreenSize.ScreenUnit.Inches));
    temp_2.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp_2.setResponseTime(new ResponseTime(0.5f, ResponseTime.Measurement.Milliseconds));

    when(mockedRepo.findMonitors(Mockito.anyList())).thenReturn(
      Future.succeededFuture(Arrays.asList(Monitor.withId("1", temp_1),
        Monitor.withId("2", temp_2))));

    context.verify(() -> {
      ProductQuery q1 = new ProductQuery();
      q1.setKey(MonitorRepository.REFRESH_RATE);
      q1.setValue("165 Hz");
      q1.setOperation(ProductQuery.Operator.IS);

      ProductQuery q2 = new ProductQuery();
      q2.setKey(MonitorRepository.SCREEN_SIZE);
      q2.setValue("27.0 Inches");
      q2.setOperation(ProductQuery.Operator.IS);

      JsonArray payloadRequest = JsonArray.of(q1, q2);

      client.postAbs("http://localhost:8080/api/monitors").sendJson(payloadRequest)
        .onFailure(context::failNow)
        .onSuccess(result -> {
          int responseCode = result.statusCode();
          Assertions.assertEquals(200, responseCode);

          JsonArray responseBodyJsonObject = result.bodyAsJsonArray();
          System.out.println(responseBodyJsonObject);
          Assertions.assertEquals(2, responseBodyJsonObject.size());

          context.completeNow();
        });
    });
  }
}
