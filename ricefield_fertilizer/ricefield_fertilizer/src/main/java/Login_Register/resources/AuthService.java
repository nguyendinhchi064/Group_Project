package Login_Register.resources;

import Login_Register.model.LoginRequest;
import Login_Register.model.User;
import io.smallrye.jwt.build.Jwt;
import org.wildfly.security.password.Password;
import org.wildfly.security.password.PasswordFactory;
import org.wildfly.security.password.WildFlyElytronPasswordProvider;
import org.wildfly.security.password.interfaces.BCryptPassword;
import org.wildfly.security.password.util.ModularCrypt;


import java.util.List;


public class AuthService {

    public boolean verifyPassword(String bCryptPasswordHash, String passwordToVerify) throws Exception {
        WildFlyElytronPasswordProvider provider = new WildFlyElytronPasswordProvider();
        PasswordFactory passwordFactory = PasswordFactory.getInstance(BCryptPassword.ALGORITHM_BCRYPT, provider);
        Password userPasswordDecoded = ModularCrypt.decode(bCryptPasswordHash);
        Password userPasswordRestored = passwordFactory.translate(userPasswordDecoded);
        System.out.println(bCryptPasswordHash);
        return passwordFactory.verify(userPasswordRestored, passwordToVerify.toCharArray());
    }

    public boolean isValidLogin(LoginRequest loginRequest) {
        // Find the user by username
        List<User> userList = User.list("username = ?1", loginRequest.username);
        System.out.println("User list: " + userList);

        // Check if there is a matching user
        if (!userList.isEmpty()) {
            User user = userList.get(0);

            try {
                // Verify the provided password against the stored hashed password
                System.out.println(user.getPassword() + " " + loginRequest.password);
                var test = verifyPassword(user.getPassword(), loginRequest.password);
                System.out.println(test);
                return test;
            } catch (Exception e) {
                System.err.println("Password verification failed for user: " + loginRequest.username);
                return false;
            }
        } else {
            System.err.println("No user found with username: " + loginRequest.username);
            return false;
        }
    }


    public User getUser(LoginRequest loginRequest) {
        List<User> userList = User.list("username = ?1", loginRequest.username);
        if (!userList.isEmpty()) {
            return userList.get(0);
        }
        return null;
    }
}
