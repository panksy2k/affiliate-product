package com.product.affiliation.web;

import com.product.affiliation.models.ConditionProduct;
import com.product.affiliation.models.Monitor;
import com.product.affiliation.models.ProductPrice;
import com.product.affiliation.models.ProductType;
import com.product.affiliation.models.ProductWarranty;
import com.product.affiliation.models.RefreshRate;
import com.product.affiliation.models.ResponseTime;
import com.product.affiliation.models.ScreenSize;
import com.product.affiliation.repositories.MonitorRepository;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(VertxExtension.class)
@ExtendWith(MockitoExtension.class)
public class MonitorControllerTest {

  @Mock
  private MonitorRepository monitorRepository;

  @Mock
  private RoutingContext mockedRoutingContext;

  @Mock
  private HttpServerResponse mockedHttpResponse;

  @Test
  public void testCreateMonitor(VertxTestContext context) {
    //Given
    Monitor temp =
      new Monitor(null, "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "24 Inch BENQ Monitor");
    temp.setScreenSize(new ScreenSize(27f, ScreenSize.ScreenUnit.Inches));
    temp.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp.setResponseTime(new ResponseTime(0.5f, ResponseTime.Measurement.Milliseconds));
    temp.setWarranty(new ProductWarranty(2, ProductWarranty.Warranty.Years));
    temp.setAffiliateLink("pankajpardasani.co.uk/asjghfds/sdf?jdsghf");
    temp.setProductCondition(ConditionProduct.New);

    Mockito.when(mockedRoutingContext.getBodyAsJson()).thenReturn(JsonObject.mapFrom(temp));
    Mockito.when(mockedRoutingContext.response()).thenReturn(mockedHttpResponse);
    Mockito.when(mockedHttpResponse.setStatusCode(Mockito.anyInt())).thenReturn(mockedHttpResponse);
    Mockito.when(mockedHttpResponse.end(Mockito.anyString())).thenReturn(null);

    Mockito.when(monitorRepository.createMonitor(Mockito.any(Monitor.class)))
      .thenReturn(Future.succeededFuture(Monitor.withId("1", temp)));

    //When
    MonitorController SUT = new MonitorController(monitorRepository);

    context.verify(() -> {
      SUT.createMonitor(mockedRoutingContext);
      Mockito.verify(mockedRoutingContext, Mockito.never()).fail(Mockito.any(Throwable.class));
    });
    context.completeNow();
  }

  @Test
  public void testRemoveMonitor(VertxTestContext context) {
    //Given
    Mockito.when(mockedRoutingContext.getBodyAsJson()).thenReturn(JsonObject.of("deleted", "true"));
    Mockito.when(mockedRoutingContext.response()).thenReturn(mockedHttpResponse);
    Mockito.when(mockedHttpResponse.getStatusCode()).thenReturn(200);

    //When
    MonitorController SUT = new MonitorController(monitorRepository);

    context.verify(() -> {
      SUT.removeMonitor(mockedRoutingContext);
      Assertions.assertEquals(200, mockedRoutingContext.response().getStatusCode());
      Assertions.assertEquals("true", mockedRoutingContext.getBodyAsJson().getString("deleted"));
    });
    context.completeNow();
  }

  @Test
  public void testFindMonitorById(VertxTestContext context) {
    //Given
    Monitor temp =
      new Monitor("1", "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "Sony 23 Inch Monitor");
    temp.setScreenSize(new ScreenSize(27f, ScreenSize.ScreenUnit.Inches));
    temp.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp.setResponseTime(new ResponseTime(0.5f, ResponseTime.Measurement.Milliseconds));

    Mockito.when(mockedRoutingContext.getBodyAsJson()).thenReturn(JsonObject.mapFrom(temp));
    Mockito.when(mockedRoutingContext.response()).thenReturn(mockedHttpResponse);
    Mockito.when(mockedHttpResponse.getStatusCode()).thenReturn(200);

    //When
    MonitorController SUT = new MonitorController(monitorRepository);

    context.verify(() -> {
      SUT.findMonitorById(mockedRoutingContext);
      Assertions.assertEquals("MONITOR", mockedRoutingContext.getBodyAsJson().getString("productType"));
      Assertions.assertEquals(200, mockedRoutingContext.response().getStatusCode());
    });
    context.completeNow();
  }

  @Test
  public void testFindMonitorAttributesByName(VertxTestContext context) {
    //Given
    Monitor temp =
      new Monitor("1", "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "Dell 27 Inch Monitor");
    temp.setScreenSize(new ScreenSize(27f, ScreenSize.ScreenUnit.Inches));
    temp.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp.setResponseTime(new ResponseTime(0.5f, ResponseTime.Measurement.Milliseconds));

    Mockito.when(mockedRoutingContext.getBodyAsJsonArray()).thenReturn(JsonArray.of("165 HERTZ", "195 HERTZ"));
    Mockito.when(mockedRoutingContext.response()).thenReturn(mockedHttpResponse);
    Mockito.when(mockedHttpResponse.getStatusCode()).thenReturn(200);

    //When
    MonitorController SUT = new MonitorController(monitorRepository);

    context.verify(() -> {
      SUT.findMonitorAttribute(mockedRoutingContext);
      Assertions.assertEquals("[\"165 HERTZ\",\"195 HERTZ\"]", mockedRoutingContext.getBodyAsJsonArray().toString());
      Assertions.assertEquals(200, mockedRoutingContext.response().getStatusCode());
    });
    context.completeNow();
  }
}
