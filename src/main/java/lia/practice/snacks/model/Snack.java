package lia.practice.snacks.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

// Annotation declaring this as a mongoDB entity to create records from,
// and value is name of the collection
@Document (value = "snacks")
//@Document // Without value, when creating multiple collections
public class Snack {

    @Id
    private UUID id; // If UUID in SnackRepository
    // private String id; // If String in SnackRepository

//    private UUID orgId; // Possible to include this

    private UUID productId;

    private String name;

    private String flavour;

    private double weight;

//    private LocalDateTime creationDateTime;

    private String creationDateTimeString;

    // Empty constructor
    public Snack() {
    }

    // Constructor without productId
    public Snack(String name, String flavour, double weight) {
//        this.id = id;
        this.id = UUID.randomUUID();
        this.name = name;
        this.flavour = flavour;
        this.weight = weight;
    }

    // Constructor including productId as well
    public Snack(String name, String flavour, double weight, UUID productId) {
        this.id = UUID.randomUUID(); // Example using db generated UUID
        this.name = name;
        this.flavour = flavour;
        this.weight = weight;
        this.productId = productId; // Example assigning provided UUID
    }

    // Constructor including DateTime as well
    public Snack(String name, String flavour, double weight, UUID productId, String creationDateTimeString) {
        this.id = UUID.randomUUID(); // Example using db generated UUID
        this.name = name;
        this.flavour = flavour;
        this.weight = weight;
        this.productId = productId; // Example assigning provided UUID
//        this.creationDateTime = creationDateTime.withNano(0); // Remove nano seconds
//        this.creationDateTime = creationDateTime.truncatedTo(ChronoUnit.SECONDS); // Remove nano seconds
        this.creationDateTimeString = creationDateTimeString;
    }


//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlavour() {
        return flavour;
    }

    public void setFlavour(String flavour) {
        this.flavour = flavour;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

//    public LocalDateTime getCreationDateTime() {
////        return creationDateTime.withNano(0);
//        return creationDateTime;
//    }
//
//    public void setCreationDateTime(LocalDateTime creationDateTime) {
////        this.creationDateTime = creationDateTime.withNano(0);
////        this.creationDateTime = creationDateTime.truncatedTo(ChronoUnit.SECONDS);
//        this.creationDateTime = creationDateTime;
//    }

    public String getCreationDateTimeString() {
        return creationDateTimeString;
    }

    public void setCreationDateTimeString(String creationDateTimeString) {
        this.creationDateTimeString = creationDateTimeString;
    }
}
