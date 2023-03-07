package lia.practice.mongofluxpractice.utility;

import lia.practice.mongofluxpractice.model.Snack;
import lia.practice.mongofluxpractice.repository.SnackRepository;
import lia.practice.mongofluxpractice.service.SnackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
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

            // Alternative 1: use repository crud and custom creation with logic here
            // ...

            // Alternative 2: Use service class method to save (use logic creation)
            snackService.createSnackNoDuplicateMock(snack1).subscribe();
            snackService.createSnackNoDuplicateMock(snack2).subscribe();
            snackService.createSnackNoDuplicateMock(snack3).subscribe();
            snackService.createSnackNoDuplicateMock(snack4).subscribe();
            snackService.createSnackNoDuplicateMock(snack5).subscribe();
        };

    }


}
