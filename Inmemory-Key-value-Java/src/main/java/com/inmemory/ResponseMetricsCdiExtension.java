package com.inmemory;

import io.helidon.microprofile.server.ServerCdiExtension;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;

import static javax.interceptor.Interceptor.Priority.LIBRARY_BEFORE;

public class ResponseMetricsCdiExtension implements Extension {
    void registerResponseMetrics(@Observes @Priority(LIBRARY_BEFORE + 50) @Initialized(ApplicationScoped.class) Object event,
                                 ServerCdiExtension serverCdiExtension) {
        ResponseMetricsSupport responseMetricsSupport = ResponseMetricsSupport.create();
        responseMetricsSupport.update(serverCdiExtension.serverRoutingBuilder());
    }
}
