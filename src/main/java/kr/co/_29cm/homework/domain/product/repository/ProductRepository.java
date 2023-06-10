package kr.co._29cm.homework.domain.product.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import kr.co._29cm.homework.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "100")})
    @Query("select p from Product p where p.id = ?1")
    Product findByIdForUpdate(Long id);

    List<Product> findAllByOrderByIdDesc();
}
