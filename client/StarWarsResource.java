package org.acme;

//import com.thoughtworks.xstream.io.path.Path;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@Path("/starwars")
public class StarWarsResource {

    private static final Logger LOGGER = Logger.getLogger(StarWarsResource.class);

    @RestClient
    StarWarsService starWarsService;

    // In-memory store for updated person records (keyed by name)
    //private Map<String, Person> updatedPeople = new HashMap<>();
    // public for unit tests only
    public Map<String, Person> updatedPeople = new HashMap<>();

    /**
     * GET endpoint to retrieve people from the Star Wars API.
     * Optionally filters by a provided name query parameter.
     */
    @GET
    @Path("/people")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPeople(@QueryParam("name") String name) {
        try {
            PeopleResponse remotePeople = starWarsService.getPeople();
            if (remotePeople != null && remotePeople.results != null) {
                // Replace any remote person with our updated version if it exists
                List<Person> combined = remotePeople.results.stream()
                        .map(p -> updatedPeople.getOrDefault(p.name, p))
                        .collect(Collectors.toList());
                remotePeople.results = combined;
                // Apply filtering by name if the query parameter is provided
                if (name != null && !name.isEmpty()) {
                    remotePeople.results = remotePeople.results.stream()
                            .filter(p -> p.name.toLowerCase().contains(name.toLowerCase()))
                            .collect(Collectors.toList());
                }
                return Response.ok(remotePeople).build();
            } else {
                LOGGER.error("Received null or empty response from the Star Wars API.");
                return Response.status(Response.Status.BAD_GATEWAY)
                        .entity("Error retrieving data from the Star Wars API")
                        .build();
            }
        } catch (Exception e) {
            LOGGER.error("Exception occurred while fetching people from the Star Wars API", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing your request.")
                    .build();
        }
    }

    /**
     * PUT endpoint to update a person’s details.
     * This simulates an update by storing the provided person details in an in‑memory map.
     */
    @PUT
    @Path("/person")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePerson(Person person) {
        if (person == null || person.name == null || person.name.isEmpty()) {
            LOGGER.warn("Invalid update attempt: Person name is missing.");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Person name is required")
                    .build();
        }
        try {
            updatedPeople.put(person.name, person);
            LOGGER.info("Updated person: " + person.name);
            return Response.ok(person).build();
        } catch (Exception e) {
            LOGGER.error("Exception occurred while updating person", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while updating the person.")
                    .build();
        }
    }

    /**
     * POST endpoint to create a new person.
     * This simulates creation by adding the provided person details to an in-memory map.
     */
    @POST
    @Path("/person")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPerson(Person person) {
        if (person == null || person.name == null || person.name.isEmpty()) {
            LOGGER.warn("Invalid creation attempt: Person name is missing.");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Person name is required")
                    .build();
        }
        try {
            updatedPeople.put(person.name, person);
            LOGGER.info("Created person: " + person.name);
            return Response.status(Response.Status.CREATED).entity(person).build();
        } catch (Exception e) {
            LOGGER.error("Exception occurred while creating person", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while creating the person.")
                    .build();
        }
    }
}

/**
 * MicroProfile REST Client interface to access the Star Wars API.
 */
//@RegisterRestClient(baseUri = "https://swapi.dev/api")
//interface StarWarsService {
//    @GET
//    @Path("/people")
//    @Produces(MediaType.APPLICATION_JSON)
//    PeopleResponse getPeople();
//}

/**
 * Represents the response from the Star Wars API for people.
 */
//class PeopleResponse {
//    public int count;
//    public String next;
//    public String previous;
//    public List<Person> results;
//}

/**
 * Represents a Person entity from the Star Wars API.
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
//}