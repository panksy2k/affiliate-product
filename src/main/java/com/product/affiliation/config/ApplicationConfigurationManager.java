package com.product.affiliation.config;

import io.vertx.core.Future;

public interface ApplicationConfigurationManager {
    Future<ApplicationConfig> retrieveApplicationConfiguration();
}
