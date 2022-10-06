package de.lyth.controller;

import de.lyth.model.persistence.Order;
import de.lyth.model.persistence.User;
import de.lyth.repositories.OrderRepository;
import de.lyth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/submit/{name}")
    public ResponseEntity<Order> submit(@PathVariable String name) {
        User user = userRepository.findByName(name);
        if(user == null) return ResponseEntity.notFound().build();

        Order order = Order.createFromCart(user.getCart());
        orderRepository.save(order);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/history/{name}")
    public ResponseEntity<List<Order>> getOrdersForUser(@PathVariable String name) {
        User user = userRepository.findByName(name);
        if(user == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(orderRepository.findByUser(user));
    }

}
