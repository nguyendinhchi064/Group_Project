//package UserPage;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "user_db", schema = "mydatabase", catalog = "")
//public class UserDb {
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
//    @Column(name = "Userid")
//    private int userid;
//    @Basic
//    @Column(name = "username")
//    private String username;
//    @Basic
//    @Column(name = "password")
//    private String password;
//    @Basic
//    @Column(name = "email")
//    private String email;
//    @Basic
//    @Column(name = "role")
//    private String role;
//
//    public int getUserid() {
//        return userid;
//    }
//
//    public void setUserid(int userid) {
//        this.userid = userid;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        UserDb userDb = (UserDb) o;
//
//        if (userid != userDb.userid) return false;
//        if (username != null ? !username.equals(userDb.username) : userDb.username != null) return false;
//        if (password != null ? !password.equals(userDb.password) : userDb.password != null) return false;
//        if (email != null ? !email.equals(userDb.email) : userDb.email != null) return false;
//        if (role != null ? !role.equals(userDb.role) : userDb.role != null) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = userid;
//        result = 31 * result + (username != null ? username.hashCode() : 0);
//        result = 31 * result + (password != null ? password.hashCode() : 0);
//        result = 31 * result + (email != null ? email.hashCode() : 0);
//        result = 31 * result + (role != null ? role.hashCode() : 0);
//        return result;
//    }
//}
