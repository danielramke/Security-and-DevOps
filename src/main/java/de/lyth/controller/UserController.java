package de.lyth.controller;

import de.lyth.model.persistence.Cart;
import de.lyth.model.persistence.User;
import de.lyth.model.requests.CreateUserRequest;
import de.lyth.repositories.CartRepository;
import de.lyth.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findByID(@PathVariable Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<User> findByName(@PathVariable String name) {
        User user = userRepository.findByName(name);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateUserRequest request) {
        if(userRepository.existsByName(request.getName())) {
            log.warn("Username {} is already taken! Try another username...", request.getName());
            return ResponseEntity.badRequest().body(String.format("Username %s is already taken! Please chose another one!", request.getName()));
        }
        User user = new User();
        user.setName(request.getName());
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);

        if(request.getPassword().length() < 7) {
            log.error("Password is to short! Minimum digits 7 you have {} ", request.getPassword().length());
            return ResponseEntity.badRequest().body(String.format("Password is to short! Minimum digits 7 you have %s", request.getPassword().length()));
        }

        if(!request.getPassword().equals(request.getConfirmPassword())) {
            log.error("The password is not matches the confirmed password!");
            return ResponseEntity.badRequest().body("The password is not matches the confirmed password!");
        }

        user.setPassword(encoder.encode(request.getPassword()));
        userRepository.save(user);
        log.info("Created successfully new user: " + request.getName());
        return ResponseEntity.ok(user);
    }

}
