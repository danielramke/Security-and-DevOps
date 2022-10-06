package de.lyth.controllers;

import de.lyth.TestHelper;
import de.lyth.SecurityAndDevOpsApplication;
import de.lyth.controller.UserController;
import de.lyth.model.persistence.User;
import de.lyth.model.requests.CreateUserRequest;
import de.lyth.repositories.CartRepository;
import de.lyth.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SecurityAndDevOpsApplication.class)
public class UserControllerTest {

    private UserController controller;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        controller = new UserController();
        TestHelper.manipulateObject(controller, "userRepository", userRepository);
        TestHelper.manipulateObject(controller, "cartRepository", cartRepository);
        TestHelper.manipulateObject(controller, "encoder", encoder);
    }

    @Test
    public void checkCreateUser() {
        when(encoder.encode("123456789")).thenReturn("thisIsHashed");
        CreateUserRequest request = createUserRequest();

        ResponseEntity<?> response = controller.create(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        User user = (User) response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals(request.getUsername(), user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void checkFindUserByID() {
        User user = randomUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        ResponseEntity<User> response = controller.findByID(user.getId());
        User selected = response.getBody();
        assertNotNull(selected);
        assertEquals(user.getId(), selected.getId());
        assertEquals(user.getUsername(), selected.getUsername());
        assertEquals(user.getPassword(), selected.getPassword());
    }

    @Test
    public void checkFindUserByName() {
        User user = randomUser();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ResponseEntity<User> response = controller.findByName(user.getUsername());
        User selected = response.getBody();
        assertNotNull(selected);
        assertEquals(user.getId(), selected.getId());
        assertEquals(user.getUsername(), selected.getUsername());
        assertEquals(user.getPassword(), selected.getPassword());
    }

    private static CreateUserRequest createUserRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(TestHelper.randomString());
        request.setPassword("123456789");
        request.setConfirmPassword("123456789");
        return request;
    }

    private static User randomUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername(TestHelper.randomString());
        user.setPassword("123456789");
        return user;
    }

}
