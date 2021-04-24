package com.inmemory;

import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.SimplyTimed;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Objects;

import static javax.ws.rs.core.Response.status;

@Path("/")
@RequestScoped
public class CacheResource {

    private final CacheProvider cacheProvider;

    @Inject
    public CacheResource(CacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    @Inject
    @Metric(name = "NO_OF_KEYS_IN_DB", absolute = true)
    private Counter cacheSize;

    @Path("get/{key}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(name = "GET_API_COUNT", absolute = true)
    @SimplyTimed(name = "GET_API_LATENCY", absolute = true, unit = MetricUnits.MILLISECONDS)
    public Response getMessage(@PathParam("key") String name) {
        String value = this.cacheProvider.get(name);
        if(Objects.nonNull(value)){
            return Response.ok(new Cache(name , value)).build();
        }

        return status(Response.Status.NOT_FOUND).entity("key not found").build();
    }


    @POST
    @Path("/set")
    @Consumes(MediaType.APPLICATION_JSON)
    @Counted(name = "SET_API_HIT_COUNT", absolute = true)
    @SimplyTimed(name = "SET_API_LATENCY", absolute = true, unit = MetricUnits.MILLISECONDS)
    public Response savePost(@Valid Cache cache) {
        this.cacheProvider.set(cache.getKey(),cache.getValue());
        cacheSize.inc();
        return  Response.ok(cache.getKey()).build();
    }

    @Path("/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(name = "SEARCH_API_HIT_COUNT", absolute = true)
    @SimplyTimed(name = "SEARCH_API_LATENCY", absolute = true, unit = MetricUnits.MILLISECONDS)
    public Response getMessage(@QueryParam("prefix") String prefix, @QueryParam("suffix") String suffix) {

        if(Objects.nonNull(prefix) && ! prefix.isEmpty()){
            return  Response.ok(this.cacheProvider.search(k -> k.startsWith(prefix))).build();
        } else if(Objects.nonNull(suffix) && ! suffix.isEmpty()) {
            return Response.ok(this.cacheProvider.search(k -> k.endsWith(suffix))).build();
        } else {
            return status(Response.Status.BAD_REQUEST).entity("Supports only Prefix and Suffix operations").build();
        }

    }

}
