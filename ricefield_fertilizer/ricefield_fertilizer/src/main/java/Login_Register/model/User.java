package Login_Register.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;



@Entity
@Table(name = "user_db")
@UserDefinition
public class User extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UserID")
    public long userid;

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
        if (USERROLE.equals(role) || ADMINROLE.equals(role)) {
            this.role = role;
        } else {
            throw new IllegalArgumentException("Invalid role");
        }
    }

    final static String USERROLE = "User";
    final static String ADMINROLE = "Admin";

    public boolean isAdmin() {
        return ADMINROLE.equals(role);
    }
    public List<String> generateGroups() {
        if (isAdmin()) {
            return Arrays.asList(USERROLE, ADMINROLE);
        } else {
            return Arrays.asList(USERROLE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!Objects.equals(username, user.username)) return false;
        if (!Objects.equals(password, user.password)) return false;
        if (!Objects.equals(email, user.email)) return false;
        return Objects.equals(role, user.role);
    }

    private boolean userExists(String username) {
        return (User.count("username", username) > 0);
    }

    private boolean emailExists(String email) {
        return (User.count("email", email) > 0);
    }


    private boolean isValidPassword(String password) {
        // Minimum length of 5 characters, at least one uppercase letter, and at least one special character
        String regex = "^(?=.*[A-Z])(?=.*[!@#$%^&*()-+]).{5,}$";
        return Pattern.matches(regex, password);
    }

    public long getUserid() {
        return userid;
    }

}