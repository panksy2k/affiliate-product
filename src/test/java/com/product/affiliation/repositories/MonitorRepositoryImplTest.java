package com.product.affiliation.repositories;

import com.product.affiliation.models.Monitor;
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
}
