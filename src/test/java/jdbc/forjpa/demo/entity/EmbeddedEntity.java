package jdbc.forjpa.demo.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by archangohel on 14/08/17.
 */
@Embeddable
public class EmbeddedEntity {

    @Column(name = "embedded_id")
    private Long embeddedId;
    @Column(name = "embedded_name")
    private String embeddedName;

    public Long getEmbeddedId() {
        return embeddedId;
    }

    public void setEmbeddedId(Long embeddedId) {
        this.embeddedId = embeddedId;
    }

    public String getEmbeddedName() {
        return embeddedName;
    }

    public void setEmbeddedName(String embeddedName) {
        this.embeddedName = embeddedName;
    }
}
