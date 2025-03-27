package org.acme;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
        import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
//import static wiremock.org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
@QuarkusTestResource(WireMockPeopleResource.class)
public class PeopleClientWireMockTest {

    private static WireMockServer wireMockServer;

//    @BeforeAll
//    public static void setup() {
//        wireMockServer = new WireMockServer(8082); // Match the baseUri in PeopleClient
//        wireMockServer.start();
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        wireMockServer.stop();
//    }

    @Test
    public void testGetPeopleWireMock() {
//        wireMockServer.stubFor(get(urlEqualTo("/people"))
//                .willReturn(aResponse()
//                        .withHeader("Content-Type", "application/json")
//                        .withStatus(200)
//                        .withBody("{\"count\": 2, \"results\": [{\"name\": \"Luke Skywalker\", \"height\": \"172\"}, {\"name\": \"Darth Vader\", \"height\": \"202\"}]}")));

//        given()
//                .when().get("/people")
//                .then()
//                .statusCode(200)
//                .contentType(ContentType.JSON)
//                .body("count", is(2))
//                .body("results[0].name", is("Luke Skywalker"));
        given()
                .when().get("/client/getPeople")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("count", is(2))
                .body("results[0].name", is("Luke Skywalker"));
//        assertThat(testClient.get("/people").statusCode(), is(200));
//        assertThat(testClient.get("/some/thing/else").statusCode(), is(404));
    }

    //@Disabled
    @Test
    public void testAddPersonWireMock() {
//        wireMockServer.stubFor(post(urlEqualTo("/people"))
//                //.withHeader("Content-Type", "application/json")
//                .withHeader("Content-Type", containing("json"))
//                .withRequestBody(containing("Leia Organa"))
//                .willReturn(aResponse()
//                        .withHeader("Content-Type", "application/json")
//                        .withStatus(201)
//                        .withBody("{\"name\": \"Leia Organa\", \"height\": \"150\"}")));

        String personJson = "{\"name\": \"Leia Organa\", \"height\": \"150\"}";

        given()
                .contentType(ContentType.JSON)
                .body(personJson)
                .when().post("/client/addPerson")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("name", is("Leia Organa"))
                .body("height", is("150"));
    }

    @Disabled
    @Test
    public void testAddPersonBadRequestWireMock() {
        wireMockServer.stubFor(post(urlEqualTo("/people"))
                .willReturn(aResponse()
                        .withStatus(400)));

        String personJson = "{\"height\": \"150\"}";

        given()
                .contentType(ContentType.JSON)
                .body(personJson)
                .when().post("/client/addPerson")
                .then()
                .statusCode(400);
    }

    @Test
    public void testAddPersonConflictWireMock() {
//        wireMockServer.stubFor(post(urlEqualTo("/people"))
//                .withRequestBody(equalToJson("{ \"name\":  \"Luke Skywalker\"  }", true, true))
//                .willReturn(aResponse()
//                        .withStatus(409)));

        String personJson = "{\"name\": \"Luke Skywalker\", \"height\": \"172\"}";

        given()
                .contentType(ContentType.JSON)
                .body(personJson)
                .when().post("/client/addPerson")
                .then()
                .statusCode(409);
    }

    //@Disabled
    @Test
    public void testUpdatePersonWireMock() {
//        wireMockServer.stubFor(put(urlEqualTo("/people"))
//                //.withHeader("Content-Type", "application/json")
//                .withHeader("Content-Type", containing("json"))
//                .withRequestBody(containing("Luke Skywalker"))
//                .willReturn(aResponse()
//                        .withHeader("Content-Type", "application/json")
//                        .withBody("{\"name\": \"Luke Skywalker\", \"height\": \"180\"}")));

        String personJson = "{\"name\": \"Luke Skywalker\", \"height\": \"180\"}";

        given()
                .contentType(ContentType.JSON)
                .body(personJson)
                .when().put("/client/updatePerson")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("name", is("Luke Skywalker"))
                .body("height", is("180"));
    }

    @Disabled
    @Test
    public void testUpdatePersonBadRequestWireMock() {
        wireMockServer.stubFor(put(urlEqualTo("/people"))
                .willReturn(aResponse()
                        .withStatus(400)));

        String personJson = "{\"height\": \"180\"}";

        given()
                .contentType(ContentType.JSON)
                .body(personJson)
                .when().put("/client/updatePerson")
                .then()
                .statusCode(400);
    }

    @Test
    public void testUpdatePersonNotFoundWireMock() {
//        wireMockServer.stubFor(put(urlEqualTo("/people"))
//                .withRequestBody(equalToJson("{ \"name\":  \"Nonexistent Person\"  }", true, true))
//                .willReturn(aResponse()
//                        .withStatus(404)));

        String personJson = "{\"name\": \"Nonexistent Person\", \"height\": \"180\"}";

        given()
                .contentType(ContentType.JSON)
                .body(personJson)
                .when().put("/client/updatePerson")
                .then()
                .statusCode(404);
    }
}