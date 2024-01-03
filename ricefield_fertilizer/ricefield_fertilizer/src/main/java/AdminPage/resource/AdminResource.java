package AdminPage.resource;

import Login_Register.User;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/auth")
@RolesAllowed("Admin")
public class AdminResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() {
        return User.listAll();
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
}
