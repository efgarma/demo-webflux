package com.webflux.example.demowebflux.config;

import com.webflux.example.demowebflux.dash.DashDataHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(DashDataHandler dashDataHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/dash"), (serverRequest -> dashDataHandler.getSimulatedData(serverRequest)));
    }

}
