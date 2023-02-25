package lia.practice.mongoflux.controller;

import lia.practice.mongoflux.model.Snack;
import lia.practice.mongoflux.repository.SnackRepository;
import lia.practice.mongoflux.service.SnackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/snacks")
@CrossOrigin(origins = "http://localhost:3000")
public class SnackController {

    @Autowired
    private SnackRepository snackRepository;

    @Autowired
    private SnackService snackService;

    @GetMapping
    public Flux<Snack> getAllSnacks() {
        return snackService.getAllSnacks();
    }

    @GetMapping("/snackbyid/{id}")
    public Mono<Snack> getById(@PathVariable String id) {
        return snackService.getById(id);
    }

    @PostMapping("/createsnacks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<Snack> createSnack(@RequestBody Snack snack) {
        return snackService.createSnack(snack);
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
