package de.lyth.repositories;

import de.lyth.model.persistence.Cart;
import de.lyth.model.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
