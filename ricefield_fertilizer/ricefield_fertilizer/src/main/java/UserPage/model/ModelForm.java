package UserPage.model;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import java.io.InputStream;

public class ModelForm {

    @FormParam("modelName")
    @PartType(MediaType.TEXT_PLAIN)
    private String modelName;

    @FormParam("modelDescription")
    @PartType(MediaType.TEXT_PLAIN)
    private String modelDescription;

    @FormParam("file")
    @PartType("application/octet-stream")
    private InputStream file;


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

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }
}
