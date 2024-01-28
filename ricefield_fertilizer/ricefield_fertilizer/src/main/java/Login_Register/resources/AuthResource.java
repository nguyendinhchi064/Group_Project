package Login_Register.resources;

import Login_Register.model.RegisterRequest;
import Login_Register.model.User;
import Login_Register.model.LoginRequest;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;


import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import java.util.regex.Pattern;


@Path("/auth")
@Tag(name = "LoginRegister", description = "LoginRegister REST APIs")
public class AuthResource {

    private static final Logger LOG = Logger.getLogger(AuthResource.class);

    @Inject
    AuthService authService;

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

        return Response.status(Response.Status.CREATED).entity("{\"message\":\"User registered successfully\"}").build();
    }

    @POST
    @Transactional
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest loginRequest) {
        String username = null;
        try {
            username = loginRequest.getUsername();
            LOG.debugf("Login attempt for username: %s", username);

            if (!authService.isValidLogin(loginRequest)) {
                LOG.warnf("Login failed: Invalid credentials for username: %s", username);
                return Response.status(Response.Status.UNAUTHORIZED).entity("{\"message\":\"Fail to login\"}").build();
            }

            User user = authService.getUser(loginRequest);
            if (user == null) {
                LOG.warnf("Login failed: User not found for username: %s", username);
                return Response.status(Response.Status.UNAUTHORIZED).entity("{\"message\":\"Fail to login\"}").build();
            }

            String token = user.generate(user);
            LOG.infof("Login success for username: %s, token generated", username);

            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add("token", token)
                    .build();

            return Response.ok(jsonResponse).build();

        } catch (Exception e) {
            LOG.errorf("Error during login for username: %s", username, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"Internal server error\"}").build();
        }
    }
}
