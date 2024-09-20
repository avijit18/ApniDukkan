package in.ApniDukaan.Services.IMPL;

import in.ApniDukaan.Entities.Cart;
import in.ApniDukaan.Entities.Product;
import in.ApniDukaan.Entities.UserEntity;
import in.ApniDukaan.Repositories.CartRepository;
import in.ApniDukaan.Repositories.ProductRepository;
import in.ApniDukaan.Repositories.UserRepository;
import in.ApniDukaan.Services.Interfaces.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceIMPL implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public Cart saveCart(Integer productId, Integer userId) {
        UserEntity userDtls = userRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();

        Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userId);

        Cart cart = null;

        if (ObjectUtils.isEmpty(cartStatus)) {
            cart = new Cart();
            cart.setProduct(product);
            cart.setUser(userDtls);
            cart.setQuantity(1);
            cart.setTotalPrice(1 * product.getDiscountPrice());
        } else {
            cart = cartStatus;
            cart.setQuantity(cart.getQuantity() + 1);
            cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getDiscountPrice());
        }
        Cart saveCart = cartRepository.save(cart);

        return saveCart;
    }

    @Override
    public List<Cart> getCartsByUser(Integer userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);

        Double totalOrderPrice = 0.0;
        List<Cart> updateCarts = new ArrayList<>();
        for (Cart c : carts) {
            Double totalPrice = (c.getProduct().getDiscountPrice() * c.getQuantity());
            c.setTotalPrice(totalPrice);
            totalOrderPrice = totalOrderPrice + totalPrice;
            c.setTotalOrderPrice(totalOrderPrice);
            updateCarts.add(c);
        }
        return updateCarts;
    }

    @Override
    public Integer getCountCart(Integer userId) {
        return cartRepository.countByUserId(userId);
    }

    @Override
    public void updateQuantity(String sy, Integer cid) {
        Cart cart = cartRepository.findById(cid).get();
        int updateQuantity;

        if (sy.equalsIgnoreCase("de")) {
            updateQuantity = cart.getQuantity() - 1;

            if (updateQuantity <= 0) {
                cartRepository.delete(cart);
            } else {
                cart.setQuantity(updateQuantity);
                cartRepository.save(cart);
            }
        } else {
            updateQuantity = cart.getQuantity() + 1;
            cart.setQuantity(updateQuantity);
            cartRepository.save(cart);
        }
    }
}
