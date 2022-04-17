package ru.job4j.tracker.store;

import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.react.Observe;

import java.util.List;

public interface Store extends AutoCloseable {
    void init();
    Item add(Item item);
    boolean replace(int id, Item item);
    boolean delete(int id);
    List<Item> findAll();
    List<Item> findByName(String key);
    Item findById(int id);
    void getByReact(Observe<Item> observe) throws InterruptedException;
}