package UserPage.resources.UserPage;



import UserPage.model.ModelDb;
import UserPage.model.ModelForm;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Path("/Model")
public class ModelResource {

    @POST
    @Path("/add")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addModel(@MultipartForm ModelForm modelForm) {
        try {
            // Create a new ModelDb object
            ModelDb modelDb = new ModelDb();
            modelDb.setModelName(modelForm.getModelName());
            modelDb.setModelDescription(modelForm.getModelDescription());

            // Process the file
            String filePath = saveFile(modelForm.getFile());

            // Here, instead of saving the model to the database, you can save the filePath
            // and other model information to a file or another storage mechanism as per your requirement

            return Response.status(Response.Status.CREATED).entity("Model added successfully with file stored at " + filePath).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity("Error processing the request").build();
        }
    }

    private String saveFile(InputStream fileContent) throws Exception {
        String fileName = "uniqueFileName"; // Generate a unique file name
        String directory = "C:\\Users\\ADMIN\\Downloads\\GP_PREDICT-20240128T162736Z-001\\GP_PREDICT"; // Directory to save the file
        java.nio.file.Path filePath = Paths.get(directory, fileName);

        Files.copy(fileContent, filePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toString();
    }
}

