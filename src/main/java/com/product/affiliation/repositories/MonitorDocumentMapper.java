package com.product.affiliation.repositories;

import com.obsidiandynamics.func.CheckedFunction;
import com.product.affiliation.models.Monitor;
import com.product.affiliation.models.ProductPrice;
import com.product.affiliation.models.ProductType;
import com.product.affiliation.models.RefreshRate;
import com.product.affiliation.models.ResponseTime;
import com.product.affiliation.models.ScreenSize;
import com.product.affiliation.util.Strings;
import io.netty.util.internal.StringUtil;
import io.vertx.core.json.JsonObject;

public class MonitorDocumentMapper implements CheckedFunction<JsonObject, Monitor, RuntimeException> {
  @Override
  public Monitor apply(JsonObject document) throws RuntimeException {

    Monitor m = new Monitor(document.getString("_id"),
      document.getString(MonitorRepository.MODEL_NAME),
      Strings.tokens(document.getString(MonitorRepository.PRICE), ProductPrice::parse),
      ProductType.MONITOR
    );

    String refreshRateStored = document.getString(MonitorRepository.REFRESH_RATE);
    if (!StringUtil.isNullOrEmpty(refreshRateStored)) {
      RefreshRate rrate = Strings.tokens(refreshRateStored, RefreshRate::parse);
      if (rrate != null) {
        m.setRefreshRate(rrate);
      }
    }

    String responseTimeStored = document.getString(MonitorRepository.RESPONSE_TIME);
    if (!StringUtil.isNullOrEmpty(responseTimeStored)) {
      ResponseTime responseTime = Strings.tokens(responseTimeStored, ResponseTime::parse);
      if (responseTime != null) {
        m.setResponseTime(responseTime);
      }
    }

    String screenSizeStored = document.getString(MonitorRepository.SCREEN_SIZE);
    if (!StringUtil.isNullOrEmpty(screenSizeStored)) {
      ScreenSize screen = Strings.tokens(screenSizeStored, ScreenSize::parse);
      if (screen != null) {
        m.setScreenSize(screen);
      }
    }

    return m;
  }
}
