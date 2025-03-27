package org.acme;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockPeopleResource implements QuarkusTestResourceLifecycleManager {

    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(8082);
        wireMockServer.start();

        // GET
        wireMockServer.stubFor(get(urlEqualTo("/people"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{\"count\": 2, \"results\": [{\"name\": \"Luke Skywalker\", \"height\": \"172\"}, {\"name\": \"Darth Vader\", \"height\": \"202\"}]}")));

        // POST
        wireMockServer.stubFor(post(urlEqualTo("/people"))
                //.withHeader("Content-Type", "application/json")
                .withHeader("Content-Type", containing("json"))
                .withRequestBody(containing("Leia Organa"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(201)
                        .withBody("{\"name\": \"Leia Organa\", \"height\": \"150\"}")));

        // POST 400 bad request - missing name
        wireMockServer.stubFor(post(urlEqualTo("/people"))
                //.withRequestBody(matchingJsonPath("$.name"))
                //.withRequestBody(notContaining("name"))
                //.withRequestBody(notContaining("\"name\""))
                .withRequestBody(equalToJson("{ \"name\": null }", true, true))
                .willReturn(aResponse()
                        .withStatus(400)));

        // POST 409 conflict
        wireMockServer.stubFor(post(urlEqualTo("/people"))
                //.withRequestBody(equalToJson("{ \"name\":  \"Luke Skywalker\"  }", true, true))
                .withRequestBody(containing("Luke Skywalker"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(409)));

        // PUT
        wireMockServer.stubFor(put(urlEqualTo("/people"))
                //.withHeader("Content-Type", "application/json")
                .withHeader("Content-Type", containing("json"))
                .withRequestBody(containing("Luke Skywalker"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"name\": \"Luke Skywalker\", \"height\": \"180\"}")));

        // PUT 400 bad request - missing name
        wireMockServer.stubFor(put(urlEqualTo("/people"))
//                .withRequestBody(matchingJsonPath("$.name"))
                .withRequestBody(equalToJson("{ \"name\": null }", true, true))
                .willReturn(aResponse()
                        .withStatus(400)));

        // PUT 404 not found
        wireMockServer.stubFor(put(urlEqualTo("/people"))
                //.withRequestBody(equalToJson("{ \"name\":  \"Nonexistent Person\"  }", true, true))
                .withRequestBody(containing("Nonexistent Person"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(404)));

        return null;
    }

    @Override
    public void stop() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }
}