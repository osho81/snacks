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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

// Use UUID or String as paras/args depending on @Id datatype

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
        System.out.println(id);
        System.out.println(UUID.fromString(id));
        return snackRepository.findById(UUID.fromString(id));
//        return snackRepository.findById(id);
    }

    public Mono<Snack> createSnack(Snack snack) {

        // If no date/time is provided, set current date
        LocalDateTime creationDateTime;
        if (snack.getCreationDateTime() == null) {
            creationDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // Remove nano seconds

            // Else set the date/time provided form postman/frontend
        } else {
            creationDateTime = snack.getCreationDateTime().truncatedTo(ChronoUnit.SECONDS);
        }

        // Use Snack entity constructor, to generate uuid as id, before save in db
//        Snack tempSnack = new Snack(snack.getName(), snack.getFlavour(), snack.getWeight());

        // Example create snack and provide productId-UUID
//        Snack tempSnack = new Snack(snack.getName(), snack.getFlavour(), snack.getWeight(), UUID.randomUUID());

        // Example create snack and provide productId-UUID & creation date
        Snack tempSnack = new Snack(snack.getName(), snack.getFlavour(), snack.getWeight(), UUID.randomUUID(), creationDateTime);

        logger.info("Created a snack");
        return snackRepository.save(tempSnack);
    }

    // Create snack with logic rejecting duplicate names
    public Mono<Snack> createSnackNoDuplicate(Snack snack) {
        // Check if snack already exists in MongoDB
        // Use repository existByName method, that returns a boolean
        return snackRepository.existsByName(snack.getName())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RuntimeException("Duplicate snack found"));

                    } else { // If not already exist, set creation logic and save
                        LocalDateTime creationDateTime;
                        if (snack.getCreationDateTime() == null) {
                            creationDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
                        } else {
                            creationDateTime = snack.getCreationDateTime().truncatedTo(ChronoUnit.SECONDS);
                        }

                        Snack tempSnack = new Snack(snack.getName(), snack.getFlavour(), snack.getWeight(), UUID.randomUUID(), creationDateTime);

                        return snackRepository.save(tempSnack);
                    }
                });
    }

    public Mono<ResponseEntity<Snack>> updateSnack(String id, Snack snack) {
        return snackRepository.findById(UUID.fromString(id))
//        return snackRepository.findById(id)
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
        return snackRepository.deleteById(UUID.fromString(id));
//        return snackRepository.deleteById(id);
    }
}
