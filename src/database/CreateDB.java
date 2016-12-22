package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class CreateDB {

    private static Connection connection = null;
    private static String dataBaseName = "jdbc:sqlite:webshop.db";
    private static String itemsBackupPath = "./itemsimport.dmp";
    private static String customerBackupPath = "./customerimport.dmp";


    public static void main(String[] args) {
        setConnectionAndCreateDB(false);
        importItems();
        importCustomer();
        closeConnection();
        System.out.println("Database created!");
    }

    public static void setConnectionAndCreateDB(boolean dropTables) {
        try {
            // create a database connection
            connection = DriverManager.getConnection(dataBaseName);
            connection.setAutoCommit(true);
            Statement statement = connection.createStatement();
            //Setting foreign keys constraint on
            statement.executeUpdate("pragma foreign_keys=on");
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            if(dropTables){
                statement.executeUpdate("DROP TABLE IF EXISTS ITEMS");
                statement.executeUpdate("DROP TABLE IF EXISTS customers");

            }

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS items(" +
                    "item_id INTEGER PRIMARY KEY," +
                    "itemname TEXT NOT NULL," +
                    "category TEXT NOT NULL," +
                    "price INTEGER NOT NULL," +
                    "stock INTEGER NOT NULL," +
                    "description TEXT NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS customers(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "firstname TEXT NOT NULL," +
                    "lastname TEXT NOT NULL," +
                    "address TEXT NOT NULL," +
                    "zipcode TEXT NOT NULL," +
                    "city TEXT NOT NULL," +
                    "email TEXT NOT NULL," +
                    "phone TEXT NOT NULL)");


            statement.executeUpdate("CREATE TABLE IF NOT EXISTS orders(" +
                    "order_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "customer_id INTEGER NOT NULL," +
                    "order_date TEXT NOT NULL," +
                    "Shipped_date TEXT DEFAULT 'Processing'," +
                    "FOREIGN KEY (customer_id) REFERENCES customers(id) ON UPDATE CASCADE" +
                    ")");


            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ordered_items(" +
                    "order_id INTEGER NOT NULL," +
                    "item_id INTEGER NOT NULL," +
                    "quantity INTEGER NOT NULL,"+
                    "PRIMARY KEY(order_id, item_id)," +
                    "FOREIGN KEY (order_id) REFERENCES orders(order_id)," +
                    "FOREIGN KEY (item_id) REFERENCES items(item_id) ON UPDATE CASCADE" +
                    ")");

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    }

    public static void importItems() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(itemsBackupPath));

            String insertTableSQL = "INSERT INTO items"
                    + "(item_id, itemname, category, price, stock, description) VALUES"
                    + "(?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(insertTableSQL);

            String s = in.readLine();
            while (s != null) {
                String[] arr = s.split("\\|");
                for (int i = 0; i < arr.length; i++) {
                    ps.setString(1, arr[0]);
                    ps.setString(2, arr[1]);
                    ps.setString(3, arr[2]);
                    ps.setString(4, arr[3]);
                    ps.setString(5, arr[4]);
                    ps.setString(6, arr[5]);
                }
                ps.executeUpdate();
                s = in.readLine();
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }

    public static void importCustomer() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(customerBackupPath));

            String insertTableSQL = "INSERT INTO customers"
                    + "(firstname, lastname, address, zipcode, city, email, phone) VALUES"
                    + "(?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(insertTableSQL);
            String s = in.readLine();
            while (s != null) {
                String[] arr = s.split("\\|");
                for (int i = 0; i < arr.length; i++) {
                    ps.setString(1, arr[0]);
                    ps.setString(2, arr[1]);
                    ps.setString(3, arr[2]);
                    ps.setString(4, arr[3]);
                    ps.setString(5, arr[4]);
                    ps.setString(6, arr[5]);
                    ps.setString(7, arr[6]);
                }
                ps.executeUpdate();
                s = in.readLine();
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
    public static void SQLQuery(String query, int col, String msg) {
        try {

            long start = System.currentTimeMillis();

            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            System.out.println(msg);
            while (rs.next()) {
                for (int i = 1; i <= col; i++) {
                    if (i == col) {
                        System.out.println(rs.getString(i));
                    } else {
                        System.out.print(rs.getString(i) + " ");
                    }
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("SQL QueryTime: " + (end - start) + "ms\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void closeConnection() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e);
        }
    }

    public static void importToDB(){

    }


}
