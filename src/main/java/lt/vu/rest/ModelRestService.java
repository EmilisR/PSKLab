package lt.vu.rest;

import lt.vu.entities.Model;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/model")
@Produces(MediaType.APPLICATION_JSON)
public class ModelRestService {

    @Inject private EntityManager em;

    @GET
    @Path("/{modelId}")
    public Model find(@PathParam("modelId") Integer modelId) {
        return em.find(Model.class, modelId);
    }
}
