package lia.practice.snacks.repository;

import lia.practice.snacks.model.Snack;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


// Set to String or UUID depending on @Id datatype in entity
public interface SnackRepository extends ReactiveMongoRepository<Snack, UUID> { // UUID used in Snack Entity
    Mono<Snack> findByName(String name);

    Mono<Boolean> existsByName(String name);

    Mono<Boolean> existsById(UUID id);


}