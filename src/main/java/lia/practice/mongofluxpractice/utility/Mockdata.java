package lia.practice.mongofluxpractice.utility;

import lia.practice.mongofluxpractice.model.Snack;
import lia.practice.mongofluxpractice.repository.SnackRepository;
import lia.practice.mongofluxpractice.service.SnackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Configuration // Enable bean for mockdata on startup
public class Mockdata {

    @Autowired
    private SnackService snackService;

    @Autowired
    private SnackRepository snackRepository;

    @Bean
    public CommandLineRunner databaseSeeder() {
        return args -> {

            // Create mock snacks:
            // Id (uuid) is generated (so not included as arg)
            // Product-id (uuid) is randomized here and provided to the snack constructor
            // Where date & time is null, it gets assigned current date/time
            Snack snack1 = new Snack("mock snack 1", "cheese", 150, UUID.randomUUID(), null);
            Snack snack2 = new Snack("mock snack 2", "salty", 275, UUID.randomUUID(), LocalDateTime.now().minusDays(1).minusHours(4).minusMinutes(30));
            Snack snack3 = new Snack("mock snack 3", "onion", 125, UUID.randomUUID(), null);
            Snack snack4 = new Snack("mock snack 4", "sourcream", 100, UUID.randomUUID(), LocalDateTime.now().minusDays(3).minusHours(2).minusMinutes(15));
            Snack snack5 = new Snack("mock snack 5", "cheese", 50, UUID.randomUUID(), LocalDateTime.now().minusDays(0).minusHours(0).minusMinutes(50));

            // Use method in this class to create snacks with no duplicate logic
            createSnackNoDuplicateMock(snack1).subscribe();
            createSnackNoDuplicateMock(snack2).subscribe();
            createSnackNoDuplicateMock(snack3).subscribe();
            createSnackNoDuplicateMock(snack4).subscribe();
            createSnackNoDuplicateMock(snack5).subscribe();

            // Alternatively, use service method or repository here to create snacks
        };

    }

    // Create method for MOCK data (message + empty error if snack already exists)
    // (Could also be in Service Method, with some slight changes)
    public Mono<Snack> createSnackNoDuplicateMock(Snack snack) {
        return snackRepository.existsByName(snack.getName())
                .flatMap(exists -> {
                    if (exists) {
//                        logger.info(snack.getName() + " already exist"); // Use logger
                        System.out.println(snack.getName() + " already exist");

                        return Mono.empty(); // Compulsory return here, so set to empty

                    } else {
                        // Handle if date & time is null
                        LocalDateTime creationDateTime;
                        if (snack.getCreationDateTime() == null) {
                            creationDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
                        } else {
                            creationDateTime = snack.getCreationDateTime().truncatedTo(ChronoUnit.SECONDS);
                        }

                        // uuid is provided in mock creation above
                        Snack tempSnack = new Snack(snack.getName(), snack.getFlavour(), snack.getWeight(), snack.getProductId(), creationDateTime);

//                        logger.info(snack.getName() + " created");
                        System.out.println(snack.getName() + " created");

                        // Save each snack passed into this method
                        return snackRepository.save(tempSnack);
                    }
                });
    }


}
