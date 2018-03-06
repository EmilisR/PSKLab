package lt.vu.rest;

import lt.vu.entities.Student;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/student")
@Produces(MediaType.APPLICATION_JSON)
public class StudentRestService {

    @Inject private EntityManager em;

    @GET
    @Path("/{studentId}")
    public Student find(@PathParam("studentId") Integer studentId) {
        return em.find(Student.class, studentId);
    }
}
