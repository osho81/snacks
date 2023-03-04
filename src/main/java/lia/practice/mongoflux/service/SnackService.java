package lia.practice.mongoflux.service;

import lia.practice.mongoflux.model.Snack;
import lia.practice.mongoflux.repository.SnackRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SnackService {

    // Create logger object
    private static final Logger logger = LogManager.getLogger(SnackService.class);

    @Autowired
    private SnackRepository snackRepository;

    public Flux<Snack> getAllSnacks() {
        logger.info("Get all snacks");
        return snackRepository.findAll();
    }

    public Mono<Snack> getById(String id) {
        return snackRepository.findById(id);
    }

    public Mono<Snack> createSnack(Snack snack) {
        logger.info("Created a snack");
        return snackRepository.save(snack);
    }

    public Mono<ResponseEntity<Snack>> updateSnack(String id, Snack snack) {
        return snackRepository.findById(id)
                .flatMap(existingSnack -> {
                    existingSnack.setName(snack.getName());
                    existingSnack.setFlavour(snack.getFlavour());
                    existingSnack.setWeight(snack.getWeight());
                    return snackRepository.save(existingSnack);
                })
                .map(updatedSnack -> new ResponseEntity<>(updatedSnack, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<Void> deleteById(String id) {
        return snackRepository.deleteById(id);
    }
}
