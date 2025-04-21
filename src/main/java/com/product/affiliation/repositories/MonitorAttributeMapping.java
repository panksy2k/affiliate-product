package com.product.affiliation.repositories;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MonitorAttributeMapping implements Function<List<JsonObject>, List<String>> {

  private final List<String> m_underlineStringValue;
  private final String m_key;

  public MonitorAttributeMapping(String key) {
    m_underlineStringValue = new ArrayList<>();
    m_key = key;
  }


  @Override
  public List<String> apply(List<JsonObject> underlineDataJson) {
    if (!underlineDataJson.isEmpty() && !underlineDataJson.get(0).containsKey(m_key)) {
      return m_underlineStringValue;
    }

    for (JsonObject jo : underlineDataJson) {
      Object underlineValue = jo.getValue(m_key);

      if (underlineValue instanceof JsonArray) {
        JsonArray underlineValueArray = (JsonArray) underlineValue;
        underlineValueArray.stream().map(String::valueOf).forEach(e -> m_underlineStringValue.add(e));
      } else if (underlineValue instanceof String) {
        m_underlineStringValue.add(String.valueOf(underlineValue));
      }
    }

    return m_underlineStringValue;
  }
}
