package Login_Register;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.ws.rs.core.Response;

import java.util.List;

import java.util.regex.Pattern;



@Entity
@Table(name = "user_db")
@UserDefinition
public class User extends PanacheEntityBase {
    @Id
    @Column(name = "UserID")
    public int userid;
    @Username
    public String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Password
    public String password;
    @Roles
    public String role;
    @Column(unique = true)
    @Email
    public String email;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (userExists(username)) {
            throw new IllegalArgumentException("This user is already exist");
        } else {
            this.username = username;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // Validate password before hashing
        if (isValidPassword(password)) {
            this.password = BcryptUtil.bcryptHash(password);
        } else {
            throw new IllegalArgumentException("Invalid password");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (emailExists(email)) {
            System.out.println("This user is already exist");
            throw new IllegalArgumentException("This email is already exist");
        } else {
            this.email = email;
        }
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    final static String USERROLE = "User";
    final static String ADMINROLE = "Admin";

    /**
     * Adds a new user to the database
     *
     * @param username the username
     * @param password the unencrypted password (it will be encrypted with bcrypt)
     * @param role     the comma-separated roles
     * @return null
     */
    @Transactional
    public static String add(String username, String password, String role, String email) {
        User user = new User();
        user.setUsername(username);
        user.setRole(role);
        user.setPassword(password);
        user.setEmail(email);

        if (USERROLE.equals(user.getRole()) || ADMINROLE.equals(user.getRole())) {
            user.persist();
            return Response.ok(user).build().toString();
        } else {
            System.out.println("Invalid role");
            return "Invalid role";
        }
    }



    private static boolean userExists(String username) {
        return (User.count("username", username) > 0);
    }

    private static boolean emailExists(String email) {
        return (User.count("email", email) > 0);
    }

    public static User findByName(String username) {
        return find("username", username).firstResult();
    }

    public static List<User> findByNamePass(String username, String password) {
        return User.list("username = ?1 and password = ?2",
                username, BcryptUtil.bcryptHash(password));
    }

    private static boolean isValidPassword(String password) {
        // Minimum length of 5 characters, at least one uppercase letter, and at least one special character
        String regex = "^(?=.*[A-Z])(?=.*[!@#$%^&*()-+]).{5,}$";
        return Pattern.matches(regex, password);
    }
}

