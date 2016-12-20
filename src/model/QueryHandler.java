package model;

import java.sql.*;
import java.util.ArrayList;


public class QueryHandler {

    private static Connection connection = null;
    private static String dataBaseName = "jdbc:sqlite:webshop.db";


    public void createItem(int item_id, String itemname, String category, int price, String info, int stock) {
        // create Query
    }

    public void updateItem(int item_id, String itemname, String category, int price, String info, int stock) {
        // Update Query
    }

    public void deleteItem(String name) {
        // Delete Query
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

}


