package lia.practice.snacks.utility;

import lia.practice.snacks.model.Snack;
import lia.practice.snacks.repository.SnackRepository;
import lia.practice.snacks.service.SnackService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;

//@Configuration // Enable bean for mockdata on startup
@Component // More specific class annotation
public class Mockdata {

//    @Autowired // Avoid field injection
//    private SnackRepository snackRepository;

    // Constructor injection
    private final SnackRepository snackRepository;
    private final SnackService snackService;

    public Mockdata(SnackRepository snackRepository, SnackService snackService) {
        this.snackRepository = snackRepository;
        this.snackService = snackService;
    }


    @Bean
    public CommandLineRunner databaseSeeder() {
        return args -> {

            // Create mock snacks:
            // Id (uuid) is generated (so not included as arg)
            // Product-id (uuid) is randomized here and provided to the snack constructor
            // Where date & time is null, it gets assigned current date/time
            // (UUID for orgId generated at https://www.uuidgenerator.net/version1)
            Snack snack1 = new Snack(UUID.fromString("58cdac92-871f-4b41-9fbc-91693075a3f4"),"mock snack 1", "cheese", 150, UUID.randomUUID(), "2023-02-02 12:00:00");
            Snack snack2 = new Snack(UUID.fromString("58cdac92-871f-4b41-9fbc-91693075a3f4"),"mock snack 2", "salty", 275, UUID.randomUUID(), "2023-03-03 13:15:00");
            Snack snack3 = new Snack(UUID.fromString("66bbcf48-cf60-40a5-a564-1a8db0bea8bb"), "mock snack 3", "onion", 125, UUID.randomUUID(), "2023-02-12 15:35:00");
            Snack snack4 = new Snack(UUID.fromString("66bbcf48-cf60-40a5-a564-1a8db0bea8bb"),"mock snack 4", "sourcream", 100, UUID.randomUUID(), null);
            Snack snack5 = new Snack(null,"mock snack 5", "cheese", 50, UUID.randomUUID(), "2023-02-29 22:05:20");

            // Use method in this class to create snacks with no duplicate logic
//            createSnackNoDuplicateMock(snack1).subscribe();
//            createSnackNoDuplicateMock(snack2).subscribe();
//            createSnackNoDuplicateMock(snack3).subscribe();
//            createSnackNoDuplicateMock(snack4).subscribe();
//            createSnackNoDuplicateMock(snack5).subscribe();//

            // Use corresponding method in service class
//            snackService.createSnackNoDuplicate(snack1).subscribe();
//            snackService.createSnackNoDuplicate(snack2).subscribe();
//            snackService.createSnackNoDuplicate(snack3).subscribe();
//            snackService.createSnackNoDuplicate(snack4).subscribe();
//            snackService.createSnackNoDuplicate(snack5).subscribe();

            // Use save to specific collections (for 1-4)
            snackService.createInSpecificCollWithoutPathVarNoDuplicate(snack1).subscribe();
            snackService.createInSpecificCollWithoutPathVarNoDuplicate(snack2).subscribe();
            snackService.createInSpecificCollWithoutPathVarNoDuplicate(snack3).subscribe();
            snackService.createInSpecificCollWithoutPathVarNoDuplicate(snack4).subscribe();
            snackService.createInSpecificCollWithoutPathVarNoDuplicate(snack5).subscribe();
//            snackService.createSnackNoDuplicate(snack5).subscribe(); // snack5 saved to default coll
        };

    }

    // Create method for MOCK data (message + empty error if snack already exists)
    // (Could also be in Service Method, with some slight changes)
//    public Mono<Snack> createSnackNoDuplicateMock(Snack snack) {
//        return snackRepository.existsByName(snack.getName())
//                .flatMap(exists -> {
//                    if (exists) {
////                        logger.info(snack.getName() + " already exist"); // Use logger
//                        System.out.println(snack.getName() + " already exist");
//
//                        return Mono.empty(); // Compulsory return here, so set to empty
//
//                    } else {
//                        // Handle if date & time is null
//                        String creationDateTime;
//                        if (snack.getCreationDateTimeString() == null) {
//                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Specify format
//                            String formattedDateTime = LocalDateTime.now().format(formatter); // Apply format
//                            creationDateTime = formattedDateTime;
//                        } else {
//                            creationDateTime = snack.getCreationDateTimeString();
//                        }
//
//                        // uuid is provided in mock creation above
//                        Snack tempSnack = new Snack(snack.getName(), snack.getFlavour(), snack.getWeight(),
//                                snack.getProductId(), creationDateTime);
//
////                        logger.info(snack.getName() + " created");
//                        System.out.println(snack.getName() + " created");
//
//                        // Save each snack passed into this method
//                        return snackRepository.save(tempSnack);
//                    }
//                });
//    }


}
