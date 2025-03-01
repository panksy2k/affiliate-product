package com.product.affiliation.models;

public enum ConditionProduct {
  New, Used;

  @Override
  public String toString() {
    return this.name();
  }

  public static ConditionProduct fromName(String sourceMatch) {
    if (sourceMatch == null) {
      return null;
    }

    for (ConditionProduct temp : values()) {
      if (temp.name().equalsIgnoreCase(sourceMatch)) {
        return temp;
      }
    }

    return null;
  }
}
