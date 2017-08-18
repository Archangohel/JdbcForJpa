package jdbc.forjpa.demo.entity;


import javax.persistence.*;
import java.util.Set;

/**
 * Created by archangohel on 13/08/17.
 */
public class SampleEntity extends SampleAbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private long id;

    @Column(name = "total")
    private double total;

    @Column(name = "name")
    private String name;

    @OneToMany
    private Set<SampleOneToMany> oneToManyList;

    @OneToOne
    private SampleOneToOne oneToOneField;

    @ManyToOne
    private SampleManyToOne manyToOneField;

    @ManyToMany
    private SampleManyToMany manyToManyField;

    @Transient
    private String demoTransient;

    @Embedded
    private EmbeddedEntity embeddedEntity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDemoTransient() {
        return demoTransient;
    }

    public void setDemoTransient(String demoTransient) {
        this.demoTransient = demoTransient;
    }

    public EmbeddedEntity getEmbeddedEntity() {
        return embeddedEntity;
    }

    public void setEmbeddedEntity(EmbeddedEntity embeddedEntity) {
        this.embeddedEntity = embeddedEntity;
    }

    public Set<SampleOneToMany> getOneToManyList() {
        return oneToManyList;
    }

    public void setOneToManyList(Set<SampleOneToMany> oneToManyList) {
        this.oneToManyList = oneToManyList;
    }

    public SampleOneToOne getOneToOneField() {
        return oneToOneField;
    }

    public void setOneToOneField(SampleOneToOne oneToOneField) {
        this.oneToOneField = oneToOneField;
    }

    public SampleManyToOne getManyToOneField() {
        return manyToOneField;
    }

    public void setManyToOneField(SampleManyToOne manyToOneField) {
        this.manyToOneField = manyToOneField;
    }

    public SampleManyToMany getManyToManyField() {
        return manyToManyField;
    }

    public void setManyToManyField(SampleManyToMany manyToManyField) {
        this.manyToManyField = manyToManyField;
    }
}
