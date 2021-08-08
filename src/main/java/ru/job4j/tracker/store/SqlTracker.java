package ru.job4j.tracker.store;

import ru.job4j.tracker.model.Item;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.sql.*;

public class SqlTracker implements Store {

    private Connection cn;

    public void init() {
        try (InputStream in = SqlTracker.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            cn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Item add(Item item) {
        try (PreparedStatement statement =
                     cn.prepareStatement("insert into items(name, created) values (?, ?)")) {
            statement.setString(1, item.getName());
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return item;
    }

    @Override
    public void close() throws Exception {
        if (cn != null) {
            cn.close();
        }
    }

    @Override
    public boolean replace(int id, Item item) {
        boolean result = false;
        try (PreparedStatement statement =
                     cn.prepareStatement("update items set name = ?, created = ? where items.id = ?")) {
            statement.setString(1, item.getName());
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(3, id);
            result = statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        try (PreparedStatement statement =
                     cn.prepareStatement("delete from items where id = ?")) {
            statement.setInt(1, id);
            result = statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Item> findAll() {
        List<Item> resultList = new ArrayList<>();
        try (Statement statement = cn.createStatement()) {
            String sql = "select * from items";
            try (ResultSet rs = statement.executeQuery(sql)) {
                resultList = rsToItemsList(rs);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultList;
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> resultList = new ArrayList<>();
        try (PreparedStatement statement = cn.prepareStatement("select * from items where items.name = ?")) {
            statement.setString(1, key);
            try (ResultSet rs = statement.executeQuery()) {
                resultList = rsToItemsList(rs);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultList;
    }

    @Override
    public Item findById(int id) {
        List<Item> resultList = new ArrayList<>();
        try (PreparedStatement statement = cn.prepareStatement("select * from items where items.id = ?")) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                resultList = rsToItemsList(rs);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (resultList.size() > 0) {
            return resultList.get(0);
        } else return null;
    }

    public List<Item> rsToItemsList(ResultSet rs) {
        List<Item> resultList = new ArrayList<>();
        try {
            while (rs.next()) {
                resultList.add(new Item(rs.getInt(1),
                        rs.getString(2),
                        rs.getTimestamp(3).toLocalDateTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
