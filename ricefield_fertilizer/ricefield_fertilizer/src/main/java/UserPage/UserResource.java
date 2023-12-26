package UserPage;

import Login_Register.User;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("")
public class UserResource {
    @POST
    @Path("/AddProJect")
    public Response AddProject(ProjDb projDb) {
        ProjDb.addProject(projDb.projname, projDb.description);
        return Response.ok(null).build();
    }
}
