package in.ApniDukaan.Services.IMPL;

import in.ApniDukaan.DTOs.OrderRequest;
import in.ApniDukaan.Entities.Cart;
import in.ApniDukaan.Entities.Enums.OrderStatus;
import in.ApniDukaan.Entities.OrderAddress;
import in.ApniDukaan.Entities.ProductOrder;
import in.ApniDukaan.Repositories.CartRepository;
import in.ApniDukaan.Repositories.ProductOrderRepository;
import in.ApniDukaan.Services.Interfaces.OrderService;
import in.ApniDukaan.Utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceIMPL implements OrderService {

    private final ProductOrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CommonUtils commonUtils;

    @Override
    public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception {
        List<Cart> carts = cartRepository.findByUserId(userid);

        for (Cart cart : carts) {
            ProductOrder order = new ProductOrder();

            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderDate(LocalDate.now());
            order.setProduct(cart.getProduct());
            order.setPrice(cart.getProduct().getDiscountPrice());
            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());
            order.setStatus(OrderStatus.IN_PROGRESS.getName());
            order.setPaymentType(orderRequest.getPaymentType());

            OrderAddress address = new OrderAddress();
            address.setFirstName(orderRequest.getFirstName());
            address.setLastName(orderRequest.getLastName());
            address.setEmail(orderRequest.getEmail());
            address.setMobileNo(orderRequest.getMobileNo());
            address.setAddress(orderRequest.getAddress());
            address.setCity(orderRequest.getCity());
            address.setState(orderRequest.getState());
            address.setPinCode(orderRequest.getPinCode());

            order.setOrderAddress(address);

            ProductOrder saveOrder = orderRepository.save(order);
            commonUtils.sendMailForProductOrder(saveOrder, "success");
        }
    }

    @Override
    public List<ProductOrder> getOrdersByUser(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public ProductOrder updateOrderStatus(Integer id, String status) {
        Optional<ProductOrder> findById = orderRepository.findById(id);
        if (findById.isPresent()) {
            ProductOrder productOrder = findById.get();
            productOrder.setStatus(status);
            return orderRepository.save(productOrder);
        }
        return null;
    }

    @Override
    public List<ProductOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public ProductOrder getOrdersByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    @Override
    public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return orderRepository.findAll(pageable);
    }
}
