package UserPage;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import java.util.Objects;

@Entity
@Table(name = "proj_db", schema = "mydatabase")
public class ProjDb extends PanacheEntityBase {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ProjID")
    private int projId;
    @Basic
    @Column(name = "Projname")
    public String projname;
    @Basic
    @Column(name = "Description")
    public String description;
    @Basic
    @Column(name = "Userid")
    private Integer userid;

    public int getProjId() {
        return projId;
    }

    public void setProjId(int projId) {
        this.projId = projId;
    }

    public String getProjname() {
        return projname;
    }

    public void setProjname(String projname) {
        this.projname = projname;
    }

    public String getDiscription() {
        return description;
    }

    public void setDiscription(String description) {
        this.description = description;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjDb projDb = (ProjDb) o;

        if (projId != projDb.projId) return false;
        if (!Objects.equals(projname, projDb.projname)) return false;
        if (!Objects.equals(description, projDb.description)) return false;
        return Objects.equals(userid, projDb.userid);
    }

    @Override
    public int hashCode() {
        int result = projId;
        result = 31 * result + (projname != null ? projname.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (userid != null ? userid.hashCode() : 0);
        return result;
    }

    @Transactional
    public static Response addProject(String projname, String discription) {
        try {
            ProjDb projDb = new ProjDb();
            projDb.setProjname(projname);
            projDb.setDiscription(discription);

            // Persist the new project entity
            projDb.persist();

            // Optionally, you can return a response with a status code and URI to the created resource
            return null;
        } catch (Exception e) {
            // Handle exceptions, log, and return an appropriate response
            return Response.serverError().entity("Failed to create project").build();
        }
    }
}
