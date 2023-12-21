package Login_Register;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashSet;

import io.smallrye.jwt.build.Jwt;

@Path("")
public class AuthResource {

    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register")
    public Response register(User user) {
        User.add(user.username,user.password,user.role, user.email);
        return Response.ok(null).build();
    }

    @POST
    @PermitAll
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String login(LoginRequest loginRequest){
//        if(isValidLogin(loginRequest.username,loginRequest.password)) {
//            System.out.println("Login success");
//            String token = generate(loginRequest.username);
//            System.out.println("DEBUG #######: " + token);
//            return token;
//        }
//        else
//        {
//            throw new WebApplicationException("fail to login");
//        }
        String token = generate(loginRequest.username);
        System.out.println("DEBUG #######: " + token);
        return token;
    }
    public static class LoginRequest {
        public String username;
        public String password;
    }

    @GET
    @RolesAllowed("Admin")
    @Path("/Admin")
    @Produces(MediaType.TEXT_PLAIN)
    public String Admin(){
        return "Hello,Admin";
    }
    @GET
    @RolesAllowed("User")
    @Path("/User")
    @Produces(MediaType.TEXT_PLAIN)
    public String User(){
        return "Hello,User";
    }
    @GET
    @Path("/{userId}")
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
    @PUT
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
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
    public boolean isValidLogin(String username, String password) {
        User user = User.findByName(username);

        // Check if the user exists and if the provided password matches the stored password
        return user != null && user.getPassword().equals(password);
    }
}
