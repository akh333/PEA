package com.inmemory;

import io.helidon.metrics.RegistryFactory;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;
import org.eclipse.microprofile.metrics.*;

public class ResponseMetricsSupport implements Service {
    private final Counter[] responseCounters = new Counter[6];
    static ResponseMetricsSupport create() {
        return new ResponseMetricsSupport();
    }
    private ResponseMetricsSupport() {
        MetricRegistry appRegistry = RegistryFactory.getInstance().getRegistry(MetricRegistry.Type.APPLICATION);
        Metadata metadata = Metadata.builder()
                .withName("httpResponse")
                .withDisplayName("HTTP response values")
                .withDescription("Counts the number of HTTP responses in each status category (1xx, 2xx, etc.)")
                .withType(MetricType.COUNTER)
                .withUnit(MetricUnits.NONE)
                .build();
        for (int i = 1; i < responseCounters.length; i++) {
            responseCounters[i] = appRegistry.counter(metadata, new Tag("range", i + "xx"));
        }
    }
    @Override
    public void update(Routing.Rules rules) {
        rules.any(this::updateRange);
    }
    // Edited to adopt Ciaran's fix later in the thread.
    private void updateRange(ServerRequest request, ServerResponse response) {
        request.next();
        response.whenSent().thenRun(() -> logMetric(response));
    }
    private void logMetric(ServerResponse response) {
        int range = response.status().code() / 100;
        if (range > 0 && range < responseCounters.length) {
            responseCounters[range].inc();
        }
    }
}
