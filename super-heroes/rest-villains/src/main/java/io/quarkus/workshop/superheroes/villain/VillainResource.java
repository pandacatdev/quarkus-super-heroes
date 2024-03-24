package io.quarkus.workshop.superheroes.villain;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/api/villains")
@Tag(name = "Villains")
public class VillainResource {
    Logger logger;

    VillainService villainService;

    // Constructor injection
    public VillainResource(Logger logger, VillainService villainService) {
        this.logger = logger;
        this.villainService = villainService;
    }

    @Operation(summary = "Returns a random villain")
    @GET
    @Path("/random")
    @APIResponse(
        responseCode = "200",
        content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class, required = true))
    )
    public RestResponse<Villain> getRandomVillain() {
        Villain villain = villainService.findRandomVillain();
        logger.debug("Found random villain: " + villain);
        return RestResponse.ok(villain);
    }

    @Operation(summary = "Returns all the villains from the database")
    @GET
    @APIResponse(
        responseCode = "200",
        content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class, type = SchemaType.ARRAY))
    )
    public RestResponse<List<Villain>> getAllVillains() {
        List<Villain> villains = villainService.findAllVillains();
        logger.debug("Total number of villains: " + villains.size());
        return RestResponse.ok(villains);
    }

    @Operation(summary = "Returns a villain for a given identifier")
    @GET
    @Path("/{id}")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class)))
    @APIResponse(responseCode = "404", description = "The villain is not found for a given identifier")
    public RestResponse<Villain> getVillain(@RestPath Long id) {
        Villain villain = villainService.findVillainById(id);
        if (villain != null) {
            logger.debug("Found 1 villain: " + villain);
            return RestResponse.ok(villain);
        } else {
            logger.debug("No villain found with id " + id);
            return RestResponse.notFound();
        }
    }

    @Operation(summary = "Creates a valid villain")
    @POST
    @APIResponse(
        responseCode = "201",
        description = "The URI of the created villain",
        content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = URI.class))
    )
    public RestResponse<Void> createVillain(@Valid Villain villain, @Context UriInfo uriInfo) {
        villain = villainService.persistVillain(villain);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Long.toString(villain.id));
        logger.debug("New villain created with URI " + uriBuilder.build().toString());
        return RestResponse.created(uriBuilder.build());
    }

    @Operation(summary = "Updates an exiting  villain")
    @PUT
    @APIResponse(
        responseCode = "200",
        description = "The updated villain",
        content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class))
    )
    public RestResponse<Villain> updateVillain(@Valid Villain villain) {
        villain = villainService.updateVillain(villain);
        logger.debug("Villain updated with new valued " + villain);
        return RestResponse.ok(villain);
    }

    @Operation(summary = "Deletes an exiting villain")
    @DELETE
    @Path("/{id}")
    @APIResponse(responseCode = "204")
    public RestResponse<Void> deleteVillain(@RestPath Long id) {
        villainService.deleteVillain(id);
        logger.debug("Villain deleted with id " + id);
        return RestResponse.noContent();
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello Villain Resource";
    }
}
