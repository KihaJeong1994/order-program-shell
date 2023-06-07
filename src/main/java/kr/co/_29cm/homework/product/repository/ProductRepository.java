package kr.co._29cm.homework.product.repository;

import kr.co._29cm.homework.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
