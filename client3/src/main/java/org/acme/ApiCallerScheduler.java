package org.acme;

import io.quarkus.runtime.Quarkus;
import jakarta.validation.OverridesAttribute;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.scheduler.Scheduled;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class ApiCallerScheduler {

    @Inject
    @RestClient
    PeopleClient peopleClient;

    @Inject
    Logger logger;

    @ConfigProperty(name = "use.live.swapi")
    String useLiveSwapi;

    // Run the API calls once on startup and exit
    @Scheduled(every = "5m", delayed = "0s") // Run immediately on startup
    void callApiOnceAndExit() {
        logger.info("Running API calls once on startup and exiting...");
        callApi();
        Quarkus.asyncExit(); // Exit Quarkus after the task is complete
    }

    private void callApi() {
        List<String> emptyList = Collections.emptyList();

        try {
            // Example: Get all people
            PeopleResponse peopleResponse = peopleClient.getPeople();
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

            if (useLiveSwapi.equalsIgnoreCase("false")) {
                // Example: Add a new person
                Person newPerson = new Person("Scheduled Person", "180", "80", "brown",
                        "fair", "blue", "20BBY", "male", "homeworld",
                        emptyList, emptyList, emptyList, emptyList, "created", "edited", "url");
                Person addedPerson = peopleClient.addPerson(newPerson);
//                logger.info("Added Person: " + addedPerson);
                if (addedPerson != null ) {
                    logger.info("Added Person:");
                        logger.info("  Name: " + addedPerson.name);
                        logger.info("  Height: " + addedPerson.height);
                        logger.info("  Mass: " + addedPerson.mass);
                        logger.info("  Hair Color: " + addedPerson.hair_color);
                        logger.info("  Skin Color: " + addedPerson.skin_color);
                        logger.info("  Eye Color: " + addedPerson.eye_color);
                        logger.info("  Birth Year: " + addedPerson.birth_year);
                        logger.info("  Gender: " + addedPerson.gender);
                        logger.info("  ------------------");
                } else {
                    logger.info("No person added.");
                }

                // Example: Update a person
                Person updatePerson = new Person("Luke Skywalker", "185", "85", "brown",
                        "fair", "blue", "19BBY", "male", "homeworld",
                        emptyList, emptyList, emptyList, emptyList, "created", "edited", "url");
                Person updatedPerson = peopleClient.updatePerson(updatePerson);
//                logger.info("Updated Person: " + updatedPerson);
                if (updatedPerson != null ) {
                    logger.info("Updated Person:");
                    logger.info("  Name: " + updatedPerson.name);
                    logger.info("  Height: " + updatedPerson.height);
                    logger.info("  Mass: " + updatedPerson.mass);
                    logger.info("  Hair Color: " + updatedPerson.hair_color);
                    logger.info("  Skin Color: " + updatedPerson.skin_color);
                    logger.info("  Eye Color: " + updatedPerson.eye_color);
                    logger.info("  Birth Year: " + updatedPerson.birth_year);
                    logger.info("  Gender: " + updatedPerson.gender);
                    logger.info("  ------------------");
                } else {
                    logger.info("No person updated.");
                }
            }
        } catch (Exception e) {
            logger.error("Error calling API: " + e.getMessage(), e);
        }
    }
}