package lia.practice.mongoflux.controller;

import lia.practice.mongoflux.model.Snack;
import lia.practice.mongoflux.repository.SnackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/snacks")
public class SnackController {

    @Autowired
    private SnackRepository snackRepository;

    @GetMapping
    public Flux<Snack> getAllSnacks() {
        return snackRepository.findAll();
    }

    @PostMapping("/createsnacks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<Snack> createSnack(@RequestBody Snack snack) {
        return snackRepository.save(snack);
    }

}
