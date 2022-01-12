package ru.job4j.tracker.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.tracker.model.Item;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class HbmTrackerTest {
    private BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/scripts/update_001.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @After
    public void dropTable() {
        try (Connection con = pool.getConnection();
             PreparedStatement pr = con.prepareStatement(
                     "DROP TABLE IF EXISTS items")) {
            pr.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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