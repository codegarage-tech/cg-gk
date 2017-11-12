package com.reversecoder.gk7.driver.model;

/**
 * Created by rashed on 3/2/16.
 */
public class Area {

    private String id = "";
    private String title = "";
    private String status = "";
    private String created = "";
    private String modified = "";
    private String created_by = "";
    private String modified_by = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    @Override
    public String toString() {
        return "Area{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                ", created_by='" + created_by + '\'' +
                ", modified_by='" + modified_by + '\'' +
                '}';
    }
}
