package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class StarWarsResourceClientTest {

    @Test
    public void testGetPeopleEndpoint() {
        given()
                .when().get("/people")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void testGetPeopleEndpointWithFilter() {
        given()
                .queryParam("name", "luke")
                .when().get("/people")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void testCreatePersonEndpoint() {
        String personJson = "{\"name\": \"Leia Organa\", \"height\": \"150\"}";

        given()
                .contentType(ContentType.JSON)
                .body(personJson)
                .when().post("/person")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("name", is("Leia Organa"))
                .body("height", is("150"));
    }

    @Test
    public void testCreatePersonEndpointBadRequest() {
        String personJson = "{\"height\": \"150\"}";

        given()
                .contentType(ContentType.JSON)
                .body(personJson)
                .when().post("/person")
                .then()
                .statusCode(400);
    }

    @Test
    public void testUpdatePersonEndpoint() {
        String personJson = "{\"name\": \"Luke Skywalker\", \"height\": \"172\"}";

        given()
                .contentType(ContentType.JSON)
                .body(personJson)
                .when().put("/person")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("name", is("Luke Skywalker"))
                .body("height", is("172"));
    }

    @Test
    public void testUpdatePersonEndpointBadRequest() {
        String personJson = "{\"height\": \"172\"}";

        given()
                .contentType(ContentType.JSON)
                .body(personJson)
                .when().put("/person")
                .then()
                .statusCode(400);
    }

    @Test
    public void testUpdatePersonEndpointNotFound() {
        String personJson = "{\"name\": \"Nonexistent Person\", \"height\": \"172\"}";

        given()
                .contentType(ContentType.JSON)
                .body(personJson)
                .when().put("/person")
                .then()
                .statusCode(404);
    }
}