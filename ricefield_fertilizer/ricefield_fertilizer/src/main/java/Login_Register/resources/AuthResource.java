package Login_Register.resources;

import Login_Register.model.RegisterRequest;
import Login_Register.model.User;
import Login_Register.model.LoginRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;


import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import java.io.File;
import java.nio.file.Paths;
import java.util.regex.Pattern;


@Path("/auth")
@Tag(name = "LoginRegister", description = "LoginRegister REST APIs")
public class AuthResource {
    @ConfigProperty(name = "file.upload.dir")
    String uploadDir;

    private static final Logger LOG = Logger.getLogger(AuthResource.class);


    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$"
    );

    private boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    @POST
    @Path("/register")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(RegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"Passwords do not match\"}").build();
        }
        if (!isValidEmail(registerRequest.getEmail())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"Invalid email format\"}").build();
        }
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(registerRequest.getPassword());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setRole(registerRequest.getRole());
        newUser.persist();

        String userDirectory = Paths.get(uploadDir, registerRequest.getUsername()).toString();
        File directory = new File(userDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return Response.status(Response.Status.CREATED).entity("{\"message\":\"User registered successfully\"}").build();
    }

    @POST
    @Transactional
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest loginRequest) {
        AuthService authService = new AuthService();
        if (authService.isValidLogin(loginRequest)) {
            System.out.println("Login success");
            /* fetch user from database using request information */
            User user = authService.getUser(loginRequest);
            String token = user.generate(user);
            System.out.println("DEBUG #######: " + token);
            return Response.ok().entity("{\"token\":\"" + token + "\"}").build();
        } else {
            // Return a JSON response with an error message
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"Fail to login\"}").build();
        }
    }
}
