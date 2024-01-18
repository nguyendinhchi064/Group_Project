package UserPage.resources;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {
    @Inject
    SecurityIdentity securityIdentity;
    public Long getUserId() {
        // Assuming you store the user ID as a custom attribute in the SecurityIdentity
        return Long.parseLong(securityIdentity.getAttribute("userId"));
    }
}
