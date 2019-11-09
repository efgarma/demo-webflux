package com.webflux.example.demowebflux;

import com.webflux.example.demowebflux.conference.Conference;
import com.webflux.example.demowebflux.conference.ConferenceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@SpringBootTest
@AutoConfigureWebTestClient
class DemoWebfluxApplicationTests {

    @Autowired
    WebTestClient webClient;
    @MockBean
    ConferenceRepository conferenceRepository;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    void saveConference() {
        Conference conferenceBo = new Conference(UUID.randomUUID().toString(), "Erick Orozco", "Spring webflux");

        Mockito
                .when(conferenceRepository.save(ArgumentMatchers.any()))
                .thenAnswer((Answer<?>) args -> {
                    logger.info("New Conference {}", args.getArgument(0).toString());
                    return Mono.just(args.getArgument(0));
                });

        webClient.post()
                .uri("/conference")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(conferenceBo))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void getConferenceById() {

        final String id = UUID.randomUUID().toString();
        Mockito
                .when(conferenceRepository.findById(ArgumentMatchers.eq(id)))
                .then((Answer<?>) args -> Mono.just(new Conference(id, "Speaker A", "Topic A")));

        webClient.get()
                .uri("/conference/{id}", id)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Conference.class)
                .getResponseBody()
                .subscribe(data -> {
                    logger.info("Result: {}", data);
                });
    }

    @Test
    void getConferences() {

        Mockito
                .when(conferenceRepository.findAll())
                .then((Answer<?>) args -> Flux.fromArray(new Conference[]{
                        new Conference(UUID.randomUUID().toString(), "Speaker A", "Topic A"),
                        new Conference(UUID.randomUUID().toString(), "Speaker B", "Topic B"),
                        new Conference(UUID.randomUUID().toString(), "Speaker C", "Topic C")
                }));

        webClient.get()
                .uri("/conference")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Conference.class)
                .getResponseBody()
                .subscribe(data -> {
                    logger.info("Result: {}", data);
                });
    }

    @Test
    void updateConference() {

        final String id = UUID.randomUUID().toString();
        Mockito
                .when(conferenceRepository.findById(ArgumentMatchers.eq(id)))
                .then((Answer<?>) args -> {
                    Conference conference = new Conference(id, "Speaker A", "Old topic");
                    logger.info("Original Conference: {}", conference);
                    return Mono.just(conference);
                });

        // Change Value
        Conference conference = new Conference(id, "Speaker A", "New topic");
        Mockito
                .when(conferenceRepository.save(ArgumentMatchers.any()))
                .then((Answer<?>) args -> Mono.just(args.getArgument(0, Conference.class)));

        webClient.put()
                .uri("/conference")
                .body(BodyInserters.fromValue(conference))
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Conference.class)
                .getResponseBody()
                .subscribe(data -> {
                    logger.info("Result: {}", data);
                });
    }

    @Test
    void deleteById() {
        final String id = UUID.randomUUID().toString();
        Mockito
                .when(conferenceRepository.findById(ArgumentMatchers.eq(id)))
                .then((Answer<?>) args -> Mono.just(new Conference(id, "Speaker A", "Topic A")));

        Mockito
                .when(conferenceRepository.deleteById(ArgumentMatchers.anyString()))
                .thenAnswer((Answer<?>) args -> {
                    logger.info("Conference deleted: {}", args.getArgument(0).toString());
                    return Mono.empty();
                });

        webClient.delete()
                .uri("/conference/{id}", id)
                .exchange()
                .expectStatus()
                .isOk();
    }

}
