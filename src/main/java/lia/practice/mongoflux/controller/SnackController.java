package lia.practice.mongoflux.controller;

import lia.practice.mongoflux.model.Snack;
import lia.practice.mongoflux.repository.SnackRepository;
import lia.practice.mongoflux.service.SnackService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

// Use UUID or String as paras/args depending on @Id datatype

@RestController
@RequestMapping("/snacks")
@CrossOrigin(origins = "http://localhost:3000")
public class SnackController {

    // Create logger object
    private static final Logger logger = LogManager.getLogger(SnackController.class);

    @Autowired
    private SnackRepository snackRepository;

    @Autowired
    private SnackService snackService;

//    @GetMapping
//    public Flux<Snack> getAllSnacks() {
//        return snackService.getAllSnacks();
//    }

    // Example getAllSnacks method with extensive trace example
    @GetMapping
    public Flux<Snack> getAllSnacks() {

        logger.trace("Entering getAllSnacks() method");

        Flux<Snack> snacks = snackService.getAllSnacks();

        snacks.doOnComplete(() -> logger.trace("Finished retrieving all snacks"))
                .doOnError(error -> logger.error("Error occurred while retrieving snacks: {}", error.getMessage()))
                .doOnNext(snack -> logger.trace("Retrieved snack: {}", snack.getId()))
                .subscribe();

        logger.trace("Leaving getAllItems() method");
        return snacks;
    }

    @GetMapping("/snackbyid/{id}")
    public Mono<Snack> getById(@PathVariable String id) {
        return snackService.getById(id);
    }

    @PostMapping("/createsnacks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<Snack> createSnack(@RequestBody Snack snack) {
//        return snackService.createSnack(snack);
        return snackService.createSnackNoDuplicate(snack); // USing the no duplicate logic
    }

    @PutMapping("/updatesnacks/{id}")
    public Mono<ResponseEntity<Snack>> updateSnack(@PathVariable String id, @RequestBody Snack snack) {
        return snackService.updateSnack(id, snack);
    }

    @DeleteMapping("/deletesnacks/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        return snackService.deleteById(id);
    }

}
