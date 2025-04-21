package com.product.affiliation.models;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public enum MonitorSpecialFeatures {
  ANTI_GLARE_SCREEN("Anti Glare Screen"),
  HEIGHT_ADJUSTMENT("Height Adjustment"),
  TILT_ADJUSTMENT("Tilt Adjustment"),
  SWIVEL_ADJUSTMENT("Swivel Adjustment");

  private final String specialFeatureDesc;

  MonitorSpecialFeatures(String specialFeatureDesc) {
    this.specialFeatureDesc = specialFeatureDesc;
  }

  public static MonitorSpecialFeatures fromStringEnum(String specialFeatureName) {
    for (MonitorSpecialFeatures f : MonitorSpecialFeatures.values()) {
      if (f.name().equals(specialFeatureName)) {
        return f;
      }
    }

    return null;
  }


  public static Set<String> fromValues(Set<String> values) {
    if (values.isEmpty()) {
      return Collections.emptySet();
    }

    Set<String> specialFeatures = new LinkedHashSet<>();
    if (values != null) {
      for (String v : values) {
        MonitorSpecialFeatures feature = fromValue(v);
        if (feature != null) {
          specialFeatures.add(feature.name());
        }
      }
    }
    return specialFeatures;
  }

  public static MonitorSpecialFeatures fromValue(String value) {
    for (MonitorSpecialFeatures m : values()) {
      if (m.toString().equalsIgnoreCase(value)) {
        return m;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return specialFeatureDesc;
  }
}
