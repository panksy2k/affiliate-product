package com.product.affiliation.validators;

import com.product.affiliation.exceptions.ValidationError;
import com.product.affiliation.models.Monitor;
import com.product.affiliation.util.Strings;
import io.vertx.core.Future;
import java.util.Objects;

public class ProductValidator {
  public static Future<Monitor> validateCreateMonitor(Monitor createMonitor) {
    try {
      if (createMonitor == null) {
        throw new ValidationError("Monitor object is null");
      }

      final Monitor newMonitor = Monitor.withId(null, createMonitor);

      if (Strings.isBlank(newMonitor.getProductType())) {
        throw new ValidationError("Product Type is null");
      }

      if (Strings.isBlank(newMonitor.getModelName())) {
        throw new ValidationError("Model name is null");
      }

      Objects.requireNonNull(newMonitor.getRefreshRate(), "Refresh rate is unknown");
      Objects.requireNonNull(newMonitor.getScreenSize(), "Screen Size is unknown");

      return Future.succeededFuture(newMonitor);

    } catch (Exception e) {
      return Future.failedFuture(e.getMessage());
    }
  }

  public static Future<String> validateMonitorID(String id) {
    if (Strings.isBlank(id)) {
      return Future.failedFuture("ID is blank or null");
    }

    return Future.succeededFuture(id);
  }
}
