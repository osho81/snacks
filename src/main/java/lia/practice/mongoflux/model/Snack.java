package lia.practice.mongoflux.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

// Annotation declaring this as a mongoDB entity to create records from,
// and value is name of the collection
@Document (value = "snacks")
public class Snack {

    @Id
//    @MongoId
    private String id; // String, aligned with SnackRepository mongo-crud

//    @Field("name")
    private String name;

//    @Field("flavour")
    private String flavour;

//    @Field("weight")
    private double weight;

    public Snack() {
    }

    public Snack(String id, String name, String flavour, double weight) {
        this.id = id;
        this.name = name;
        this.flavour = flavour;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
