package lia.practice.mongoflux.controller;

import lia.practice.mongoflux.model.Snack;
import lia.practice.mongoflux.repository.SnackRepository;
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

    @GetMapping
    public Flux<Snack> getAllSnacks() {
        return snackRepository.findAll();
    }

    @PostMapping("/createsnacks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<Snack> createSnack(@RequestBody Snack snack) {
        return snackRepository.save(snack);
    }

    @PutMapping("/updatesnacks/{name}")
    public Mono<ResponseEntity<Snack>> updateSnackByName(@PathVariable String name, @RequestBody Snack snack) {
        return snackRepository.findByName(name)
                .flatMap(existingSnack -> {
                    existingSnack.setName(snack.getName());
                    existingSnack.setFlavour(snack.getFlavour());
                    existingSnack.setWeight(snack.getWeight());
                    return snackRepository.save(existingSnack);
                })
                .map(updatedSnack -> new ResponseEntity<>(updatedSnack, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/deletesnacks/{name}")
    public Mono<Void> deleteSnackByName(@PathVariable String name)
    {
        return snackRepository.deleteByName(name);
    }


}
