//package UserPage.resources;
//
//
//
//import UserPage.model.DataEntity;
//import UserPage.model.ModelDb;
//import jakarta.annotation.security.RolesAllowed;
//import jakarta.transaction.Transactional;
//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.Response;
//
//
//
//@Path("/user")
//public class UserResource {
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed({"User","Admin"})
//    @Path("/FindModelBy{ModelId}")
//    public Response getModel(@PathParam("ModelId") int ModelID) {
//        ModelDb modelDb = ModelDb.findById(ModelID);
//        if (modelDb != null) {
//            return Response.ok(modelDb).build();
//        } else {
//            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
//        }
//    }
//    @PUT
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed("User")
//    @Path("/UpdateModelBy{ModelID}")
//    public Response updateModel(@PathParam("ModelID") int ModelID, ModelDb updatedModel) {
//        ModelDb updated = ModelDb.updateModel(ModelID, updatedModel);
//        if (updated != null) {
//            return Response.ok(updated).build();
//        } else {
//            return Response.status(Response.Status.NOT_FOUND).entity("Model not found").build();
//        }
//    }
//    @DELETE
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed("User")
//    @Path("/DeleteModelBy{ModelId}")
//    public Response deleteModel(@PathParam("ModelId") int modelId) {
//        ModelDb.findById(modelId); // Check if the project exists before deleting (optional)
//        ModelDb.deleteModel(modelId);
//        return Response.noContent().build();
//    }
//
//    @DELETE
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed("User")
//    @Path("/DeleteDataBy{DataId}")
//    public Response deleteData(@PathParam("DataId") int dataId) {
//        DataEntity.findById(dataId); // Check if the project exists before deleting (optional)
//        DataEntity.deleteModel(dataId);
//        return Response.noContent().build();
//    }
//    @Transactional
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed("User")
//    @Path("/AddData")
//    public Response AddData(DataEntity dataEntity) {
//        try {
//            dataEntity.persist();
//            return Response.ok().build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//}
