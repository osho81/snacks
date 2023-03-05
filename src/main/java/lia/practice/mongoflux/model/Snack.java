package lia.practice.mongoflux.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

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
//        this.id = id;
        this.id = UUID.randomUUID(); // Example using db generated UUID
        this.name = name;
        this.flavour = flavour;
        this.weight = weight;
        this.productId = productId; // Example assigning provided UUID
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
}
