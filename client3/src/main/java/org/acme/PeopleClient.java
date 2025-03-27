package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

/**
 * REST Client interface to access the PeopleResource.
 */
//@RegisterRestClient(baseUri = "http://localhost:8080/people") // Adjust base URI if needed
//wiremock
// @RegisterRestClient(baseUri = "http://localhost:8082/people") // Adjust base URI if needed
@RegisterRestClient(baseUri = "https://swapi.dev/api/people") // Adjust base URI if needed
public interface PeopleClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    PeopleResponse getPeople();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Person addPerson(Person person);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Person updatePerson(Person person);
}

/**
 * Represents the response from the PeopleResource for people.
 */
//class PeopleResponse {
//    public int count;
//    public String next;
//    public String previous;
//    public List<Person> results;
//}

/**
 * Represents a Person entity.
 */
//class Person {
//    public String name;
//    public String height;
//    public String mass;
//    public String hair_color;
//    public String skin_color;
//    public String eye_color;
//    public String birth_year;
//    public String gender;
//    public String homeworld;
//    public List<String> films;
//    public List<String> species;
//    public List<String> vehicles;
//    public List<String> starships;
//    public String created;
//    public String edited;
//    public String url;
//
//    // Default constructor required for JSON-B
//    public Person() {}
//
//    //public Person(String name, String height, String mass, String hair_color, String skin_color, String eye_color, String birth_year, String gender) {
//    public Person(String name, String height, String mass, String hair_color, String skin_color,
//                  String eye_color, String birth_year, String gender,
//                  String homeworld, List<String> films, List<String> species, List<String> vehicles,
//                  List<String> starships, String created, String edited, String url ) {
//        this.name = name;
//        this.height = height;
//        this.mass = mass;
//        this.hair_color = hair_color;
//        this.skin_color = skin_color;
//        this.eye_color = eye_color;
//        this.birth_year = birth_year;
//        this.gender = gender;
//        this.homeworld = homeworld;
//        this.films = films;
//        this.species = species;
//        this.vehicles = vehicles;
//        this.starships =  starships;
//        this.created = created;
//        this.edited = edited;
//        this.url = url;
//    }
// }