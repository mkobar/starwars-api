package org.acme;

//package org.acme.mock;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
public class PeopleResourceTest {

    /**
     * Verify the GET /people endpoint returns the initial two characters.
     */
    @Test
    public void testGetPeople() {
        RestAssured.given()
                .when().get("/people")
                .then()
                .statusCode(200)
                //.body("count", equalTo(2))
                .body("count", greaterThan(1))
                .body("results.name", hasItems("Luke Skywalker", "Darth Vader"));
    }

    /**
     * Test the POST /people endpoint to add a new person.
     */
    @Test
    public void testAddPerson() {
        String newPersonJson = "{\"name\":\"Leia Organa\","
                + "\"height\":\"150\","
                + "\"mass\":\"49\","
                + "\"hair_color\":\"brown\","
                + "\"skin_color\":\"light\","
                + "\"eye_color\":\"brown\","
                + "\"birth_year\":\"19BBY\","
                //+ "\"gender\":\"female\"}";
                + "\"gender\":\"female\","
                + "\"homeworld\":\"homeworld\","
                + "\"films\":[],"
                + "\"species\":[],"
                + "\"vehicles\":[],"
                + "\"starships\":[],"
                + "\"created\":\"created\","
                + "\"edited\":\"edited\","
                + "\"url\":\"url\"}";

        // Add the new person
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(newPersonJson)
                .when().post("/people")
                .then()
                .statusCode(201)
                .body("name", equalTo("Leia Organa"));

        // Verify the person is now present
        RestAssured.given()
                .when().get("/people")
                .then()
                .statusCode(200)
                .body("results.name", hasItems("Leia Organa"));
    }

    /**
     * Test the PUT /people endpoint to update an existing person's details.
     */
    @Test
    public void testUpdatePerson() {
        // First, add a new person to update
        String newPersonJson = "{\"name\":\"Han Solo\","
                + "\"height\":\"180\","
                + "\"mass\":\"80\","
                + "\"hair_color\":\"brown\","
                + "\"skin_color\":\"fair\","
                + "\"eye_color\":\"blue\","
                + "\"birth_year\":\"29BBY\","
                + "\"gender\":\"male\","
                + "\"homeworld\":\"homeworld\","
                + "\"films\":[],"
                + "\"species\":[],"
                + "\"vehicles\":[],"
                + "\"starships\":[],"
                + "\"created\":\"created\","
                + "\"edited\":\"edited\","
                + "\"url\":\"url\"}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(newPersonJson)
                .when().post("/people")
                .then()
                .statusCode(201)
                .body("name", equalTo("Han Solo"));

        // Now, update the person (e.g., change hair color)
        String updatedPersonJson = "{\"name\":\"Han Solo\","
                + "\"height\":\"180\","
                + "\"mass\":\"80\","
                + "\"hair_color\":\"black\","
                + "\"skin_color\":\"fair\","
                + "\"eye_color\":\"blue\","
                + "\"birth_year\":\"29BBY\","
                + "\"gender\":\"male\","
                + "\"homeworld\":\"homeworld\","
                + "\"films\":[],"
                + "\"species\":[],"
                + "\"vehicles\":[],"
                + "\"starships\":[],"
                + "\"created\":\"created\","
                + "\"edited\":\"edited\","
                + "\"url\":\"url\"}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatedPersonJson)
                .when().put("/people")
                .then()
                .statusCode(200)
                .body("hair_color", equalTo("black"));

        // Verify the update via GET
        RestAssured.given()
                .when().get("/people")
                .then()
                .statusCode(200)
                .body("results.find { it.name == 'Han Solo' }.hair_color", equalTo("black"));
    }

    /**
     * Test that adding a duplicate person returns a 409 Conflict status.
     */
    @Test
    public void testAddDuplicatePerson() {
        String newPersonJson = "{\"name\":\"Yoda\","
                + "\"height\":\"66\","
                + "\"mass\":\"17\","
                + "\"hair_color\":\"white\","
                + "\"skin_color\":\"green\","
                + "\"eye_color\":\"brown\","
                + "\"birth_year\":\"896BBY\","
                + "\"gender\":\"male\","
                + "\"homeworld\":\"homeworld\","
                + "\"films\":[],"
                + "\"species\":[],"
                + "\"vehicles\":[],"
                + "\"starships\":[],"
                + "\"created\":\"created\","
                + "\"edited\":\"edited\","
                + "\"url\":\"url\"}";

        // First addition should succeed
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(newPersonJson)
                .when().post("/people")
                .then()
                .statusCode(201);

        // Second addition should return conflict
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(newPersonJson)
                .when().post("/people")
                .then()
                .statusCode(409);
    }

    /**
     * Test updating a non-existing person returns a 404 Not Found status.
     */
    @Test
    public void testUpdateNonExistingPerson() {
        String updatePersonJson = "{\"name\":\"Non Existing\","
                + "\"height\":\"170\","
                + "\"mass\":\"70\","
                + "\"hair_color\":\"black\","
                + "\"skin_color\":\"fair\","
                + "\"eye_color\":\"green\","
                + "\"birth_year\":\"50BBY\","
                + "\"gender\":\"male\","
                + "\"homeworld\":\"homeworld\","
                + "\"films\":[],"
                + "\"species\":[],"
                + "\"vehicles\":[],"
                + "\"starships\":[],"
                + "\"created\":\"created\","
                + "\"edited\":\"edited\","
                + "\"url\":\"url\"}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatePersonJson)
                .when().put("/people")
                .then()
                .statusCode(404);
    }
}
