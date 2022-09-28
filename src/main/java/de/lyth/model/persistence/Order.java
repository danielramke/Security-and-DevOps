package de.lyth.model.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "user_order")
@Setter
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    @Column
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonProperty
    @Column
    private List<Item> items;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    @JsonProperty
    private User user;

    @JsonProperty
    @Column
    private BigDecimal total;

    public static Order createFromCart(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems().stream().toList());
        order.setTotal(cart.getTotal());
        order.setUser(cart.getUser());
        return order;
    }

}
