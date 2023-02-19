package lia.practice.mongoflux.repository;

import lia.practice.mongoflux.model.Snack;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SnackRepository extends ReactiveMongoRepository<Snack, String> {
//public interface SnackRepository extends ReactiveCrudRepository<Snack, String> {
}
