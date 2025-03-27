package org.acme;

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

    //private final Map<String, Person> personMap = new HashMap<>();
    public Map<String, Person> personMap = new HashMap<>();

    @GET
    @Path("/people")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPeople(@QueryParam("name") String name) {
        try {
            PeopleResponse remotePeople = starWarsService.getPeople();
            if (remotePeople != null && remotePeople.results != null) {
                List<Person> combined = remotePeople.results.stream()
                        .map(p -> personMap.getOrDefault(p.name, p))
                        .collect(Collectors.toList());

                if (name != null && !name.isEmpty()) {
                    combined = combined.stream()
                            .filter(p -> p.name.toLowerCase().contains(name.toLowerCase()))
                            .collect(Collectors.toList());
                }

                PeopleResponse response = new PeopleResponse();
                response.count = combined.size();
                response.results = combined;
                return Response.ok(response).build();
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

        personMap.put(person.name, person);
        LOGGER.info("Created person: " + person.name);
        return Response.status(Response.Status.CREATED).entity(person).build();
    }

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
        if(!personMap.containsKey(person.name)){
            return Response.status(Response.Status.NOT_FOUND).entity("Person not found").build();
        }

        personMap.put(person.name, person);
        LOGGER.info("Updated person: " + person.name);
        return Response.ok(person).build();
    }
}

//@RegisterRestClient(baseUri = "https://swapi.dev/api")
//interface StarWarsService {
//    @GET
//    @Path("/people")
//    @Produces(MediaType.APPLICATION_JSON)
//    PeopleResponse getPeople();
//}

//class PeopleResponse {
//    public int count;
//    public String next;
//    public String previous;
//    public List<Person> results;
//}
//// Default constructor required for JSON-B
//public void Person() {}
//
//public void Person(String name, String height, String mass, String hair_color, String skin_color,
//                   String eye_color, String birth_year, String gender)
//{
//    this.name = name;
//    this.height = height;
//    this.mass = mass;
//    this.hair_color = hair_color;
//    this.skin_color = skin_color;
//    this.eye_color = eye_color;
//    this.birth_year = birth_year;
//    this.gender = gender;
//}