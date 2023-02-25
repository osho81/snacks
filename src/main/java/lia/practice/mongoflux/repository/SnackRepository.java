package lia.practice.mongoflux.repository;

import lia.practice.mongoflux.model.Snack;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SnackRepository extends ReactiveMongoRepository<Snack, String> {
    Mono<Snack> findByName(String name);

    Mono<Void> deleteByName(String name);


}
