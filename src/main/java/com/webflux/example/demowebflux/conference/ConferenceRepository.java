package com.webflux.example.demowebflux.conference;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConferenceRepository extends ReactiveMongoRepository<Conference, String> {
}
