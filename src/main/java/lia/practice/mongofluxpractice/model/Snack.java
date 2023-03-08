package lia.practice.mongofluxpractice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

// Annotation declaring this as a mongoDB entity to create records from,
// and value is name of the collection
@Document (value = "snacks")
public class Snack {

    @Id
    private UUID id; // If UUID in SnackRepository
    // private String id; // If String in SnackRepository

    private UUID productId;

    private String name;

    private String flavour;

    private double weight;

    private LocalDateTime creationDateTime;

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
    public Snack(String name, String flavour, double weight, UUID productId, LocalDateTime creationDateTime) {
        this.id = UUID.randomUUID(); // Example using db generated UUID
        this.name = name;
        this.flavour = flavour;
        this.weight = weight;
        this.productId = productId; // Example assigning provided UUID
//        this.creationDateTime = creationDateTime.withNano(0); // Remove nano seconds
//        this.creationDateTime = creationDateTime.truncatedTo(ChronoUnit.SECONDS); // Remove nano seconds
        this.creationDateTime = creationDateTime; // Use this to also accept null here
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

    public LocalDateTime getCreationDateTime() {
//        return creationDateTime.withNano(0);
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
//        this.creationDateTime = creationDateTime.withNano(0);
//        this.creationDateTime = creationDateTime.truncatedTo(ChronoUnit.SECONDS);
        this.creationDateTime = creationDateTime;
    }
}
