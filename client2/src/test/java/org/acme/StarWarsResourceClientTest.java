package org.acme;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class StarWarsResourceClientTest {

    @Test
    public void testGetPeopleEndpoint() {
        RestAssured.given()
                //.when().get("/starwars/people")
                .when().get("/people")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON); // Adjust assertions as needed
    }

    @Test
    public void testGetPeopleEndpointWithFilter() {
        RestAssured.given()
                .queryParam("name", "luke")
                //.when().get("/starwars/people")
                .when().get("/people")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON); // Adjust assertions as needed
    }

    @Test
    public void testUpdatePersonEndpoint() {
        String personJson = "{\"name\": \"Luke Skywalker\", \"height\": \"172\"}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(personJson)
                //.when().put("/starwars/person")
                .when().put("/person")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("name", is("Luke Skywalker")); //Example of how to verify the response body.
    }

    @Test
    public void testCreatePersonEndpoint() {
        String personJson = "{\"name\": \"Leia Organa\", \"height\": \"150\"}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(personJson)
                //.when().post("/starwars/person")
                .when().post("/person")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("name", is("Leia Organa"));
    }

    @Test
    public void testUpdatePersonBadRequest() {
        String personJson = "{\"height\": \"172\"}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(personJson)
                //.when().put("/starwars/person")
                .when().put("/person")
                .then()
                .statusCode(400);

    }

    @Test
    public void testCreatePersonBadRequest() {
        String personJson = "{\"height\": \"150\"}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(personJson)
                //.when().post("/starwars/person")
                .when().post("/person")
                .then()
                .statusCode(400);
    }
}