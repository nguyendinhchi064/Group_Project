package Login_Register.resources;

import Login_Register.model.LoginRequest;
import Login_Register.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import org.wildfly.security.password.Password;
import org.wildfly.security.password.PasswordFactory;
import org.wildfly.security.password.WildFlyElytronPasswordProvider;
import org.wildfly.security.password.interfaces.BCryptPassword;
import org.wildfly.security.password.util.ModularCrypt;

import java.util.List;

@ApplicationScoped
public class AuthService {

    private static final Logger LOG = Logger.getLogger(AuthService.class);

    public boolean verifyPassword(String bCryptPasswordHash, String passwordToVerify) {
        try {
            WildFlyElytronPasswordProvider provider = new WildFlyElytronPasswordProvider();
            PasswordFactory passwordFactory = PasswordFactory.getInstance(BCryptPassword.ALGORITHM_BCRYPT, provider);
            Password userPasswordDecoded = ModularCrypt.decode(bCryptPasswordHash);
            Password userPasswordRestored = passwordFactory.translate(userPasswordDecoded);
            return passwordFactory.verify(userPasswordRestored, passwordToVerify.toCharArray());
        } catch (Exception e) {
            LOG.errorf("Password verification failed: %s", e.getMessage());
            return false;
        }
    }

    public boolean isValidLogin(LoginRequest loginRequest) {
        List<User> userList = User.list("username", loginRequest.getUsername());
        if (!userList.isEmpty()) {
            User user = userList.get(0);
            return verifyPassword(user.getPassword(), loginRequest.getPassword());
        } else {
            LOG.warnf("No user found with username: %s", loginRequest.getUsername());
            return false;
        }
    }

    public User getUser(LoginRequest loginRequest) {
        List<User> userList = User.list("username", loginRequest.getUsername());
        return userList.isEmpty() ? null : userList.get(0);
    }
}
