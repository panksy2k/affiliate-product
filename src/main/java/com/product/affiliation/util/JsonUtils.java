package com.product.affiliation.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {

  public static <T> JsonArray flattenCollectionToMultiples(List<List<T>> toFlat) {
    final JsonArray temp = new JsonArray();

    if (toFlat == null || toFlat.isEmpty()) {
      return temp;
    }

    for (int i = 0; i < toFlat.size(); i++) {
      Object ts = toFlat.get(i);

      if (ts instanceof ArrayList<?>) {
        ArrayList<?> underlyingList = (ArrayList<?>) ts;

        if (!underlyingList.isEmpty()) {
          for (int j = 0; j < underlyingList.size(); j++) {
            Object t = underlyingList.get(j);

            if (t != null) {
              temp.add(t);
            }
          }
        }
      }

    }

    return temp;
  }

  // Helper method to flatten a JsonObject
  public static JsonObject flattenJsonObject(JsonObject jsonObject) {
    JsonObject flattened = new JsonObject();
    jsonObject.forEach(entry -> {
      if (entry.getValue() instanceof JsonObject) {
        JsonObject nested = flattenJsonObject((JsonObject) entry.getValue());
        nested.forEach(
          nestedEntry -> flattened.put(entry.getKey() + "." + nestedEntry.getKey(), nestedEntry.getValue()));
      } else {
        flattened.put(entry.getKey(), entry.getValue());
      }
    });
    return flattened;
  }
}
