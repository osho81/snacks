package lia.practice.snacks.controller;

import lia.practice.snacks.model.Snack;
import lia.practice.snacks.service.SnackService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

// Use UUID or String as paras/args depending on @Id datatype

@RestController
@RequestMapping("/snacks")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"}) // React & Angular
public class SnackController {

    // Create logger object
    private static final Logger logger = LogManager.getLogger(SnackController.class);

//    @Autowired // Avoid field injection
//    private SnackService snackService;

    // Constructor injection
    private final SnackService snackService;

    public SnackController(SnackService snackService) {
        this.snackService = snackService;
    }


//    @GetMapping
//    public Flux<Snack> getAllSnacks() {
//        return snackService.getAllSnacks();
//    }

    // Example getAllSnacks method with extensive trace example
    @GetMapping
    public Flux<Snack> getAllSnacks() {

        logger.info("Entering getAllSnacks() method");

        Flux<Snack> snacks = snackService.getAllSnacks();

        snacks.doOnComplete(() -> logger.trace("Finished retrieving all snacks"))
                .doOnError(error -> logger.error("Error occurred while retrieving snacks: {}", error.getMessage()))
                .doOnNext(snack -> logger.trace("Retrieved snack: {}", snack.getId()))
                .subscribe();

        logger.trace("Leaving getAllSnacks() method");
        return snacks;
    }

    // Basic get by id
//    @GetMapping("/snackbyid/{id}")
//    public Mono<Snack> getById(@PathVariable String id) {
//        return snackService.getById(id);
//    }

    // Get by id with responseEntity
    @GetMapping("/snackbyid/{id}")
    public Mono<ResponseEntity<Snack>> getById(@PathVariable String id) {
        return snackService.getById(id)
                .map(ResponseEntity::ok);
//                .onErrorResume(ResponseStatusException.class, e -> Mono.just(ResponseEntity.status(e.getStatusCode()).build()));
    }

//    @PostMapping("/createsnacks")
////    @ResponseStatus(value = HttpStatus.CREATED) // Non-customized response
//    public Mono<Snack> createSnack(@RequestBody Snack snack) {
////        return snackService.createSnack(snack);
//        return snackService.createSnackNoDuplicate(snack); // Using the no duplicate logic
//    }


    // Create snacks with response entity, error etc
    @PostMapping("/createsnacks")
    public Mono<ResponseEntity<Snack>> createSnack(@RequestBody Snack snack) {
        return snackService.createSnackNoDuplicate(snack)
                // On success return response incl. saved snack
                .map(savedSnack -> ResponseEntity.status(HttpStatus.CREATED).body(savedSnack))
                // On error throw error
//                .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
               .onErrorResume(ResponseStatusException.class, e -> Mono.just(ResponseEntity.status(e.getStatusCode()).build()));
    }

    @PutMapping("/updatesnacks/{id}")
    public Mono<ResponseEntity<Snack>> updateSnack(@PathVariable String id, @RequestBody Snack snack) {
//        return snackService.updateSnack(id, snack);
        return snackService.updateSnackNoDuplicate(id, snack); // Using the no duplicate name logic
    }

//    @DeleteMapping("/deletesnacks/{id}")
//    public Mono<Void> deleteById(@PathVariable String id) {
//        return snackService.deleteById(id);
//    }

    // delete by id with response etc
    @DeleteMapping("/deletesnacks/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return snackService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(error -> {
                    logger.error("Failed to delete snack with id {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                })
                .map(response -> ResponseEntity.status(response.getStatusCode()).build());
    }


    ////---- Methods used for multiple collection from same entity ----////
    ////---- Methods used for multiple collection from same entity ----////
    ////---- Methods used for multiple collection from same entity ----////

    // Use this if NOT have orgId in Snack entity
//    @PostMapping("/createsnacks/{orgId}") // Get orgId as pathVar
//    @ResponseStatus(value = HttpStatus.CREATED)
//    public Mono<Snack> createSnackInSpecificColl(@RequestBody Snack snack, @PathVariable UUID orgId) {
//        // Use multiple/separated collections service method
//        return snackService.createSnackInSpecificColl(snack, orgId);
//    }

    // Use this if have orgId in Snack entity
    @PostMapping("/createsnacks/specificcoll") // No orgId pathVar; will use getOrgId
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<Snack> createSnackInSpecificCollWithoutPathVar(@RequestBody Snack snack) {
        // Use multiple/separated collections service method
        return snackService.createSnackInSpecificCollWithoutPathVar(snack);
    }

    // Use this if NOT have orgId in Snack entity
//    @GetMapping("/{orgId}")
//    public Flux<Snack> getAllSnacksFromSpecificColl(@PathVariable UUID orgId) {
//
//        logger.info("Entering getAllSnacksFromSpecificColl() method");
//
//        Flux<Snack> snacks = snackService.getAllSnacksFromSpecificColl(orgId);
//
//        snacks.doOnComplete(() -> logger.trace("Finished retrieving all snacks"))
//                .doOnError(error -> logger.error("Error occurred while retrieving snacks: {}", error.getMessage()))
//                .doOnNext(snack -> logger.trace("Retrieved snack: {}", snack.getId()))
//                .subscribe();
//
//        logger.trace("Leaving getAllSnacksFromSpecificColl() method");
//        return snacks;
//    }

    // Use this if have orgId in Snack entity
    @GetMapping("/{id}") // Snack id (not orgId in this version)
    public Flux<Snack> getAllSnacksFromSpecificColl(@PathVariable String id) {

        logger.info("Entering getAllSnacksFromSpecificColl() method");

        Flux<Snack> snacks = snackService.getAllSnacksFromSpecificColl(id);

        snacks.doOnComplete(() -> logger.trace("Finished retrieving all snacks"))
                .doOnError(error -> logger.error("Error occurred while retrieving snacks: {}", error.getMessage()))
                .doOnNext(snack -> logger.trace("Retrieved snack: {}", snack.getId()))
                .subscribe();

        logger.info("Leaving getAllSnacksFromSpecificColl() method");
        return snacks;
    }

    // Use this if NOT have orgId in Snack entity
//    @GetMapping("/snackbyid/{id}/{orgId}")
//    public Mono<Snack> getByIdFromSpecificColl(@PathVariable String id, @PathVariable UUID orgId) {
//        return snackService.getByIdFromSpecificColl(id, orgId);
//    }

    // Use this if have orgId in Snack entity
    @GetMapping("/snackbyid/specificcoll/{id}")
    public Mono<ResponseEntity<Snack>> getByIdFromSpecificColl(@PathVariable String id) {
        return snackService.getByIdFromSpecificColl(id)
                .map(ResponseEntity::ok)
                .onErrorResume(ResponseStatusException.class, e -> Mono.just(ResponseEntity.status(e.getStatusCode()).build()));
    }

    @DeleteMapping("/deletesnacks/specificcoll/{id}")
    public Mono<ResponseEntity<Void>> deleteByIdInAllColl(@PathVariable String id) {
        return snackService.deleteByIdInAllColl(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(error -> {
                    logger.error("Failed to delete snack with id {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                })
                .map(response -> ResponseEntity.status(response.getStatusCode()).build());
    }


}
