package com.product.affiliation.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.product.affiliation.models.ConditionProduct;
import com.product.affiliation.models.Monitor;
import com.product.affiliation.models.ProductPrice;
import com.product.affiliation.models.ProductQuery;
import com.product.affiliation.models.ProductType;
import com.product.affiliation.models.ProductWarranty;
import com.product.affiliation.models.RefreshRate;
import com.product.affiliation.models.ResponseTime;
import com.product.affiliation.models.ScreenSize;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxTestContext;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class MonitorRepositoryImplTest extends AbstractRepository {

  MonitorRepositoryImpl SUT;

  @Test
  public void testCreateMonitor(VertxTestContext context) {
    //Given
    Monitor temp =
      new Monitor(null, "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "Sony 23 Inch");
    temp.setScreenSize(new ScreenSize(27f, ScreenSize.ScreenUnit.Inches));
    temp.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp.setResponseTime(new ResponseTime(0.5f, ResponseTime.Measurement.Milliseconds));
    temp.setWarranty(new ProductWarranty(3, ProductWarranty.Warranty.Years));
    temp.setProductCondition(ConditionProduct.Used);
    temp.setAffiliateLink("http://pankajpardasani.co.uk");

    //When
    SUT = new MonitorRepositoryImpl(client);

    //Then
    context.verify(() -> {
      SUT.createMonitor(temp)
        .onSuccess(m -> {
          System.out.println(m.toString());
          Assertions.assertNotNull(m.getId());
          context.completeNow();
        })
        .onFailure(t -> context.failNow(t));
    });
  }

  @Test
  public void testCreateMonitorInBatch(VertxTestContext context) {
    //Given
    Monitor temp_1 =
      new Monitor(null, "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "23 Inch Monitor");
    temp_1.setScreenSize(new ScreenSize(27f, ScreenSize.ScreenUnit.Inches));
    temp_1.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp_1.setResponseTime(new ResponseTime(0.5f, ResponseTime.Measurement.Milliseconds));
    temp_1.setWarranty(new ProductWarranty(3, ProductWarranty.Warranty.Years));
    temp_1.setProductCondition(ConditionProduct.Used);
    temp_1.setAffiliateLink("http://pankajpardasani.co.uk");

    Monitor temp_2 =
      new Monitor(null, "66F6UAC3EU", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "23 Inch Monitor");
    temp_2.setScreenSize(new ScreenSize(32f, ScreenSize.ScreenUnit.Inches));
    temp_2.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 200));
    temp_2.setResponseTime(new ResponseTime(0.3f, ResponseTime.Measurement.Milliseconds));
    temp_2.setWarranty(new ProductWarranty(1, ProductWarranty.Warranty.Years));
    temp_2.setProductCondition(ConditionProduct.New);
    temp_2.setAffiliateLink("http://pankajpardasani.co.uk");

    //When
    SUT = new MonitorRepositoryImpl(client);

    //Then
    context.verify(() -> {
      SUT.createMonitorInBatch(Arrays.asList(temp_1, temp_2))
        .onSuccess(m -> {
          assertTrue(m);
          context.completeNow();
        })
        .onFailure(t -> context.failNow(t));
    });
  }

  @Test
  public void testRemoveMonitor(VertxTestContext context) {
    Monitor temp_1 =
      new Monitor(null, "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "23 Inch Monitor");
    temp_1.setScreenSize(new ScreenSize(27f, ScreenSize.ScreenUnit.Inches));
    temp_1.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp_1.setResponseTime(new ResponseTime(0.5f, ResponseTime.Measurement.Milliseconds));

    Checkpoint createCheckpoint = context.checkpoint();
    SUT = new MonitorRepositoryImpl(client);

    //Then
    context.verify(() -> {
      SUT.createMonitor(temp_1)
        .map(m -> {
          String id = m.getId();
          createCheckpoint.flag();
          return id;
        })
        .compose(id -> SUT.removeMonitor(id))
        .onSuccess(r -> {
          assertTrue(r);
          context.completeNow();
        })
        .onFailure(context::failNow);
    });
  }

  @Test
  public void testFindAllByScreenSize(VertxTestContext context) {
    SUT = new MonitorRepositoryImpl(client);
    Checkpoint createChkpoint = context.checkpoint();
    Checkpoint retrieveCheckpoint = context.checkpoint();

    Monitor temp_1 =
      new Monitor(null, "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "23 Inch Monitor");
    temp_1.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp_1.setScreenSize(new ScreenSize(27.0F, ScreenSize.ScreenUnit.Inches));
    temp_1.setResponseTime(new ResponseTime(0.5F, ResponseTime.Measurement.Milliseconds));

    context.verify(() -> {

      SUT.createMonitor(temp_1)
        .map(result -> {
          String primaryId = result.getId();
          Assertions.assertNotNull(primaryId);
          createChkpoint.flag();

          return primaryId;
        })
        .compose(id -> {
          System.out.println(id);
          ProductQuery q1 = new ProductQuery();
          q1.setKey(MonitorRepository.REFRESH_RATE);
          q1.setValue("165 Hz");
          q1.setOperation(ProductQuery.Operator.IS);

          ProductQuery q2 = new ProductQuery();
          q2.setKey(MonitorRepository.SCREEN_SIZE);
          q2.setValue("27.0 Inches");
          q2.setOperation(ProductQuery.Operator.IS);

          return SUT.findMonitors(Arrays.asList(q1, q2));
        })
        .map(l -> {
          assertTrue(l.size() == 1);
          Assertions.assertEquals(ProductType.MONITOR, l.get(0).getProductType());
          Assertions.assertEquals(165, l.get(0).getRefreshRate().getValue());
          Assertions.assertEquals(27.0F, l.get(0).getScreenSize().getSize());
          Assertions.assertEquals(0.5f, l.get(0).getResponseTime().getValue());

          retrieveCheckpoint.flag();

          System.out.println(l.toString());
          return l;
        })
        .onSuccess(s -> context.completeNow())
        .onFailure(context::failNow);
    });
  }

  @Test
  public void testFindAllIdByScreenSize(VertxTestContext context) {
    SUT = new MonitorRepositoryImpl(client);
    Checkpoint createChkpoint = context.checkpoint();
    Checkpoint retrieveCheckpoint = context.checkpoint();

    Monitor temp_1 =
      new Monitor(null, "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "23 Inch Monitor");
    temp_1.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp_1.setScreenSize(new ScreenSize(27.0F, ScreenSize.ScreenUnit.Inches));
    temp_1.setResponseTime(new ResponseTime(0.5F, ResponseTime.Measurement.Milliseconds));

    context.verify(() -> {

      SUT.createMonitor(temp_1)
        .map(result -> {
          String primaryId = result.getId();
          Assertions.assertNotNull(primaryId);
          createChkpoint.flag();

          return primaryId;
        })
        .compose(id -> {
          System.out.println(id);

          ProductQuery q2 = new ProductQuery();
          q2.setKey(MonitorRepository.SCREEN_SIZE);
          q2.setValue("27.0 Inches");
          q2.setOperation(ProductQuery.Operator.IN);

          return SUT.findMonitorsId(Arrays.asList(q2));
        })
        .map(l -> {
          assertTrue(l.size() == 1);
          Assertions.assertNotNull(l.get(0));

          retrieveCheckpoint.flag();

          return l;
        })
        .onSuccess(s -> context.completeNow())
        .onFailure(context::failNow);
    });
  }

  @Test
  public void testFindMonitorByIdValid(VertxTestContext context) {
    SUT = new MonitorRepositoryImpl(client);
    Checkpoint createChkpoint = context.checkpoint();
    Checkpoint retrieveCheckpoint = context.checkpoint();

    Monitor temp_1 =
      new Monitor(null, "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "23 Inch Monitor");
    temp_1.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp_1.setScreenSize(new ScreenSize(27.0F, ScreenSize.ScreenUnit.Inches));
    temp_1.setResponseTime(new ResponseTime(0.5F, ResponseTime.Measurement.Milliseconds));

    context.verify(() -> {

      SUT.createMonitor(temp_1)
        .map(result -> {
          String primaryId = result.getId();
          Assertions.assertNotNull(primaryId);
          createChkpoint.flag();

          return primaryId;
        })
        .compose(id -> {
          System.out.println(id);
          return SUT.findMonitorById(id);
        })
        .map(l -> {
          assertTrue(l.isPresent());
          retrieveCheckpoint.flag();

          System.out.println(l.get().toString());
          return l;
        })
        .onSuccess(s -> context.completeNow())
        .onFailure(context::failNow);
    });
  }

  @Test
  public void testFindMonitorByIdInValid(VertxTestContext context) {
    SUT = new MonitorRepositoryImpl(client);
    Checkpoint createChkpoint = context.checkpoint();
    Checkpoint retrieveCheckpoint = context.checkpoint();

    Monitor temp_1 =
      new Monitor(null, "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "23 Inch Monitor");
    temp_1.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp_1.setScreenSize(new ScreenSize(27.0F, ScreenSize.ScreenUnit.Inches));
    temp_1.setResponseTime(new ResponseTime(0.5F, ResponseTime.Measurement.Milliseconds));

    context.verify(() -> {
      SUT.createMonitor(temp_1)
        .map(result -> {
          String primaryId = result.getId();
          Assertions.assertNotNull(primaryId);
          createChkpoint.flag();

          return primaryId;
        })
        .compose(id -> {
          System.out.println(id);
          return SUT.findMonitorById("something");
        })
        .map(l -> {
          assertFalse(l.isPresent());
          retrieveCheckpoint.flag();
          return l;
        })
        .onSuccess(s -> context.completeNow())
        .onFailure(context::failNow);
    });
  }

  @ParameterizedTest
  @ValueSource(strings = {"screenSize", "refreshRate", "responseTime", "productCondition"})
  public void testFindMonitorAttributeSet(String attributeName, VertxTestContext context) {
    //Given
    Monitor temp_1 =
      new Monitor(null, "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "23 Inch Monitor");
    temp_1.setScreenSize(new ScreenSize(27f, ScreenSize.ScreenUnit.Inches));
    temp_1.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp_1.setResponseTime(new ResponseTime(0.5f, ResponseTime.Measurement.Milliseconds));
    temp_1.setWarranty(new ProductWarranty(2, ProductWarranty.Warranty.Years));
    temp_1.setProductCondition(ConditionProduct.New);
    temp_1.setAffiliateLink("https://");

    Monitor temp_2 =
      new Monitor(null, "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "23 Inch Monitor");
    temp_2.setScreenSize(new ScreenSize(29f, ScreenSize.ScreenUnit.Inches));
    temp_2.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 195));
    temp_2.setResponseTime(new ResponseTime(0.35f, ResponseTime.Measurement.Milliseconds));
    temp_2.setWarranty(new ProductWarranty(2, ProductWarranty.Warranty.Years));
    temp_2.setProductCondition(ConditionProduct.New);
    temp_2.setAffiliateLink("https://");

    //When
    SUT = new MonitorRepositoryImpl(client);
    Checkpoint createChkpoint = context.checkpoint();
    Checkpoint retrieveCheckpoint = context.checkpoint();

    //Then
    context.verify(() -> {
      SUT.createMonitorInBatch(Arrays.asList(temp_1, temp_2))
        .map(result -> {
          Assertions.assertTrue(result);
          createChkpoint.flag();
          return result;
        })
        .compose(id -> SUT.findProductAttributes(attributeName, ProductType.MONITOR.name()))
        .map(l -> {
          System.out.println(l);
          assertTrue(l.size() == 2);
          retrieveCheckpoint.flag();
          return l;
        })
        .onSuccess(s -> context.completeNow())
        .onFailure(context::failNow);
    });
  }

  @Test
  public void testCreateMonitorInEmptyBatch(VertxTestContext context) {
    SUT = new MonitorRepositoryImpl(client);

    context.verify(() -> {
      SUT.createMonitorInBatch(Arrays.asList())
        .onSuccess(result -> context.failNow("Expected failure for empty batch"))
        .onFailure(t -> {
          Assertions.assertEquals("No data to persist", t.getMessage());
          context.completeNow();
        });
    });
  }

  @Test
  public void testRemoveMonitorInvalidId(VertxTestContext context) {
    SUT = new MonitorRepositoryImpl(client);

    context.verify(() -> {
      SUT.removeMonitor("invalid-id")
        .onSuccess(result -> {
          assertFalse(result);
          context.completeNow();
        })
        .onFailure(context::failNow);
    });
  }

  @Test
  public void testFindMonitorsByPartialCriteria(VertxTestContext context) {
    SUT = new MonitorRepositoryImpl(client);

    Monitor temp =
      new Monitor(null, "66F6UAC3UK", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR,
        "23 Inch Monitor");
    temp.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 144));
    temp.setScreenSize(new ScreenSize(24.0F, ScreenSize.ScreenUnit.Inches));

    context.verify(() -> {
      SUT.createMonitor(temp)
        .compose(result -> {
          ProductQuery query = new ProductQuery();
          query.setKey(MonitorRepository.REFRESH_RATE);
          query.setValue("144 Hz");
          query.setOperation(ProductQuery.Operator.IS);

          return SUT.findMonitors(Arrays.asList(query));
        })
        .onSuccess(monitors -> {
          assertTrue(monitors.size() > 0);
          Assertions.assertEquals(144, monitors.get(0).getRefreshRate().getValue());
          context.completeNow();
        })
        .onFailure(context::failNow);
    });
  }
}
