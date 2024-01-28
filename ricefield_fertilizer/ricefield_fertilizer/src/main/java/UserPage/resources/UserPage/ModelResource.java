package UserPage.resources.UserPage;


import UserPage.model.FileUploadForm;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;


import java.io.*;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.logging.Level;


@Path("/Model")
public class ModelResource {
    private static final Logger LOGGER = Logger.getLogger(ModelResource.class.getName());

    @ConfigProperty(name = "file.upload.dir")
    String uploadDir;

    @Inject
    SecurityContext securityContext;

    @POST
    @RolesAllowed("User")
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@MultipartForm FileUploadForm form) {
        String username = securityContext.getUserPrincipal().getName();
        try {
            LOGGER.info("Uploading file for user: " + username);

            String userUploadDir = Paths.get(uploadDir, username, "upload").toString();
            File userDir = new File(userUploadDir);
            if (!userDir.exists()) {
                boolean created = userDir.mkdirs();
                if (!created) {
                    LOGGER.warning("Failed to create directory: " + userUploadDir);
                }
            }
            String filename = Paths.get(userUploadDir, form.getFileName()).toString();
            try (InputStream inputStream = form.getFileData();
                 OutputStream outputStream = new FileOutputStream(filename)) {
                inputStream.transferTo(outputStream);
            }
            LOGGER.info("File uploaded successfully for user: " + username);
            return Response.ok("File uploaded successfully").build();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving file for user: " + username, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error saving file").build();
        } catch (NullPointerException e) {
            LOGGER.log(Level.SEVERE, "Security context or principal is null", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Authentication error").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error occurred", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal server error").build();
        }
    }

    @POST
    @RolesAllowed("User")
    @Path("/predict")
    public Response predict(String fileName) {
        String username = securityContext.getUserPrincipal().getName();
        try {
            String inputPath = Paths.get(uploadDir, username, "upload", fileName).toString();
            String resultDir = Paths.get(uploadDir, username, "result").toString();
            String pngDir = Paths.get(uploadDir, username, "png").toString();

            // Ensure directories exist
            new File(resultDir).mkdirs();
            new File(pngDir).mkdirs();

            runPredictionCommand(inputPath, resultDir, pngDir);

            return Response.ok("Prediction started").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @RolesAllowed("User")
    @Path("/png/{fileName}")
    @Produces("image/png")
    public Response getPngFile(@PathParam("fileName") String fileName) {
        String username = securityContext.getUserPrincipal().getName();
        try {
            String pngFilePath = Paths.get(uploadDir, username, "png", fileName).toString();
            File file = new File(pngFilePath);

            if (!file.exists()) {
                return Response.status(Response.Status.NOT_FOUND).entity("File not found").build();
            }

            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            byte[] imageData = outputStream.toByteArray();
            return Response.ok(imageData).build();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading PNG file: " + fileName, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving file").build();
        }
    }



    private void runPredictionCommand(String inputPath, String outputPath, String pngOutputPath) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("python", "Predict.py", inputPath, outputPath, pngOutputPath);

        // Redirect the error stream and output stream of the process to the current Java process
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Prediction script exited with error code: " + exitCode);
        }
    }
}

