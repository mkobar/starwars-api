package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
public interface StarWarsService {

    @GET
    @Path("/people")
    @Produces(MediaType.APPLICATION_JSON)
    PeopleResponse getPeople();

    @POST
    @Path("/people")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response addPerson(Person person);

    @PUT
    @Path("/people")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updatePerson(Person person);
}
