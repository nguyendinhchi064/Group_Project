package Login_Register.resources;

import Login_Register.User;
import Login_Register.model.LoginRequest;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashSet;



import io.smallrye.jwt.build.Jwt;

@Path("/auth")
public class AuthResource {

    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/register")
    public String register(User user) {
        user.add();
        System.out.println(user.username + " " + user.password);
        return "Register success";
    }

    @POST
    @PermitAll
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String login(LoginRequest loginRequest) {
        UserService userService = new UserService();

        if (userService.isValidLogin(loginRequest)) {
            System.out.println("Login success");
            String token = generate(loginRequest.username);
            System.out.println("DEBUG #######: " + token);
            return token;
        } else {
            throw new WebApplicationException("fail to login");
        }
    }

    @Transactional
    @PUT
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("Admin")
    public Response updateUser(@PathParam("userId") Long userId, User updatedUser) {
        // Retrieve the existing user from the database
        User existingUser = User.findById(userId);

        if (!(existingUser == null)) {
            // Update the user information
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setRole(updatedUser.getRole());

            // Persist the updated user
            existingUser.persist();
            return Response.ok(existingUser).build();

        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }
    public String generate(String username) {
        String token =
                Jwt.upn(username)
                        .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                        .sign();
        System.out.println(token);
        return token;
    }

}