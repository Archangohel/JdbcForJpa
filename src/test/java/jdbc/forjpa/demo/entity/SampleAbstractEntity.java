package jdbc.forjpa.demo.entity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by archangohel on 13/08/17.
 */
public class SampleAbstractEntity  {
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

}
