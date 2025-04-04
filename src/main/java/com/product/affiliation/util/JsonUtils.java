package com.product.affiliation.util;

import io.vertx.core.json.JsonArray;
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

  public static <T> T flattenCollectionToScalarValue(List<?> toFlat) {
    if (toFlat == null || toFlat.isEmpty()) {
      return null;
    }

    for (int i = 0; i < toFlat.size(); i++) {
      Object ts = toFlat.get(i);

      if (ts instanceof ArrayList<?>) {
        ArrayList<T> underlyingList = (ArrayList<T>) ts;

        if (underlyingList.size() == 1) {
          T t = underlyingList.get(0);
          if (t != null) {
            return t;
          }
        }
      }
    }

    return null;
  }
}
