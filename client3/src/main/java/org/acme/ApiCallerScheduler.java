package org.acme;

import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class ApiCallerScheduler {

    @Inject
    @RestClient
    PeopleClient peopleClient;

    @Inject
    Logger logger;

    // Run the API calls once on startup
    @Scheduled(every = "5m", delayed = "0s") // Run immediately on startup
    void callApiOnce() {
        logger.info("Running API calls once on startup...");
        callApi();
    }

    private void callApi() {
        try {
            // Example: Get all people
            PeopleResponse peopleResponse = peopleClient.getPeople();
//            logger.info("People: " + peopleResponse.results);
            if (peopleResponse != null && peopleResponse.results != null) {
                logger.info("People:");
                for (Person person : peopleResponse.results) {
                    logger.info("  Name: " + person.name);
                    logger.info("  Height: " + person.height);
                    logger.info("  Mass: " + person.mass);
                    logger.info("  Hair Color: " + person.hair_color);
                    logger.info("  Skin Color: " + person.skin_color);
                    logger.info("  Eye Color: " + person.eye_color);
                    logger.info("  Birth Year: " + person.birth_year);
                    logger.info("  Gender: " + person.gender);
                    logger.info("  ------------------");
                }
            } else {
                logger.info("No people found.");
            }

            // Example: Add a new person
//            Person newPerson = new Person("Scheduled Person", "180", "80", "brown", "fair", "blue", "20BBY", "male");
//            Person addedPerson = peopleClient.addPerson(newPerson);
//            logger.info("Added Person: " + addedPerson);

            // Example: Update a person
//            Person updatePerson = new Person("Luke Skywalker", "185", "85", "brown", "fair", "blue", "19BBY", "male");
//            Person updatedPerson = peopleClient.updatePerson(updatePerson);
//            logger.info("Updated Person: " + updatedPerson);

        } catch (Exception e) {
            logger.error("Error calling API: " + e.getMessage(), e);
        }
    }
}