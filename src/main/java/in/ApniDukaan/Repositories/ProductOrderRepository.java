package in.ApniDukaan.Repositories;

import in.ApniDukaan.Entities.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {
    List<ProductOrder> findByUserId(Integer userId);

    ProductOrder findByOrderId(String orderId);
}
