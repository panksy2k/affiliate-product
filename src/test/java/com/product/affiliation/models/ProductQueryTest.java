package com.product.affiliation.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.obsidiandynamics.json.Json;
import io.vertx.junit5.VertxExtension;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class ProductQueryTest {

  @Test
  public void testDeserializationProductQuery() throws Exception {
    String inputJson = "{\"k\":\"screenSize\",\"v\":27.2,\"operator\":\"IS\"}";

    ProductQuery parse = Json.getInstance().parse(inputJson, ProductQuery.class);

    assertEquals("screenSize", parse.getKey());
    assertEquals("IS", parse.getOperation().name());
    assertEquals(27.2, parse.getValue());

  }

  @Test
  public void testDeserializationProductCriteria() throws Exception {
    String inputJson =
      "[{\"k\":\"screenSize\",\"v\":27.2,\"operator\":\"IS\"},{\"k\":\"refreshRate\",\"v\":700,\"operator\":\"IS\"},"
        + "{\"k\":\"price\",\"v\":170.75,\"operator\":\"gt\"}]";

    ArrayList parse = Json.getInstance().parse(inputJson, ArrayList.class);

    assertTrue(parse.size() == 3);

    assertEquals("screenSize", ((LinkedHashMap) parse.get(0)).get("k"));
    assertEquals(27.2, ((LinkedHashMap) parse.get(0)).get("v"));
    assertEquals("IS", ((LinkedHashMap) parse.get(0)).get("operator"));

    assertEquals("refreshRate", ((LinkedHashMap) parse.get(1)).get("k"));
    assertEquals(700, ((LinkedHashMap) parse.get(1)).get("v"));
    assertEquals("IS", ((LinkedHashMap) parse.get(1)).get("operator"));

    assertEquals("price", ((LinkedHashMap) parse.get(2)).get("k"));
    assertEquals(170.75, ((LinkedHashMap) parse.get(2)).get("v"));
    assertEquals("gt", ((LinkedHashMap) parse.get(2)).get("operator"));
  }
}
