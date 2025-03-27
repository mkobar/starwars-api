package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * MicroProfile REST Client interface to access the Star Wars API.
 */
@RegisterRestClient
public interface StarWarsService {

    @GET
    @Path("/people")
    @Produces(MediaType.APPLICATION_JSON)
    PeopleResponse getPeople();
    //Response getPeople();

    @POST
    @Path("/person")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response addPerson(Person person);

    @PUT
    @Path("/person")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updatePerson(Person person);
}
