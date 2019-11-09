package com.webflux.example.demowebflux.dash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class DashDataHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public Mono<ServerResponse> getSimulatedData(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(this.getFluxData(), Flux.class);
    }

    private Flux<DashData> getFluxData() {
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));

        Flux<DashData> simulatedData = Flux.fromStream(Stream.generate(this::getRandomData));

        return Flux.zip(interval, simulatedData)
                .map((tuple2) -> {
                    logger.info("Tuple Value {} {}", tuple2.getT1(), tuple2.getT2());
                    return tuple2.getT2();
                });
    }

    private DashData getRandomData() {
        return new DashData(
                UUID.randomUUID().toString(),
                Math.random(),
                new Date()
        );
    }
}
