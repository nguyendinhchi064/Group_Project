package Login_Register;

import UserPage.UserResource;
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
import jakarta.ws.rs.core.UriBuilder;

import java.util.regex.Pattern;


@Entity
@Table(name = "user_db")
@UserDefinition
public class User extends PanacheEntityBase {
    @Id
    @Column(name = "Userid")
    private int userid;
    @Username
    public String username;
    @Password
    public String password;
    @Roles
    public String role;
    @Column(unique = true)
    @Email
    public String email;


    public int getUserID() {
        return userid;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if(userExists(username)){
            System.out.println("This user is already exist");
            throw new IllegalArgumentException("This user is already exist");
        }
        else{
            this.username = username;
        }
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password) {
        // Validate password before hashing
        if (isValidPassword(password)) {
            this.password = BcryptUtil.bcryptHash(password);
        }
        else {
            throw new IllegalArgumentException("Invalid password");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(emailExists(email)){
            System.out.println("This user is already exist");
            throw new IllegalArgumentException("This email is already exist");
        }
        else{
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
    final static String SEPARATOR = ",";
    /**
     * Adds a new user to the database
     *
     * @param username the username
     * @param password the unencrypted password (it will be encrypted with bcrypt)
     * @param role     the comma-separated roles
     * @return null
     */
    @Transactional
    public static Response add(String username, String password, String role, String email) {
        User user = new User();
        user.setUsername(username);
        user.setRole(role);
        user.setPassword(password);
        user.setEmail(email);
        if(user.role.equals(USERROLE)){
            System.out.println(user.userid);
            user.persist();
        }else if(user.role.equals(ADMINROLE)){
            user.persist();
        }
        else{
            System.out.println("Invalid role");
        }
        return Response.created(UriBuilder.fromResource(AuthResource.class).build())
                .entity("Register successfully")
                .build();

    }


//    public static void addAdminRole(Long userId){
//        User user = User.findById(userId);
//        if(userExists(user.username)){
//            addRole(user, ADMINROLE);
//            user.persist();
//        }
//    }
//    public String generateJWT(String username){
//        //todo: add password verification
//        User foundUser = User.find("username", username).firstResult();
//        return TokenGenerator.generate(username);
//    }
    private static boolean userExists(String username){
        return (User.count("username", username) > 0);
    }
    private static boolean emailExists(String email){
        return (User.count("email", email) > 0);
    }
    public static User findByName(String username){
        return find("username", username).firstResult();
    }
    private static void addRole(User user, String role){
        user.role = user.role + SEPARATOR + role;
    }
    private static boolean isValidPassword(String password) {
        // Minimum length of 8 characters and limit at 12 characters, at least one uppercase letter, and at least one special character
        String regex = "^(?=.*[A-Z])(?=.*[!@#$%^&*()-+]).{8,12}$";
        return Pattern.matches(regex, password);
    }
}

