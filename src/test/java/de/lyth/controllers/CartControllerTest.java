package de.lyth.controllers;

import de.lyth.TestHelper;
import de.lyth.controller.CartController;
import de.lyth.model.persistence.Cart;
import de.lyth.model.persistence.Item;
import de.lyth.model.persistence.User;
import de.lyth.model.requests.ModifyCartRequest;
import de.lyth.repositories.CartRepository;
import de.lyth.repositories.ItemRepository;
import de.lyth.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController controller;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);

    @Before
    public void setUp() {
        controller = new CartController();
        TestHelper.manipulateObject(controller, "userRepository", userRepository);
        TestHelper.manipulateObject(controller, "itemRepository", itemRepository);
        TestHelper.manipulateObject(controller, "cartRepository", cartRepository);
    }

    @Test
    public void checkAddToCart() {
        ModifyCartRequest request = generateModifyCartRequest();
        when(userRepository.findByUsername(request.getUsername())).thenReturn(getUser01());
        when(itemRepository.findById(request.getItemID())).thenReturn(Optional.of(getItem01()));
        ResponseEntity<Cart> response = controller.addToCart(request);
        Cart cart = response.getBody();
        assertNotNull(cart);
        cart.setUser(getUser01());
        assertEquals(request.getUsername(), cart.getUser().getUsername());
        assertTrue(cart.getItems().stream().anyMatch(item -> item.getId() == request.getItemID()));
    }

    @Test
    public void checkRemoveFromCart() {
        ModifyCartRequest request = removeModifyCartRequest();
        when(userRepository.findByUsername(request.getUsername())).thenReturn(getUser02());
        when(itemRepository.findById(request.getItemID())).thenReturn(Optional.of(getItem02()));

        assertEquals(2, getUser02().getCart().getItems().size());
        ResponseEntity<Cart> response = controller.removeFormCart(request);
        Cart cart = response.getBody();
        assertNotNull(cart);
        cart.setUser(getUser02());
        assertEquals(request.getUsername(), cart.getUser().getUsername());
        assertEquals(1, cart.getItems().size());
        assertFalse(cart.getItems().stream().anyMatch(item -> item.getId() == request.getItemID()));
    }

    private static ModifyCartRequest generateModifyCartRequest() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemID(getItem01().getId());
        request.setUsername(getUser01().getUsername());
        request.setQuantity(3);
        return request;
    }

    private static ModifyCartRequest removeModifyCartRequest() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemID(getItem02().getId());
        request.setUsername(getUser02().getUsername());
        request.setQuantity(1);
        return request;
    }

    private static Item getItem01() {
        return generateItem(1L, "Item1", "Item 1 description", 10);
    }

    private static Item getItem02() {
        return generateItem(2L, "Item2", "Item 2 description", 100);
    }

    private static Cart generateCart() {
        Item item = getItem01();
        List<Item> items = new ArrayList<>(List.of(item));
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(items);
        return cart;
    }

    private static Cart removeItemFromCart() {
        List<Item> items = new ArrayList<>(Arrays.asList(getItem01()
                , getItem02()));
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(items);
        return cart;
    }

    private static Item generateItem(Long id, String name, String description, double price) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setPrice(new BigDecimal(price));
        return item;
    }

    private static User getUser01() {
        return generateUser(1L, "Daniel", "1234567", false);
    }

    private static User getUser02() {
        return generateUser(2L, "Test", "123456789", true);
    }

    private static User generateUser(long id, String username, String password, boolean removed) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        if(!removed) {
            user.setCart(generateCart());
        } else {
            user.setCart(removeItemFromCart());
        }
        return user;
    }

}
