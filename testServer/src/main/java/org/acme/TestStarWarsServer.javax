package org.acme.mock;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Resource that simulates the Star Wars API's /people endpoint.
 */
@Path("/people")
public class PeopleResource {

    // In-memory store keyed by person name
    private static Map<String, Person> peopleStore = new HashMap<>();

    /**
     * Initializes the in-memory store with two default characters.
     */
    @PostConstruct
    public void init() {
        peopleStore.put("Luke Skywalker", new Person("Luke Skywalker", "172", "77", "blond", "fair", "blue", "19BBY", "male"));
        peopleStore.put("Darth Vader", new Person("Darth Vader", "202", "136", "none", "white", "yellow", "41.9BBY", "male"));
    }

    /**
     * GET endpoint to return all people.
     * 
     * @return a PeopleResponse containing a list of Person objects.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PeopleResponse getPeople() {
        PeopleResponse response = new PeopleResponse();
        List<Person> persons = new ArrayList<>(peopleStore.values());
        response.count = persons.size();
        response.next = null;
        response.previous = null;
        response.results = persons;
        return response;
    }

    /**
     * POST endpoint to add a new person.
     *
     * @param person the person to add
     * @return a Response with status CREATED if added successfully
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPerson(Person person) {
        if (person == null || person.name == null || person.name.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Person name is required")
                           .build();
        }
        if (peopleStore.containsKey(person.name)) {
            return Response.status(Response.Status.CONFLICT)
                           .entity("Person with name '" + person.name + "' already exists")
                           .build();
        }
        peopleStore.put(person.name, person);
        return Response.status(Response.Status.CREATED)
                       .entity(person)
                       .build();
    }

    /**
     * PUT endpoint to update an existing person's details.
     *
     * @param person the person data to update
     * @return a Response with status OK if updated successfully
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePerson(Person person) {
        if (person == null || person.name == null || person.name.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Person name is required for update")
                           .build();
        }
        if (!peopleStore.containsKey(person.name)) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Person with name '" + person.name + "' not found")
                           .build();
        }
        peopleStore.put(person.name, person);
        return Response.ok(person).build();
    }
}

/**
 * Represents the response from the Star Wars API for people.
 */
class PeopleResponse {
    public int count;
    public String next;
    public String previous;
    public List<Person> results;
}

/**
 * Represents a Person entity from the Star Wars API.
 */
class Person {
    public String name;
    public String height;
    public String mass;
    public String hair_color;
    public String skin_color;
    public String eye_color;
    public String birth_year;
    public String gender;

    // Default constructor required for JSON-B
    public Person() {}

    public Person(String name, String height, String mass, String hair_color, String skin_color, String eye_color, String birth_year, String gender) {
        this.name = name;
        this.height = height;
        this.mass = mass;
        this.hair_color = hair_color;
        this.skin_color = skin_color;
        this.eye_color = eye_color;
        this.birth_year = birth_year;
        this.gender = gender;
    }
}
