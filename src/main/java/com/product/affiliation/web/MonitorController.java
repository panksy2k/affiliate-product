package com.product.affiliation.web;

import com.product.affiliation.models.Monitor;
import com.product.affiliation.repositories.MonitorRepository;
import io.vertx.core.Future;

public class MonitorController {

    private final MonitorRepository monitorRepository;

    public MonitorController(MonitorRepository monitorRepository) {
        this.monitorRepository = monitorRepository;
    }

    public void createMonitor(Monitor product) {
        //Validate
        //
    }
}
