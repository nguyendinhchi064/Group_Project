package Login_Register.model;

import UserPage.model.ProjDb;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.jwt.build.Jwt;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;

import java.util.Arrays;
import java.util.HashSet;
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
    @OneToMany(mappedBy = "user")
    private List<ProjDb> projects;


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
    public static User findByUsername(String username) {
        return User.find("username", username).firstResult();
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

    // Check if the user is an admin
    private boolean isAdmin() {
        if(role.equals("Admin")){
            return true;
        } else {
            return false;
        }
    }
    private boolean isUser() {
        if(role.equals("User")){
            return true;
        } else {
            return false;
        }
    }
    public String generate(User user) {
        List<String> groups;
        if (user.isAdmin()) {
            groups = Arrays.asList("User", "Admin");
        } else if(user.isUser()) {
            groups = Arrays.asList("User");
        } else {
            return "False";
        }

        // Set the token lifespan, for example, 1 hour (3600 seconds)
        long durationSeconds = 3600;
        long currentTimeInSeconds = System.currentTimeMillis() / 1000;
        long expirationTime = currentTimeInSeconds + durationSeconds;

        String token = Jwt.upn(user.getUsername()) // User Principal Name
                .groups(new HashSet<>(groups))
                .claim("username", user.getUsername()) // Add username as a custom claim
                .expiresAt(expirationTime)
                .sign();
        System.out.println(token);
        return token;
    }

    public List<ProjDb> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjDb> projects) {
        this.projects = projects;
    }
}