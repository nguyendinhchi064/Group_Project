package Login_Register.resources;

import Login_Register.model.RegisterRequest;
import Login_Register.model.User;
import Login_Register.model.LoginRequest;
import jakarta.annotation.security.PermitAll;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;


import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;



@Path("/auth")
@Tag(name = "LoginRegister", description = "LoginRegister REST APIs")
public class AuthResource {

    @POST
    @Path("/register")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response registerUser(RegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Passwords do not match").build();
        }
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(registerRequest.getPassword());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setRole(registerRequest.getRole());
        newUser.persist();

        return Response.status(Response.Status.CREATED).entity("User registered successfully").build();
    }

    @POST
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public String login(LoginRequest loginRequest) {
        AuthService authService = new AuthService();
        if (authService.isValidLogin(loginRequest)) {
            System.out.println("Login success");
            /* fetch user from database using request information */
            User user = authService.getUser(loginRequest);
            String token = user.generate(user);
            System.out.println("DEBUG #######: " + token);
            return token;
        } else {
            //throw new WebApplicationException("fail to login");
            return "Fail";
        }
    }


}