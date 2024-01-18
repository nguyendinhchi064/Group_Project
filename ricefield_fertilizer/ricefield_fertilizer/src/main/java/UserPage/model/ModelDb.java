package UserPage.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.Objects;

@Entity
@Table(name = "model_db", schema = "mydatabase")
public class ModelDb extends PanacheEntityBase {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "ModelID")
    private int modelId;
    @Basic
    @Column(name = "ModelName")
    private String modelName;
    @Basic
    @Column(name = "ModelData")
    private String modelData;
    @ManyToOne
    @JoinColumn(name = "ProjID")
    private ProjDb projDb;

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelData() {
        return modelData;
    }

    public void setModelData(String modelData) {
        this.modelData = modelData;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelDb modelDb = (ModelDb) o;

        if (modelId != modelDb.modelId) return false;
        if (!Objects.equals(modelName, modelDb.modelName)) return false;
        return Objects.equals(modelData, modelDb.modelData);
    }

    @Override
    public int hashCode() {
        int result = modelId;
        result = 31 * result + (modelName != null ? modelName.hashCode() : 0);
        result = 31 * result + (modelData != null ? modelData.hashCode() : 0);
        return result;
    }

    public ProjDb getProjDb() {
        return projDb;
    }

    public void setProjDb(ProjDb projDb) {
        this.projDb = projDb;
    }
    @Transactional
    public static ModelDb updateModel(int modelId, ModelDb updatedProject) {
        ModelDb existingProject = findById(modelId);

        if (existingProject != null) {
            existingProject.setModelName(updatedProject.getModelName());;
            existingProject.setModelData(updatedProject.getModelData());
            existingProject.persist();
        }

        return existingProject;
    }
    @Transactional
    public static void deleteModel(int modelId) {
        ModelDb modelDb = findById(modelId);
        if (modelDb != null) {
            modelDb.delete();
        }
    }
}
