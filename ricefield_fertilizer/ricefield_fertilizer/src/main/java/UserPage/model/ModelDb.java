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
        private long modelId;
        @Basic
        @Column(name = "ModelName")
        private String modelName;
        @Basic
        @Column(name = "ModelDescription")
        private String modelDescription;
        @OneToOne
        @JoinColumn(name = "modelDb")
        private DataEntity dataEntity;

        public long getModelId() {
            return modelId;
        }


        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public String getModelDescription() {
            return modelDescription;
        }

        public void setModelDescription(String modelDescription) {
            this.modelDescription = modelDescription;
        }



        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ModelDb modelDb = (ModelDb) o;

            if (modelId != modelDb.modelId) return false;
            if (!Objects.equals(modelName, modelDb.modelName)) return false;
            return Objects.equals(modelDescription, modelDb.modelDescription);
        }

        @Override
        public int hashCode() {
            long result = modelId;
            result = 31 * result + (modelName != null ? modelName.hashCode() : 0);
            result = 31 * result + (modelDescription != null ? modelDescription.hashCode() : 0);
            return (int) result;
        }

    @Transactional
    public static ModelDb updateModel(int modelId, ModelDb updatedProject) {
        ModelDb existingProject = findById(modelId);

        if (existingProject != null) {
            existingProject.setModelName(updatedProject.getModelName());;
            existingProject.setModelDescription(updatedProject.getModelDescription());
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

        public DataEntity getDataEntity() {
            return dataEntity;
        }

    }
