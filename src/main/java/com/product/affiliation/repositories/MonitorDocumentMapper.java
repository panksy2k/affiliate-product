package com.product.affiliation.repositories;

import com.obsidiandynamics.func.CheckedFunction;
import com.product.affiliation.models.ConditionProduct;
import com.product.affiliation.models.MaxDisplayResolution;
import com.product.affiliation.models.Monitor;
import com.product.affiliation.models.ProductPrice;
import com.product.affiliation.models.ProductType;
import com.product.affiliation.models.ProductWarranty;
import com.product.affiliation.models.RefreshRate;
import com.product.affiliation.models.ResponseTime;
import com.product.affiliation.models.ScreenSize;
import com.product.affiliation.util.Strings;
import io.netty.util.internal.StringUtil;
import io.vertx.core.json.JsonObject;

/**
 * To convert the mongodb json object into domain object (for UI/deserialization to json)
 */
public class MonitorDocumentMapper implements CheckedFunction<JsonObject, Monitor, RuntimeException> {
  @Override
  public Monitor apply(JsonObject document) throws RuntimeException {

    Monitor m = new Monitor(document.getString("_id"),
      document.getString(MonitorRepository.MODEL_NAME),
      Strings.tokens(document.getString(MonitorRepository.PRICE), ProductPrice::parse),
      ProductType.MONITOR,
      document.getString(MonitorRepository.DESCRIPTION)
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

    String warrantyStored = document.getString(MonitorRepository.WARRANTY);
    if (!StringUtil.isNullOrEmpty(warrantyStored)) {
      ProductWarranty prdWarranty = Strings.tokens(warrantyStored, ProductWarranty::parse);
      if (prdWarranty != null) {
        m.setWarranty(prdWarranty);
      }
    }

    m.setAffiliateLink(document.getString(MonitorRepository.AFFILIATE_LINK));
    m.setProductCondition(ConditionProduct.fromName(document.getString(MonitorRepository.PRODUCT_CONDITION)));
    m.setBrandName(document.getString(MonitorRepository.BRAND));

    String maxDisplayResolution = document.getString(MonitorRepository.DISPLAY_RESOLUTION);
    if (!StringUtil.isNullOrEmpty(maxDisplayResolution)) {
      MaxDisplayResolution matchedDisplayResolution = MaxDisplayResolution.fromValue(maxDisplayResolution);
      if (matchedDisplayResolution != null) {
        m.setMaxDisplayResolution(matchedDisplayResolution.toString());
      }
    }

    return m;
  }
}
