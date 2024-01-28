package UserPage.resources.UserPage;

import UserPage.model.ProjDb;
import UserPage.model.ProjDbDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/Project")
public class ProjectResource {

    @POST
    @Transactional
    @Path("/Add")
    @RolesAllowed("User")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProject(ProjDbDTO projDbDTO) {
        ProjDb projDb = new ProjDb();
        projDb.setProjname(projDbDTO.getProjname());
        projDb.setDescription(projDbDTO.getDescription());
        projDb.persist();
        return Response.status(Response.Status.CREATED).entity(projDb).build();
    }

    @GET
    @Path("/all_ProjectInfo")
    @RolesAllowed({"User","Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjDb> getAllProjects() {
        return ProjDb.listAll();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"User","Admin"})
    @Path("/FindProjectBy{projId}")
    public Response getProject(@PathParam("projId") int projId) {
        ProjDb project = ProjDb.findById(projId);
        if (project != null) {
            return Response.ok(project).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }
    }
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    @Path("/UpdateProjectBy{projId}")
    public Response updateProject(@PathParam("projId") int projId, ProjDb updatedProject) {
        ProjDb updated = ProjDb.updateProject(projId, updatedProject);
        if (updated != null) {
            return Response.ok(updated).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }
    }
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"User","Admin"})
    @Path("/DeleteProjectBy{projId}")
    public Response deleteProject(@PathParam("projId") int projId) {
        ProjDb.findById(projId); // Check if the project exists before deleting (optional)
        ProjDb.deleteProject(projId);
        return Response.noContent().build();
    }
}
