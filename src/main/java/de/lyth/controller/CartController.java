package de.lyth.controller;

import de.lyth.model.persistence.Cart;
import de.lyth.model.persistence.Item;
import de.lyth.model.persistence.User;
import de.lyth.model.requests.ModifyCartRequest;
import de.lyth.repositories.CartRepository;
import de.lyth.repositories.ItemRepository;
import de.lyth.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/addToCart")
    public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if(user == null) {
            log.error("Can't create cart because the user wasn't found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<Item> item = itemRepository.findById(request.getItemID());
        if(item.isEmpty()) ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Cart cart = user.getCart();
        IntStream.range(0, request.getQuantity())
                .forEach(i -> cart.addItem(item.get()));
        cartRepository.save(cart);
        log.info(String.format("Cart created for user= %s and items %s.", user.getUsername(),
                user.getCart().getItems().stream().map(Item::getName).collect(Collectors.toList())));
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<Cart> removeFormCart(@RequestBody ModifyCartRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if(user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Optional<Item> item = itemRepository.findById(request.getItemID());
        if(item.isEmpty()) ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Cart cart = user.getCart();
        IntStream.range(0, request.getQuantity())
                .forEach(i -> cart.removeItem(item.get()));
        cartRepository.save(cart);
        log.info(String.format("User %s removed item %s from cart.", user.getUsername(), item.get().getName()));
        return ResponseEntity.ok(cart);
    }

}
