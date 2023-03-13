package lia.practice.snacks.utility;

import lia.practice.snacks.model.Snack;
import lia.practice.snacks.repository.SnackRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

//@Configuration // Enable bean for mockdata on startup
@Component // More specific class annotation
public class Mockdata {

//    @Autowired // Avoid field injection
//    private SnackRepository snackRepository;

    // Constructor injection
    private final SnackRepository snackRepository;

    public Mockdata(SnackRepository snackRepository) {
        this.snackRepository = snackRepository;
    }


    @Bean
    public CommandLineRunner databaseSeeder() {
        return args -> {

            // Create mock snacks:
            // Id (uuid) is generated (so not included as arg)
            // Product-id (uuid) is randomized here and provided to the snack constructor
            // Where date & time is null, it gets assigned current date/time
            Snack snack1 = new Snack("mock snack 1", "cheese", 150, UUID.randomUUID(), "2023-02-02 12:00:00");
            Snack snack2 = new Snack("mock snack 2", "salty", 275, UUID.randomUUID(), "2023-03-03 13:15:00");
            Snack snack3 = new Snack("mock snack 3", "onion", 125, UUID.randomUUID(), "2023-02-12 15:35:00");
            Snack snack4 = new Snack("mock snack 4", "sourcream", 100, UUID.randomUUID(), null);
            Snack snack5 = new Snack("mock snack 5", "cheese", 50, UUID.randomUUID(), "2023-02-29 22:05:20");

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
                        String creationDateTime;
                        if (snack.getCreationDateTimeString() == null) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Specify format
                            String formattedDateTime = LocalDateTime.now().format(formatter); // Apply format
                            creationDateTime = formattedDateTime;
                        } else {
                            creationDateTime = snack.getCreationDateTimeString();
                        }

                        // uuid is provided in mock creation above
                        Snack tempSnack = new Snack(snack.getName(), snack.getFlavour(), snack.getWeight(),
                                snack.getProductId(), creationDateTime);

//                        logger.info(snack.getName() + " created");
                        System.out.println(snack.getName() + " created");

                        // Save each snack passed into this method
                        return snackRepository.save(tempSnack);
                    }
                });
    }


}
