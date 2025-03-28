package org.acme;

import org.acme.StarWarsService;
import org.acme.StarWarsResource;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@QuarkusTest
public class StarWarsResourceTest {

    @InjectMock
    StarWarsService starWarsService;

    StarWarsResource starWarsResource;

    @BeforeEach
    public void setup() {
        starWarsResource = new StarWarsResource();
        starWarsResource.starWarsService = starWarsService;
    }

    @Test
    public void testGetPeople_Success_NoFilter() {
        PeopleResponse mockResponse = new PeopleResponse();
        Person person1 = new Person();
        person1.name = "Luke Skywalker";
        Person person2 = new Person();
        person2.name = "Darth Vader";
        mockResponse.results = Arrays.asList(person1, person2);

        when(starWarsService.getPeople()).thenReturn(mockResponse);

        Response response = starWarsResource.getPeople(null);

        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
        PeopleResponse actualResponse = (PeopleResponse) response.getEntity();
        assertEquals(2, actualResponse.results.size());
        assertEquals("Luke Skywalker", actualResponse.results.get(0).name);
        assertEquals("Darth Vader", actualResponse.results.get(1).name);
    }

    @Test
    public void testGetPeople_Success_WithFilter() {
        PeopleResponse mockResponse = new PeopleResponse();
        Person person1 = new Person();
        person1.name = "Luke Skywalker";
        Person person2 = new Person();
        person2.name = "Darth Vader";
        mockResponse.results = Arrays.asList(person1, person2);

        when(starWarsService.getPeople()).thenReturn(mockResponse);

        Response response = starWarsResource.getPeople("luke");

        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
        PeopleResponse actualResponse = (PeopleResponse) response.getEntity();
        assertEquals(1, actualResponse.results.size());
        assertEquals("Luke Skywalker", actualResponse.results.get(0).name);
    }

    @Test
    public void testGetPeople_EmptyResults() {
        PeopleResponse mockResponse = new PeopleResponse();
        mockResponse.results = null;

        when(starWarsService.getPeople()).thenReturn(mockResponse);

        Response response = starWarsResource.getPeople(null);

        assertEquals(502, response.getStatus());
    }

    @Test
    public void testGetPeople_Exception() {
        when(starWarsService.getPeople()).thenThrow(new RuntimeException("Test Exception"));

        Response response = starWarsResource.getPeople(null);

        assertEquals(500, response.getStatus());
    }

    @Test
    public void testUpdatePerson_Success() {
        Person person = new Person();
        person.name = "Luke Skywalker";
        person.height = "172";

        Response response = starWarsResource.updatePerson(person);

        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
        Person actualPerson = (Person) response.getEntity();
        assertEquals("Luke Skywalker", actualPerson.name);
        assertEquals("172", actualPerson.height);
    }

    @Test
    public void testUpdatePerson_BadRequest_NullPerson() {
        Response response = starWarsResource.updatePerson(null);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testUpdatePerson_BadRequest_NullName() {
        Person person = new Person();
        person.height = "172";

        Response response = starWarsResource.updatePerson(person);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testUpdatePerson_Exception() {
        starWarsResource.updatedPeople = Mockito.mock(HashMap.class);
        Person person = new Person();
        person.name = "Luke Skywalker";
        when(starWarsResource.updatedPeople.put(Mockito.anyString(), Mockito.any(Person.class))).thenThrow(new RuntimeException("test"));

        Response response = starWarsResource.updatePerson(person);

        assertEquals(500, response.getStatus());
    }

    @Test
    public void testGetPeople_UsesUpdatedPeople(){
        Person person1 = new Person();
        person1.name = "Luke Skywalker";
        Person person2 = new Person();
        person2.name = "Darth Vader";

        Person updatedPerson = new Person();
        updatedPerson.name = "Luke Skywalker";
        updatedPerson.height = "180";

        starWarsResource.updatedPeople.put("Luke Skywalker", updatedPerson);

        PeopleResponse mockResponse = new PeopleResponse();
        mockResponse.results = Arrays.asList(person1, person2);

        when(starWarsService.getPeople()).thenReturn(mockResponse);

        Response response = starWarsResource.getPeople(null);
        PeopleResponse actualResponse = (PeopleResponse) response.getEntity();

        assertEquals("180", actualResponse.results.get(0).height);
    }
}