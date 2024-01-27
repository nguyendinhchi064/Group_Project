package UserPage.resources;



import UserPage.model.DataEntity;
import UserPage.model.ModelDb;
import UserPage.model.ProjDb;
import io.smallrye.common.annotation.Blocking;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;



@Path("/user")
public class UserResource {
    @Context
    SecurityContext securityContext;



    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/AddModel")
    @Blocking
    public Response addModel(RoutingContext rc) {
        try {
            // Extract form fields
            String modelName = rc.request().getFormAttribute("modelName");
            String modelDescription = rc.request().getFormAttribute("modelDescription");

            // Handle the file upload
            List<FileUpload> fileUploads = rc.fileUploads();
            // You'll need to process these file uploads as needed

            ModelDb model = new ModelDb();
            model.setModelName(modelName);
            model.setModelDescription(modelDescription);
            model.persist();

            // Additional logic for handling file uploads

            return Response.status(Response.Status.CREATED).entity(model).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error adding model").build();
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"User","Admin"})
    @Path("/FindModelBy{ModelId}")
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
    @RolesAllowed("User")
    @Path("/UpdateModelBy{ModelID}")
    public Response updateModel(@PathParam("ModelID") int ModelID, ModelDb updatedModel) {
        ModelDb updated = ModelDb.updateModel(ModelID, updatedModel);
        if (updated != null) {
            return Response.ok(updated).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Model not found").build();
        }
    }
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    @Path("/DeleteModelBy{ModelId}")
    public Response deleteModel(@PathParam("ModelId") int modelId) {
        ModelDb.findById(modelId); // Check if the project exists before deleting (optional)
        ModelDb.deleteModel(modelId);
        return Response.noContent().build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    @Path("/DeleteDataBy{DataId}")
    public Response deleteData(@PathParam("DataId") int dataId) {
        DataEntity.findById(dataId); // Check if the project exists before deleting (optional)
        DataEntity.deleteModel(dataId);
        return Response.noContent().build();
    }
    @Transactional
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    @Path("/AddData")
    public Response AddData(DataEntity dataEntity) {
        try {
            dataEntity.persist();
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
