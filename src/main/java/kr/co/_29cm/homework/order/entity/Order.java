package kr.co._29cm.homework.order.entity;

import kr.co._29cm.homework.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Order {

    private Long id;

    // @ManyToMany : JPA 다대다를 고려해 Long productId 대신 Product product 사용
    private Product product;
    private Integer quantity;

    public Order(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
