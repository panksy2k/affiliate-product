package com.product.affiliation.util;

import java.util.Collection;

public class Utils {

  public static boolean isEmpty(Collection<?> aCollection) {
    if (aCollection == null) {
      return true;
    }

    return aCollection.isEmpty();
  }
}
