package jdbc.forjpa.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by archangohel on 13/08/17.
 */
@MappedSuperclass
public abstract class AbstractEntity {
    private String createdBy;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdOn;

    private String updatedBy;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedOn;

    @PrePersist
    public final void prePersist() {
        setCreatedOn(new Date());
        setUpdatedOn(new Date());
        setCreatedBy("root");
        setUpdatedBy("root");
    }

    @PreUpdate
    protected final void preUpdate() {
        setUpdatedOn(new Date());
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AbstractEntity{");
        sb.append("createdBy='").append(createdBy).append('\'');
        sb.append(", createdOn=").append(createdOn);
        sb.append(", updatedBy='").append(updatedBy).append('\'');
        sb.append(", updatedOn=").append(updatedOn);
        sb.append('}');
        return sb.toString();
    }
}
