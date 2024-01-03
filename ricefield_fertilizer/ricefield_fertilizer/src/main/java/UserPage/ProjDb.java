package UserPage;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
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

    public static ProjDb findById(int id) {
        return find("projId", id).firstResult();
    }

    @Transactional
    public static ProjDb updateProject(int projId, ProjDb updatedProject) {
        ProjDb existingProject = findById(projId);

        if (existingProject != null) {
            existingProject.setProjname(updatedProject.getProjname());
            existingProject.setDescription(updatedProject.getDescription());
            existingProject.setUserid(updatedProject.getUserid());
            existingProject.persist();
        }

        return existingProject;
    }

    @Transactional
    public static void deleteProject(int projId) {
        ProjDb project = findById(projId);
        if (project != null) {
            project.delete();
        }
    }
}
