package com.product.affiliation.repositories;

import com.product.affiliation.models.Monitor;
import com.product.affiliation.models.ProductQuery;
import com.product.affiliation.models.RefreshRate;
import com.product.affiliation.models.ResponseTime;
import com.product.affiliation.models.ScreenSize;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxTestContext;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MonitorRepositoryImplTest extends AbstractRepository {

    MonitorRepositoryImpl SUT;

    @Test
    public void testCreateMonitor(VertxTestContext context) {
        //Given
      Monitor temp = new Monitor(null, "66F6UAC3UK");
      temp.setScreenSize(new ScreenSize(27f, ScreenSize.ScreenUnit.Inches));
      temp.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
      temp.setResponseTime(new ResponseTime(0.5f, ResponseTime.Measurement.Milliseconds));

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
    Monitor temp_1 = new Monitor(null, "66F6UAC3UK");
    temp_1.setScreenSize(new ScreenSize(27f, ScreenSize.ScreenUnit.Inches));
    temp_1.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 165));
    temp_1.setResponseTime(new ResponseTime(0.5f, ResponseTime.Measurement.Milliseconds));

    Monitor temp_2 = new Monitor(null, "66F6UAC3EU");
    temp_2.setScreenSize(new ScreenSize(32f, ScreenSize.ScreenUnit.Inches));
    temp_2.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 200));
    temp_2.setResponseTime(new ResponseTime(0.3f, ResponseTime.Measurement.Milliseconds));

    //When
    SUT = new MonitorRepositoryImpl(client);

    //Then
    context.verify(() -> {
      SUT.createMonitorInBatch(Arrays.asList(temp_1, temp_2))
        .onSuccess(m -> {
          Assertions.assertTrue(m);
          context.completeNow();
        })
        .onFailure(t -> context.failNow(t));
    });
  }

  @Test
  public void testRemoveMonitor(VertxTestContext context) {
    Monitor temp_1 = new Monitor(null, "66F6UAC3UK");
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
          Assertions.assertTrue(r);
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

    Monitor temp_1 = new Monitor(null, "66F6UAC3UK");
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
          Assertions.assertTrue(l.size() == 1);
          Assertions.assertEquals("MONITOR", l.get(0).getProductType());
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
}
