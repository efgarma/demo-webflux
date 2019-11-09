package com.webflux.example.demowebflux.conference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/conference")
public class ConferenceController {

    private final ConferenceRepository conferenceRepository;

    @Autowired
    public ConferenceController(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

    @PostMapping
    public Mono<Conference> saveConference(@RequestBody Conference conference) {
        return this.conferenceRepository.save(conference);
    }

    @GetMapping("/{id}")
    public Mono<Conference> getConference(@PathVariable("id") String id) {
        return this.conferenceRepository.findById(id);
    }

    @PutMapping
    public Mono<ResponseEntity<Conference>> updateConference(@RequestBody Conference conference) {
        return this.conferenceRepository.findById(conference.getId())
                .flatMap(existingConference -> this.conferenceRepository.save(conference))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteConference(@PathVariable String id) {
        return this.conferenceRepository.findById(id)
                .flatMap(existingConference -> this.conferenceRepository.deleteById(existingConference.getId())
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Conference> getAll() {
        return this.conferenceRepository.findAll();
    }

}
