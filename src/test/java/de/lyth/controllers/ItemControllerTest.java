package de.lyth.controllers;

import de.lyth.TestHelper;
import de.lyth.controller.ItemController;
import de.lyth.model.persistence.Item;
import de.lyth.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController controller;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        controller = new ItemController();
        TestHelper.manipulateObject(controller, "itemRepository", itemRepository);
    }

    @Test
    public void checkGetItemList() {
        Item item = generateItem();
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item, item, item));
        ResponseEntity<List<Item>> response = controller.getItems();
        List<Item> selected = response.getBody();
        assertNotNull(selected);
        assertEquals(3, selected.size());
    }

    @Test
    public void checkGetItemByID() {
        Item item = generateItem();
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = controller.getItemByID(item.getId());
        Item selected = response.getBody();
        assertNotNull(selected);
        assertEquals(item.getId(), selected.getId());
        assertEquals(item.getName(), selected.getName());
        assertEquals(item.getDescription(), selected.getDescription());
        assertEquals(item.getPrice(), selected.getPrice());
    }

    @Test
    public void checkGetItemByName() {
        Item item = generateItem();
        when(itemRepository.findByName(item.getName())).thenReturn(List.of(item));
        ResponseEntity<List<Item>> response = controller.getItemByName(item.getName());
        List<Item> selected = response.getBody();
        assertNotNull(selected);
        assertEquals(1, selected.size());
        assertEquals(item.getName(), selected.get(0).getName());
        assertEquals(item.getDescription(), selected.get(0).getDescription());
        assertEquals(item.getPrice(), selected.get(0).getPrice());
    }


    private static Item generateItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Random Item");
        item.setDescription("This item is for Unit testing!");
        item.setPrice(new BigDecimal(200));
        return item;
    }

}
