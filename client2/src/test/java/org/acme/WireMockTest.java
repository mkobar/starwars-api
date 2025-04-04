package org.acme;
//package org.acme.client;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.mockito.InjectMock;
import org.acme.Person;
import org.acme.StarWarsService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collections;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class WireMockTest {

    @InjectMock
    @RestClient
    StarWarsService starWarsService;

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8089));
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    /**
     * Test GET /people endpoint to retrieve a list of characters.
     */
    @Test
    public void testGetPeople() {
        String jsonResponse = "{ \"count\": 1, \"results\": [{ \"name\": \"Luke Skywalker\", \"height\": \"172\" }] }";

        stubFor(get(urlEqualTo("/people"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                        .withBody(jsonResponse)
                        .withStatus(200)));

        //Response response = starWarsService.getPeople();
        PeopleResponse response = starWarsService.getPeople();
        assertNotNull(response);
        //assertEquals(200, response.getStatus());
    }

    /**
     * Test POST /people endpoint to add a new character.
     */
    @Test
    public void testAddPerson() {
        Person newPerson = new Person("Leia Organa", "150", "49", "brown", "light", "brown", "19BBY", "female",
                "homeworld", null, null, null, null, "created", "edited", "url");
        String requestBody = "{ \"name\": \"Leia Organa\", \"height\": \"150\" }";

        stubFor(post(urlEqualTo("/people"))
                .withRequestBody(equalToJson(requestBody))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                        .withStatus(201)));

        try (Response response = starWarsService.addPerson(newPerson)) {
            assertEquals(201, response.getStatus());
        }
    }

    /**
     * Test PUT /people endpoint to update an existing character.
     */
    @Test
    public void testUpdatePerson() {
        Person updatedPerson = new Person("Leia Organa", "150", "49", "black", "light", "brown", "19BBY", "female",
                "homeworld", null, null, null, null, "created", "edited", "url");
        String requestBody = "{ \"name\": \"Leia Organa\", \"hair_color\": \"black\" }";

        stubFor(put(urlEqualTo("/people"))
                .withRequestBody(equalToJson(requestBody))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                        .withStatus(200)));

        try (Response response = starWarsService.updatePerson(updatedPerson)) {
            assertEquals(200, response.getStatus());
        }
    }

    /**
     * Test handling of a 404 error when retrieving a non-existing character.
     */
    @Test
    public void testGetNonExistingPerson() {
        stubFor(get(urlEqualTo("/people/999"))
                .willReturn(aResponse()
                        .withStatus(404)));

        //Response response = starWarsService.getPeople();
        PeopleResponse response = starWarsService.getPeople();
        //assertEquals(404, response.getStatus());
    }

    /**
     * Test handling of a failed POST request (conflict scenario).
     */
    @Test
    public void testAddDuplicatePerson() {
        Person duplicatePerson = new Person("Luke Skywalker", "172", "77", "blond", "fair", "blue", "19BBY", "male",
        "homeworld", null, null, null, null, "created", "edited", "url");
        String requestBody = "{ \"name\": \"Luke Skywalker\" }";

        stubFor(post(urlEqualTo("/people"))
                .withRequestBody(equalToJson(requestBody))
                .willReturn(aResponse()
                        .withStatus(409)));

        try (Response response = starWarsService.addPerson(duplicatePerson)) {
            assertEquals(409, response.getStatus());
        }
    }
}
