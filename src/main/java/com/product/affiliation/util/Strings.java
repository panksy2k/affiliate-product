package com.product.affiliation.util;


import java.util.function.Function;

public final class Strings {

  public static <T> T tokens(String inputString, Function<String, T> factory) {
    if (inputString == null) {
      return null;
    }

    return factory.apply(inputString);
  }

  public static boolean isBlank(String s) {
    if (s == null) {
      return true;
    }

    if (s.trim().equals("")) {
      return true;
    }

    return false;
  }
}
