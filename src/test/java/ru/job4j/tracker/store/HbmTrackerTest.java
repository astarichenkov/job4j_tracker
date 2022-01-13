package ru.job4j.tracker.store;

import org.junit.Test;
import ru.job4j.tracker.model.Item;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HbmTrackerTest {

    @Test
    public void whenAddTwoItemsAndFindAll() {
        HbmTracker hbmTracker = new HbmTracker();
        hbmTracker.add(new Item("firstItem"));
        hbmTracker.add(new Item("secondItem"));
        List<Item> list = hbmTracker.findAll();
        assertEquals(list.size(), 2);
        assertEquals(list.get(0).getName(), "firstItem");
        assertEquals(list.get(1).getName(), "secondItem");
    }

    @Test
    public void whenFindByName() {
        HbmTracker hbmTracker = new HbmTracker();
        hbmTracker.add(new Item("firstItem"));
        List<Item> list = hbmTracker.findByName("firstItem");
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getName(), "firstItem");
    }

    @Test
    public void whenFindById() {
        HbmTracker hbmTracker = new HbmTracker();
        hbmTracker.add(new Item("firstItem"));
        Item item = hbmTracker.findById(1);
        assertEquals(item.getName(), "firstItem");
    }

    @Test
    public void whenReplaceItem() {
        HbmTracker hbmTracker = new HbmTracker();
        hbmTracker.add(new Item("firstItem"));
        hbmTracker.replace(1, new Item("replacedItem"));
        Item item = hbmTracker.findById(1);
        assertEquals(item.getName(), "replacedItem");
    }

    @Test
    public void whenDeleteItem() {
        HbmTracker hbmTracker = new HbmTracker();
        hbmTracker.add(new Item("firstItem"));
        assertTrue(hbmTracker.delete(1));
    }
}