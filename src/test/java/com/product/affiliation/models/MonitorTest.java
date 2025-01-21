package com.product.affiliation.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.obsidiandynamics.json.Json;
import com.obsidiandynamics.json.JsonInputException;
import com.obsidiandynamics.json.JsonOutputException;
import io.vertx.junit5.VertxExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class MonitorTest {

  @Test
  public void testMonitorSerialization() throws JsonOutputException {
    //Given
    Monitor m =
      new Monitor("123", "68HV6788", new ProductPrice(233.45, ProductPrice.ProductCurrency.GBP), ProductType.MONITOR);
    m.setResponseTime(new ResponseTime(23.43f, ResponseTime.Measurement.Milliseconds));
    m.setRefreshRate(new RefreshRate(RefreshRate.RateUnit.HERTZ, 700));
    m.setScreenSize(new ScreenSize(27.2f, ScreenSize.ScreenUnit.Inches));
    m.setAffiliateLink("http://pankajpardasani/123456");
    m.setProductCondition(ConditionProduct.Used);
    m.setWarranty(new ProductWarranty(6, ProductWarranty.Warranty.Years));

    //When
    String actualResult = Json.getInstance().format(m);

    //Then
    assertEquals(
      "{\"id\":\"123\",\"modelName\":\"68HV6788\",\"price\":{\"cost\":233.45,\"productCurrency\":\"GBP\"},"
        + "\"refreshRate\":{\"measure\":\"HERTZ\",\"value\":700},\"responseTime\":{\"value\":23.43,"
        + "\"measurement\":\"Milliseconds\"},\"screenSize\":{\"size\":27.2,\"unit\":\"Inches\"},"
        + "\"affiliateLink\":\"http://pankajpardasani/123456\",\"productCondition\":\"Used\","
        + "\"warranty\":{\"warrantyValue\":6,\"warrantyUnit\":\"Years\"},\"productType\":\"MONITOR\"}",
      actualResult);
  }

  @Test
  public void testMonitorDeserialization() throws JsonInputException {
    //Given
    String inputJson =
      "{\"id\":\"123\",\"modelName\":\"68HV6788\",\"refreshRate\":{\"measure\":\"HERTZ\",\"value\":700},"
        + "\"responseTime\":{\"value\":23.43,\"measurement\":\"Milliseconds\"},\"screenSize\":{\"size\":27.2,"
        + "\"unit\":\"Inches\"},\"productType\":\"MONITOR\"}";

    //When
    Monitor actualResult = Json.getInstance().parse(inputJson, Monitor.class);

    //Then
    assertEquals("123", actualResult.getId());
    assertEquals(ProductType.MONITOR, actualResult.getProductType());
    assertEquals("68HV6788", actualResult.getModelName());

    assertEquals(700, actualResult.getRefreshRate().getValue());
    assertEquals("HERTZ", actualResult.getRefreshRate().getMeasure().name());

    assertEquals(23.43, actualResult.getResponseTime().getValue(), 0.00001);
    assertEquals("Milliseconds", actualResult.getResponseTime().getMeasurement().name());

    assertEquals(27.2f, actualResult.getScreenSize().getSize(), 0.00001);
    assertEquals("Inches", actualResult.getScreenSize().getUnit().name());
  }
}
