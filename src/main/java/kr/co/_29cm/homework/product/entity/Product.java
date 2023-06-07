package kr.co._29cm.homework.product.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Product {

    @Id
    @JsonProperty("상품번호")
    private Long id;

    @JsonProperty("상품명")
    private String name;

    @JsonProperty("판매가격")
    private Integer price;

    @JsonProperty("재고수량")
    private Integer stock;


}
