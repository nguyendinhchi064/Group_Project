package Login_Register.resources;

import Login_Register.model.User;
import Login_Register.model.LoginRequest;
import jakarta.annotation.security.PermitAll;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.HashSet;



import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import static io.smallrye.jwt.build.Jwt.claim;


@Path("/auth")
@Tag(name = "LoginRegister", description = "LoginRegister REST APIs")
public class AuthResource {

    @POST
    @PermitAll
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/register")
    public String register(User user) {
        user.persist();
        System.out.println(user.username + " " + user.password);
        return "Register success";
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