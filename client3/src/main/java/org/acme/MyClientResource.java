package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.inject.Inject;

@Path("/client")
public class MyClientResource {

    @Inject
    @RestClient
    PeopleClient peopleClient;

    @GET
    @Path("/getPeople")
    public PeopleResponse getPeopleFromClient() {
        return peopleClient.getPeople();
    }

    // Add other methods to use addPerson and updatePerson
    @POST
    @Path("/addPerson")
    public Response addPersonToClient(Person person) {
        try {
            Person addedPerson = peopleClient.addPerson(person);
            return Response.status(Response.Status.CREATED).entity(addedPerson).build();
        } catch (WebApplicationException e) {
            return Response.status(e.getResponse().getStatus()).entity(e.getResponse().getEntity()).build();
        }

    }

    @PUT
    @Path("/updatePerson")
    public Response updatePersonOnClient(Person person) {
        try {
            Person updatedPerson = peopleClient.updatePerson(person);
            return Response.ok(updatedPerson).build();
        } catch (WebApplicationException e) {
            return Response.status(e.getResponse().getStatus()).entity(e.getResponse().getEntity()).build();
        }
    }
}