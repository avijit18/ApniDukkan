package in.ApniDukaan.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private Product product;

    private Integer quantity;

    @Transient
    private Double totalPrice;

    @Transient
    private Double totalOrderPrice;
}
