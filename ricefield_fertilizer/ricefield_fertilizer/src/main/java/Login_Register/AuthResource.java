package Login_Register;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


import io.smallrye.jwt.build.Jwt;

import static Login_Register.User.findByNamePass;

@Path("/auth")
public class AuthResource {

    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register")
    public String register(User user) {
        User.add(user.username,user.password,user.role, user.email);
        return "Register success";
    }

    @POST
    @PermitAll
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String login(LoginRequest loginRequest){
        if(isValidLogin(loginRequest)) {
            System.out.println("Login success");
            String token = generate(loginRequest.username);
            System.out.println("DEBUG #######: " + token);
            return token;
        }
        else
        {
            throw new WebApplicationException("fail to login");
        }
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }

    @GET
    @Path("/{userId}")
    @RolesAllowed("Admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("userId") Long userId) {
        // Retrieve user information by userId
        User user = User.findById(userId);

        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
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
    @Transactional
    @DELETE
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("userId") Long userId) {
        // Retrieve the existing user from the database
        User existingUser = User.findById(userId);

        if (existingUser != null) {
            // Store details of the user before deletion
            String deletedUsername = existingUser.getUsername();
            String deletedEmail = existingUser.getEmail();
            String deletedRole = existingUser.getRole();

            // Delete the user from the database
            existingUser.delete();

            // Create a response with details about the deleted user
            String responseMessage = String.format(
                    "User with ID %d deleted successfully. Details: Username=%s, Email=%s, Role=%s",
                    userId, deletedUsername, deletedEmail, deletedRole);

            return Response.ok(responseMessage).build();
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
    private boolean isValidLogin(LoginRequest loginRequest) {
        List<User> userList = findByNamePass(loginRequest.username, loginRequest.password);
        return !userList.isEmpty();
    }
}
