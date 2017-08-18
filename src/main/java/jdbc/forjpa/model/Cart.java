package jdbc.forjpa.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "CART")
public class Cart  extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @Column(name = "total")
    private double total;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "cart")
    private Set<Items> items;

    @OneToOne(cascade = CascadeType.PERSIST)
    private User user;

    @Transient
    private String demoTransient;

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

    public Set<Items> getItems() {
        return items;
    }

    public void setItems(Set<Items> items) {
        this.items = items;
    }

    public String getDemoTransient() {
        return demoTransient;
    }

    public void setDemoTransient(String demoTransient) {
        this.demoTransient = demoTransient;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Cart{");
        sb.append("id=").append(id);
        sb.append(", total=").append(total);
        sb.append(", name='").append(name).append('\'');
        sb.append(", items=").append(items);
        sb.append(", user=").append(user);
        sb.append(", demoTransient='").append(demoTransient).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
