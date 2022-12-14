package de.lyth.controller;

import de.lyth.model.persistence.Item;
import de.lyth.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public ResponseEntity<List<Item>> getItems() {
        return ResponseEntity.ok(itemRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemByID(@PathVariable Long id) {
        return ResponseEntity.of(itemRepository.findById(id));
    }


    @GetMapping("/name/{name}")
    public ResponseEntity<List<Item>> getItemByName(@PathVariable String name) {
        List<Item> items = itemRepository.findByName(name);
        return items == null || items.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(items);
    }

}
