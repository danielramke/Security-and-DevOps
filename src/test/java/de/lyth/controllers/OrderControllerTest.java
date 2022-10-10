package de.lyth.controllers;

import de.lyth.TestHelper;
import de.lyth.controller.OrderController;
import de.lyth.model.persistence.Cart;
import de.lyth.model.persistence.Item;
import de.lyth.model.persistence.Order;
import de.lyth.model.persistence.User;
import de.lyth.repositories.OrderRepository;
import de.lyth.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController controller;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        controller = new OrderController();
        TestHelper.manipulateObject(controller, "userRepository", userRepository);
        TestHelper.manipulateObject(controller, "orderRepository", orderRepository);
    }

    @Test
    public void checkCreateOrderByUsername() {
        User user = generateUser();
        Cart cart = generateCart();
        cart.setUser(user);
        user.setCart(cart);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ResponseEntity<Order> response = controller.submit(user.getUsername());
        Order order = response.getBody();
        assertNotNull(order);
        assertEquals(user, order.getUser());
        assertEquals(user.getCart().getItems(), order.getItems());
        assertEquals(user.getCart().getTotal(), order.getTotal());
    }

    @Test
    public void checkCreateOrderByInvalidUsername() {
        ResponseEntity<Order> response = controller.submit(generateUser().getUsername());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());
    }

    @Test
    public void checkGetOrderByUsername() {
        Order order = generateOrder();
        when(userRepository.findByUsername(order.getUser().getUsername())).thenReturn(order.getUser());
        when(orderRepository.findByUser(order.getUser())).thenReturn(Arrays.asList(order, order, order));
        ResponseEntity<List<Order>> response = controller.getOrdersForUser(order.getUser().getUsername());
        List<Order> orderList = response.getBody();
        assertNotNull(orderList);
        assertEquals(3, orderList.size());
        orderList.forEach(o -> assertEquals(order.getUser(), o.getUser()));
    }

    private static Order generateOrder() {
        Item first = generateItem(1L, "First", "The first item...", 10);
        Item second = generateItem(2L, "Second", "The second item...", 100);
        User user = generateUser();
        final List<Item> items = Arrays.asList(first, second);
        Order order = new Order();
        order.setId(1L);
        order.setItems(items);
        order.setUser(user);
        return order;
    }

    private static Cart generateCart() {
        Item first = generateItem(1L, "First", "The first item...", 10);
        Item second = generateItem(2L, "Second", "The second item...", 100);
        final List<Item> items = Arrays.asList(first, second);
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(items);
        cart.setTotal(new BigDecimal(500));
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

    private static User generateUser() {
        Cart cart = generateCart();
        User user = new User();
        user.setId(1);
        user.setUsername("Daniel");
        user.setPassword("1234567");
        user.setCart(cart);
        return user;
    }
}
