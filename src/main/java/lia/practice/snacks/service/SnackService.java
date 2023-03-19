package lia.practice.snacks.service;

import lia.practice.snacks.model.Snack;
import lia.practice.snacks.repository.SnackRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

// Use UUID or String as paras/args depending on @Id datatype

@Service
public class SnackService {

    // Create logger object
    private static final Logger logger = LogManager.getLogger(SnackService.class);

//    @Autowired // Avoid field injection
//    private SnackRepository snackRepository;

    // Constructor injection
    private final SnackRepository snackRepository;

    public SnackService(SnackRepository snackRepository) {
        this.snackRepository = snackRepository;
    }

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
        String creationDateTime;
        if (snack.getCreationDateTimeString() == null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Specify format
            String formattedDateTime = LocalDateTime.now().format(formatter); // Apply format
            creationDateTime = formattedDateTime;

            // Else set the date/time provided form postman/frontend
        } else {
            creationDateTime = snack.getCreationDateTimeString();
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
                        String creationDateTime;
                        if (snack.getCreationDateTimeString() == null) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Specify format
                            String formattedDateTime = LocalDateTime.now().format(formatter); // Apply format
                            creationDateTime = formattedDateTime;
                        } else {
                            creationDateTime = snack.getCreationDateTimeString();
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

    public Mono<ResponseEntity<Snack>> updateSnackNoDuplicate(String id, Snack snack) {
        return snackRepository.findById(UUID.fromString(id))
                .flatMap(existingSnack ->
                        // Also check if input name already exists
                        snackRepository.existsByName(snack.getName())
                                .flatMap(exists -> { // Check if exist but ignore duplicate if it is same as itself
                                    if (exists && !snack.getName().trim().equalsIgnoreCase(existingSnack.getName().trim())) {
                                        return Mono.error(new RuntimeException("Duplicate snack name"));

                                    } else { // If name is free to use, update/save new fields
                                        existingSnack.setName(snack.getName());
                                        existingSnack.setFlavour(snack.getFlavour());
                                        existingSnack.setWeight(snack.getWeight());
                                        // Test string date/time:
                                        existingSnack.setCreationDateTimeString(snack.getCreationDateTimeString());
                                        return snackRepository.save(existingSnack);
                                    }
                                }))
                .map(updatedSnack -> new ResponseEntity<>(updatedSnack, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

//    public Mono<Void> deleteById(String id) {
//        return snackRepository.deleteById(UUID.fromString(id));
////        return snackRepository.deleteById(id);
//    }

    // Delete with error handle
//    public Mono<Void> deleteById(String id) {
//        return snackRepository.deleteById(UUID.fromString(id))
//                .doOnSuccess(result -> logger.info("Snack with id {} has been deleted", id)) // Placeholder
//                .doOnError(error -> {
//                    logger.error("Failed to delete snack with id {}: {}", id, error.getMessage());
//                    throw new RuntimeException("Failed to delete snack");
//                });
//    }

    public Mono<Void> deleteById(String id) {
        return snackRepository.findById(UUID.fromString(id))
                .flatMap(existing -> snackRepository.deleteById(UUID.fromString(id))
                        .doOnSuccess(result -> logger.info("Snack with id {} has been deleted", id)))
                .switchIfEmpty(Mono.defer(() -> {
                    logger.info("No snack found with id {}", id);
                    return Mono.empty();
                }))
                .onErrorResume(error -> {
                    logger.error("Failed to delete snack with id {}: {}", id, error.getMessage());
                    return Mono.error(new RuntimeException("Failed to delete snack"));
                });


    }
