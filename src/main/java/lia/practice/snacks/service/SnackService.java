package lia.practice.snacks.service;

import lia.practice.snacks.model.Snack;
import lia.practice.snacks.repository.SnackRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
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
    private SnackRepository snackRepository;
    private ReactiveMongoTemplate reactiveMongoTemplate; // Needed for the multiple collections approach

    public SnackService(SnackRepository snackRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.snackRepository = snackRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate; // Needed for the multiple collections approach
    }

    public Flux<Snack> getAllSnacks() {
        logger.info("Get all snacks");
        return snackRepository.findAll();
    }

    // Basic get by id
//    public Mono<Snack> getById(String id) {
//        return snackRepository.findById(UUID.fromString(id));
////        return snackRepository.findById(id);
//    }

    // Get by id with logic/error handle etc
    public Mono<Snack> getById(String id) {
        return snackRepository.findById(UUID.fromString(id))
                // If not exist, the task switches from finding to erroring
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Snack with id: " + id + " not found")));
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
//                        return Mono.error(new RuntimeException("Duplicate snack found"));

//                        logger.info(snack.getName() + " already exist"); // Use logger
                        System.out.println(snack.getName() + " already exist");

                        return Mono.empty(); // Compulsory return here, so set to empty

                    } else { // If not already exist, set creation logic and save
                        String creationDateTime;
                        if (snack.getCreationDateTimeString() == null) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Specify format
                            String formattedDateTime = LocalDateTime.now().format(formatter); // Apply format
                            creationDateTime = formattedDateTime;
                        } else {
                            creationDateTime = snack.getCreationDateTimeString();
                        }

                        // Generate uuid for productId here:
//                        Snack tempSnack = new Snack(snack.getName(), snack.getFlavour(), snack.getWeight(), UUID.randomUUID(), creationDateTime);

                        // Provided uuid for productId from postman/frontend etc:
                        Snack tempSnack = new Snack(snack.getName(), snack.getFlavour(), snack.getWeight(), snack.getProductId(), creationDateTime);

//                        logger.info(snack.getName() + " created");
                        System.out.println(snack.getName() + " created");

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

    public Mono<Void> deleteById(String id) {
        return snackRepository.deleteById(UUID.fromString(id));
//        return snackRepository.deleteById(id);
    }


    ////---- Methods used for multiple collection from same entity ----////
    ////---- Methods used for multiple collection from same entity ----////
    ////---- Methods used for multiple collection from same entity ----////

    // Use this if NOT have orgId in Snack entity
//    public Mono<Snack> createSnackInSpecificColl(Snack snack, UUID orgId) {
//
//        // If no date/time is provided, set current date
//        String creationDateTime;
//        if (snack.getCreationDateTimeString() == null) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Specify format
//            String formattedDateTime = LocalDateTime.now().format(formatter); // Apply format
//            creationDateTime = formattedDateTime;
//
//            // Else set the date/time provided form postman/frontend
//        } else {
//            creationDateTime = snack.getCreationDateTimeString();
//        }
//
//        Snack tempSnack = new Snack(snack.getName(), snack.getFlavour(), snack.getWeight(), snack.getProductId(), creationDateTime);
//
//        logger.info("Created a snack");
//
//        String collectionName = "assessments_" + orgId; // Create specified collection for this orgId
//
//        // Use reactiveMongoTEMPLATE to save snack into org-specific collection
//        return reactiveMongoTemplate.save(tempSnack, collectionName); // second arg = collection to save to
//    }

    // Use this if have orgId in Snack entity
    public Mono<Snack> createSnackInSpecificCollWithoutPathVar(Snack snack) {
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

        Snack tempSnack = new Snack(snack.getOrgId(), snack.getName(), snack.getFlavour(), snack.getWeight(), snack.getProductId(), creationDateTime);

        logger.info("Created a snack");

        String collectionName = "assessments_" + snack.getOrgId();

        // Use reactiveMongoTEMPLATE to save snack into org-specific collection
        return reactiveMongoTemplate.save(tempSnack, collectionName); // second arg = collection to save to
    }

    public Flux<Snack> getAllSnacksFromSpecificColl(UUID orgId) {
        logger.info("Get all snacks");
        String collectionName = "assessments_" + orgId;
        return reactiveMongoTemplate.findAll(Snack.class, collectionName);
    }

    public Mono<Snack> getByIdFromSpecificColl(String id, UUID orgId) {

        snackRepository.findById(UUID.fromString(id));
        String collectionName = "assessments_" + orgId;
        return reactiveMongoTemplate.findById(UUID.fromString(id), Snack.class, collectionName);
    }

}
