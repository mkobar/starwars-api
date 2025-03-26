package org.acme;

import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class StarWarsClientExample {

    public static void main(String[] args) throws Exception {
        // Create a REST client instance pointed at the test server.
        StarWarsService starWarsService = RestClientBuilder.newBuilder()
                .baseUri(new URI("http://localhost:8080"))
                .build(StarWarsService.class);

        // GET call: Retrieve all people.
        PeopleResponse response = starWarsService.getPeople();
        System.out.println("Initial People Count: " + response.count);
        if (response.results != null) {
            response.results.forEach(p -> System.out.println(" - " + p.name));
        }

        // POST call: Add a new person (e.g., Leia Organa).
        List<String> emptyList = new ArrayList<>();
        Person newPerson = new Person("Leia Organa", "150", "49", "brown", "light", "brown", "19BBY", "female",
                "homeworld", emptyList, emptyList, emptyList, emptyList, "created", "edited", "url");
        try (Response postResponse = starWarsService.addPerson(newPerson)) {
            if (postResponse.getStatus() == Response.Status.CREATED.getStatusCode()) {
                System.out.println("Successfully added: " + newPerson.name);
            } else {
                System.out.println("Failed to add person. Status: " + postResponse.getStatus());
            }
        }

        // GET call after POST to display updated list.
        response = starWarsService.getPeople();
        System.out.println("People Count After POST: " + response.count);
        if (response.results != null) {
            response.results.forEach(p -> System.out.println(" - " + p.name));
        }

        // PUT call: Update the person's details (e.g., change Leia's hair color).
        newPerson.hair_color = "black";
        try (Response putResponse = starWarsService.updatePerson(newPerson)) {
            if (putResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                System.out.println("Successfully updated: " + newPerson.name);
            } else {
                System.out.println("Failed to update person. Status: " + putResponse.getStatus());
            }
        }

        // GET call after PUT to display updated details.
        response = starWarsService.getPeople();
        System.out.println("People Count After PUT: " + response.count);
        if (response.results != null) {
            response.results.forEach(p -> System.out.println(" - " + p.name + " with hair color: " + p.hair_color));
        }
    }
}
