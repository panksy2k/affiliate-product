package com.product.affiliation.models;

import com.product.affiliation.util.Strings;

public enum MaxDisplayResolution {

  RES_1920_1080("1920 x 1080 Pixels"),
  RES_1920_1200("1920 x 1200 Pixels"),
  RES_2560_1600("2560 x 1600 Pixels"),
  RES_2560_1440("2560 x 1440 Pixels"),
  RES_3440_1440("3440 x 1440 Pixels"),
  RES_3840_1080("3840 x 1080 Pixels");


  private final String maxResolutionDesc;

  MaxDisplayResolution(String pixelsDesc) {
    maxResolutionDesc = pixelsDesc;
  }

  public static MaxDisplayResolution fromValue(String value) {
    if (Strings.isBlank(value)) {
      return null;
    }

    for (MaxDisplayResolution m : values()) {
      if (m.toString().equalsIgnoreCase(value)) {
        return m;
      }
    }

    return null;
  }

  @Override
  public String toString() {
    return maxResolutionDesc;
  }
}
