package jdbc.forjpa.model;

import javax.persistence.*;

@Entity
@Table(name = "ITEMS")
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "item_total")
    private double itemTotal;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    //Hibernate requires no-args constructor
    public Items() {
    }

    public Items(String itemId, double total, int qty, Cart c) {
        this.itemId = itemId;
        this.itemTotal = total;
        this.quantity = qty;
        this.cart = c;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public double getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(double itemTotal) {
        this.itemTotal = itemTotal;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Items{");
        sb.append("id=").append(id);
        sb.append(", itemId='").append(itemId).append('\'');
        sb.append(", itemTotal=").append(itemTotal);
        sb.append(", quantity=").append(quantity);
        sb.append('}');
        return sb.toString();
    }
}
