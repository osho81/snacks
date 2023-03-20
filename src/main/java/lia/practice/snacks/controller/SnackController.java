package lia.practice.snacks.controller;

import lia.practice.snacks.model.Snack;
import lia.practice.snacks.service.SnackService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        return snackService.createSnackNoDuplicate(snack); // Using the no duplicate logic
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

    // DeleteById with ResponseEntity
    @DeleteMapping("/deletesnacks/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return snackService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(error -> {
                    // If error is returned from service, handle error here as well
                    logger.error("Failed to delete snack with id {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                })
                .map(response -> ResponseEntity.status(response.getStatusCode()).build());
    }

}
