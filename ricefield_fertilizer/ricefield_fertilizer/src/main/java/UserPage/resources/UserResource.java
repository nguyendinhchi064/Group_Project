package UserPage.resources;


import UserPage.model.FileUploadForm;
import UserPage.model.ModelDb;
import UserPage.model.ProjDb;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.io.InputStream;


@Path("/user")
@RolesAllowed("User")
public class UserResource {

    @Transactional
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/AddProject")
    public Response AddProject(ProjDb projDb) {
        /*
        * get user_id;
        * User user = lookup(user_id);
        * projDb.setUser(user);
        * projDb.persist()
        * */
        try {
//            Long userId = projDb.getUser().getUserid();
//            User user = User.findById(userId);
//            projDb.setUser(user);
            projDb.persist();
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
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
    @Path("/DeleteProjectBy{projId}")
    public Response deleteProject(@PathParam("projId") int projId) {
        ProjDb.findById(projId); // Check if the project exists before deleting (optional)
        ProjDb.deleteProject(projId);
        return Response.noContent().build();
    }
    @POST
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/AddModel")
    public String addModel(@MultipartForm FileUploadForm fileForm, ModelDb modelDb) {
        // Set the file content in the ModelDb instance
        modelDb.setFileContent(fileForm.getFile());

        // Save the ModelDb instance to the database
        modelDb.persist();

        return "Model added successfully";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/FindProjectBy{ModelId}")
    public Response getModel(@PathParam("ModelId") int ModelID) {
        ModelDb modelDb = ModelDb.findById(ModelID);
        if (modelDb != null) {
            return Response.ok(modelDb).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }
    }
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/UpdateModelBy{ModelID}")
    public Response updateModel(@PathParam("ModelID") int ModelID, ModelDb updatedModel) {
        ModelDb updated = ModelDb.updateModel(ModelID, updatedModel);
        if (updated != null) {
            return Response.ok(updated).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }
    }
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/DeleteModelBy{ModelId}")
    public Response deleteModel(@PathParam("ModelId") int modelId) {
        ModelDb.findById(modelId); // Check if the project exists before deleting (optional)
        ModelDb.deleteModel(modelId);
        return Response.noContent().build();
    }
}
