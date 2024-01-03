package UserPage;


import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class UserResource {
    @Transactional
    @POST
    @Path("/AddProJect")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response AddProject(ProjDb projDb) {
        projDb.persist();
        return Response.ok(null).build();
    }
    @GET
    @Path("/{projId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getProject(@PathParam("projId") int projId) {
        ProjDb project = ProjDb.findById(projId);
        if (project != null) {
            return Response.ok(project).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }
    }
    @PUT
    @Path("/{projId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProject(@PathParam("projId") int projId, ProjDb updatedProject) {
        ProjDb updated = ProjDb.updateProject(projId, updatedProject);
        if (updated != null) {
            return Response.ok(updated).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }
    }
    @DELETE
    @Path("/{projId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteProject(@PathParam("projId") int projId) {
        ProjDb.findById(projId); // Check if the project exists before deleting (optional)
        ProjDb.deleteProject(projId);
        return Response.noContent().build();
    }
}
