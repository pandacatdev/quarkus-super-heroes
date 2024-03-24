package io.quarkus.workshop.superheroes.hero;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
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

@Path("/api/heroes")
@Tag(name = "heroes")
public class HeroResource {

    Logger logger;

    // Constructor injection
    public HeroResource(Logger logger) {
        this.logger = logger;
    }

    @Operation(summary = "Returns a random hero")
    @GET
    @Path("/random")
    @APIResponse(responseCode = "200",
        content = @Content(mediaType = APPLICATION_JSON,
            schema = @Schema(implementation = Hero.class, required = true)))
    public Uni<RestResponse<Hero>> getRandomHero() {
        return Hero.findRandom()
            .onItem().ifNotNull().transform(h -> {
                logger.debugf("Found random hero: %s", h);
                return RestResponse.ok(h);
            })
            .onItem().ifNull().continueWith(() -> {
                logger.debug("No random villain found");
                return RestResponse.notFound();
            });
    }

    @Operation(summary = "Returns all the heroes from the database")
    @GET
    @APIResponse(responseCode = "200",
        content = @Content(mediaType = APPLICATION_JSON,
            schema = @Schema(implementation = Hero.class, type = SchemaType.ARRAY)))
    public Uni<List<Hero>> getAllHeroes() {
        return Hero.listAll();
    }

    @Operation(summary = "Returns a hero for a given identifier")
    @GET
    @Path("/{id}")
    @APIResponse(responseCode = "200",
        content = @Content(mediaType = APPLICATION_JSON,
            schema = @Schema(implementation = Hero.class)))
    @APIResponse(responseCode = "404", description = "The hero is not found for a given identifier")
    public Uni<RestResponse<Hero>> getHero(@RestPath Long id) {
        return Hero.<Hero>findById(id)
            .map(hero -> {
                if (hero != null) {
                    return RestResponse.ok(hero);
                }
                logger.debugf("No hero found with id %d", id);
                return RestResponse.notFound();
            });
    }

    @Operation(summary = "Creates a valid hero")
    @POST
    @APIResponse(responseCode = "201", description = "The URI of the created hero",
        content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
    @WithTransaction
    public Uni<RestResponse<URI>> createHero(@Valid Hero hero, @Context UriInfo uriInfo) {
        return hero.<Hero>persist()
            .map(h -> {
                UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(h.id));
                logger.debug("New Hero created with URI " + builder.build().toString());
                return RestResponse.created(builder.build());
            });

    }

    @Operation(summary = "Updates an exiting hero")
    @PUT
    @APIResponse(responseCode = "200", description = "The updated hero",
        content = @Content(mediaType = APPLICATION_JSON,
            schema = @Schema(implementation = Hero.class)))
    @WithTransaction
    public Uni<Hero> updateHero(@Valid Hero hero) {
        return Hero.<Hero>findById(hero.id)
            .map(retrieved -> {
                retrieved.name = hero.name;
                retrieved.otherName = hero.otherName;
                retrieved.level = hero.level;
                retrieved.picture = hero.picture;
                retrieved.powers = hero.powers;
                return retrieved;
            })
            .map(h -> {
                logger.debugf("Hero updated with new value %s", h);
                return h;
            });
    }

    @Operation(summary = "Deletes an exiting hero")
    @DELETE
    @APIResponse(responseCode = "204")
    @Path("/{id}")
    @WithTransaction
    public Uni<RestResponse<Void>> deleteHero(@RestPath Long id) {
        return Hero.deleteById(id)
            .invoke(() -> logger.debugf("Hero deleted with id %d", id))
            .replaceWith(RestResponse.noContent());
    }

    @GET()
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello Hero Resource";
    }
}
