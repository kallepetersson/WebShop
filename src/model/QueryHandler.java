package model;

import java.sql.*;
import java.util.ArrayList;

public class QueryHandler {

    private static Connection connection = null;
    private static String dataBaseName = "jdbc:sqlite:webshop.db";

    public void createItem(ArrayList<String> itemInfo) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO items(item_id, itemname, category, price, stock, description) " +
                    "VALUES (?,?,?,?,?,?)");
            for (int i = 0; i < 6; i++) {
                ps.setString(i + 1, itemInfo.get(i));
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItemStock(int item_id, int stock) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE items SET stock=stock+? where item_id=?");
            ps.setInt(1, stock);
            ps.setInt(2, item_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItemPrice(int item_id, int price) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE items SET price=? where item_id=?");
            ps.setInt(1, price);
            ps.setInt(2, item_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }    }

    public void deleteItem(String name) {

        try {
            PreparedStatement ps = connection.prepareStatement("delete from items where itemname = ?");
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> getItemsInCategory(String category) {
        ArrayList<String> itemsInCategory = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("select itemname from items where category=? order by itemname collate nocase");
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                itemsInCategory.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemsInCategory;
    }

    public ArrayList<String> getItemInfo(String itemname) {
        ArrayList<String> itemInfo = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("select * from items where itemname=?");
            ps.setString(1, itemname);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                itemInfo.add(rs.getString(1));
                itemInfo.add(rs.getString(2));
                itemInfo.add(rs.getString(3));
                itemInfo.add(rs.getString(4));
                itemInfo.add(rs.getString(5));
                itemInfo.add(rs.getString(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemInfo;
    }

    public ArrayList<String> getItemInfoByID(int id) {
        ArrayList<String> itemInfo = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("select itemname,price from items where item_id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                itemInfo.add(rs.getString(1));
                itemInfo.add(rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemInfo;
    }

    public ArrayList<ArrayList<String>> cartInfo(ArrayList<Integer[]> itemIDs) {
        ArrayList<ArrayList<String>> cart = new ArrayList<>();

        try {
            for (int i = 0; i < itemIDs.size(); i++) {
                PreparedStatement ps = connection.prepareStatement("select item_id,itemname,price from items where item_id=?");
                ps.setInt(1, itemIDs.get(i)[0]);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    cart.add(new ArrayList<>());
                    cart.get(i).add(rs.getString(1));
                    cart.get(i).add(rs.getString(2));
                    cart.get(i).add(rs.getString(3));
                    cart.get(i).add(itemIDs.get(i)[1].toString());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }


    public ArrayList<String> getCategories() {
        return SQLQuery("select distinct category from items order by category collate nocase");
    }

    public void setConnection() {
        try {
            // create a database connection
            connection = DriverManager.getConnection(dataBaseName);
            connection.setAutoCommit(true);
            Statement statement = connection.createStatement();
            //Setting foreign keys constraint on
            statement.executeUpdate("pragma foreign_keys=on");
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e);
        }
    }

    public ArrayList<String> SQLQuery(String query) {
        ArrayList<String> strings = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                strings.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return strings;
    }


    public int placeOrder(ArrayList<Integer[]> cart, int customerID) {
        int orderID = 0;
        try {
            PreparedStatement ps = connection.prepareStatement("insert into orders(customer_id, order_date) values (?, strftime('%Y-%m-%d %H:%M:%S','now', 'localtime'))");
            ps.setInt(1, customerID);
            ps.executeUpdate();
            ps = connection.prepareStatement("SELECT last_insert_rowid()");
            ResultSet rs = ps.executeQuery();
            orderID = rs.getInt(1);
            for (int i = 0; i < cart.size(); i++) {
                ps = connection.prepareStatement("INSERT INTO ordered_items(order_id, item_id, quantity) VALUES(?,?,?)");
                ps.setInt(1, orderID);
                ps.setInt(2, cart.get(i)[0]);
                ps.setInt(3, cart.get(i)[1]);
                ps.executeUpdate();
                ps = connection.prepareStatement("UPDATE items SET stock=stock-? where item_id=?");
                ps.setInt(1, cart.get(i)[1]);
                ps.setInt(2, cart.get(i)[0]);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderID;

    }

    public boolean userExist(int customerID) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT id FROM customers where id=?");
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String displayCustomerName(int customerID) {
        String name = "";
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT firstname,lastname FROM customers where id=?");
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                name += rs.getString(1);

                name += " " + rs.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public int registerCustomer(ArrayList<String> customerInfo) {
        int customerID = 0;
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO customers(firstname, lastname, address, zipcode, city, email, phone)" +
                    "VALUES (?,?,?,?,?,?,?)");
            for (int i = 0; i < 7; i++) {
                ps.setString(i + 1, customerInfo.get(i));
            }
            ps.executeUpdate();
            ps = connection.prepareStatement("SELECT last_insert_rowid()");
            ResultSet rs = ps.executeQuery();
            customerID = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerID;
    }

    public ArrayList<ArrayList<String>> displayCustomerOrders(int customerID) {
        ArrayList<ArrayList<String>> orderIDs = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT order_id, order_date, shipped_date FROM orders where customer_id=? ORDER BY order_id");
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                orderIDs.add(new ArrayList<>());
                orderIDs.get(i).add(rs.getString(1));
                orderIDs.get(i).add(rs.getString(2));
                orderIDs.get(i).add(rs.getString(3));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderIDs;
    }

    public ArrayList<ArrayList<String>> displayOrderedItems(int orderID) {
        ArrayList<ArrayList<String>> orderIDs = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT item_id, quantity FROM ordered_items where order_id=? ORDER BY item_id");
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                orderIDs.add(new ArrayList<>());
                orderIDs.get(i).add(rs.getString(1));
                orderIDs.get(i).add(rs.getString(2));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderIDs;
    }

    public ArrayList<ArrayList<String>> allOrders() {
        ArrayList<ArrayList<String>> orderIDs = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM orders ORDER BY order_id");
            ResultSet rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                orderIDs.add(new ArrayList<>());
                orderIDs.get(i).add(rs.getString(1));
                orderIDs.get(i).add(rs.getString(2));
                orderIDs.get(i).add(rs.getString(3));
                orderIDs.get(i).add(rs.getString(4));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderIDs;
    }




}


