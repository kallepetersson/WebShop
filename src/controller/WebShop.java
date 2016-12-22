package controller;

import view.console;

import java.util.ArrayList;
import java.util.Scanner;

public class WebShop {

    console view = new console();
    model.QueryHandler model = new model.QueryHandler();

    private int itemId;
    private String itemName;
    private String itemCategory;
    private int itemPrice;
    private String itemInfo;
    private int itemStock;

    private static ArrayList<Integer[]> cart;

    public void startClient() {
        cart = new ArrayList<>();

        int customerID;
        model.setConnection();
        Scanner scan = new Scanner(System.in);

        int selected;

        while (true) {
            view.chooseUser();

            int user = scan.nextInt();

            loop:
            switch (user) {     // 1. Admin | 2. Customer

                case 1:     // Admin
                    view.loggedInAs("Admin");
                    view.adminCommand();

                    switch (scan.nextInt()) {   // 1. Add item | 2. Update item | 3. Remove item

                        case 1:     // Add item
                            setItemInfo();
                            model.createItem(itemId, itemName, itemCategory, itemPrice, itemInfo, itemStock);

                            break;
                        case 2:     // Update item

                            setItemInfo();
                            model.updateItem(itemId, itemName, itemCategory, itemPrice, itemInfo, itemStock);

                            break;
                        case 3:     // // Delete item

                            ArrayList<String> categories;
                            categories = model.getCategories();
                            view.displayAdminCategories(categories);

                            selected = scan.nextInt() - 1;

                            ArrayList<String> itemsInCategory = model.getItemsInCategory(categories.get(selected));
                            view.displayItemsInCategory(itemsInCategory);

                            selected = scan.nextInt() - 1;

                            model.deleteItem(itemsInCategory.get(selected));

                            break;
                    }
                    break;

                case 2:     // Customer
                    view.loginRegister();
                    selected = scan.nextInt();
                    switch (selected) {
                        case 9:
                            break loop;
                        case 1:
                            view.customerID();
                            selected = scan.nextInt();

                            if (model.userExist(selected)) {
                                view.loggedInAs(model.displayCustomerName(selected));
                                customerID = selected;
                            } else {
                                view.customerIdDontExist(selected);
                                break;
                            }

                            ArrayList<String> categories;
                            do {
                                while (true) {
                                    categories = model.getCategories();
                                    view.displayCategories(categories);

                                    selected = scan.nextInt() - 1;

                                    if (selected != 8 && selected != 7 && selected != 9) {
                                        break;
                                    }
                                    switch (selected) {
                                        case 9:
                                            break loop;
                                        case 7:
                                            ArrayList<ArrayList<String>> ordersInfo = model.displayCustomerOrders(customerID);
                                            for (int i = 0; i < ordersInfo.size(); i++) {
                                                int totalPrice = 0;

                                                view.displayLine();
                                                view.displayOrderInfo(ordersInfo.get(i));
                                                ArrayList<ArrayList<String>> orderItems = model.displayOrderedItems(Integer.valueOf(ordersInfo.get(i).get(0)));

                                                for (int j = 0; j < orderItems.size(); j++) {
                                                    ArrayList<String> itemInfo = model.getItemInfoByID(Integer.valueOf(orderItems.get(j).get(0)));
                                                    view.displayItemInfoPrevOrder(orderItems.get(j), itemInfo);
                                                    totalPrice += Integer.valueOf(itemInfo.get(1)) * Integer.valueOf(orderItems.get(j).get(1));
                                                }
                                                view.displayTotalPrice(totalPrice);
                                                if (i == ordersInfo.size() - 1) {
                                                    view.displayLine();
                                                }
                                            }
                                            break;
                                        case 8:
                                            if (cart.size() == 0) {
                                                view.noItemsInCart();
                                                break;
                                            } else {
                                                view.displayBag(model.cartInfo(cart));
                                            }
                                            view.checkoutRemoveContinue();
                                            selected = scan.nextInt();

                                            switch (selected) {
                                                //Checkout
                                                case 1:
                                                    view.thanksForOrder();
                                                    model.placeOrder(cart, customerID);
                                                    cart.clear();
                                                    break;
                                                //Remove item
                                                case 2:
                                                    view.itemIDRemoveFromCart();
                                                    selected = scan.nextInt();
                                                    removeItemFromCart(selected);
                                                    break;
                                                //Continue shopping
                                                case 3:
                                                    break;
                                            }
                                    }
                                }

                                ArrayList<String> itemsInCategory = model.getItemsInCategory(categories.get(selected));
                                view.displayItemsInCategory(itemsInCategory);

                                selected = scan.nextInt() - 1;

                                ArrayList<String> itemInfo = model.getItemInfo(itemsInCategory.get(selected));
                                view.displayItemInfo(itemInfo);

                                view.addCartGoBack();
                                selected = scan.nextInt();
                                switch (selected) {
                                    case 1:
                                        boolean found = false;
                                        for (int i = 0; i < cart.size(); i++) {
                                            if (cart.get(i)[0].equals(Integer.valueOf(itemInfo.get(0)))) {
                                                cart.get(i)[1]++;
                                                found = true;
                                                break;
                                            }
                                            found = false;
                                        }
                                        if (!found) {
                                            Integer[] temp = {Integer.valueOf(itemInfo.get(0)), 1};
                                            cart.add(temp);
                                        }
                                        break;
                                }
                            } while (selected != 0);

                        case 2:
                            view.newCustomer(model.registerCustomer(newCustomer()));
                    }

                    break;
                default:
                    return;

            }

        }
        //model.closeConnection();

    }

    public void setItemInfo() {

        Scanner scanInt = new Scanner(System.in);
        Scanner scanString = new Scanner(System.in);

        view.adminNewItem("item_id");
        this.itemId = scanInt.nextInt();

        view.adminNewItem("itemname");
        this.itemName = scanString.nextLine();

        view.adminNewItem("category");
        this.itemCategory = scanString.nextLine();

        view.adminNewItem("price");
        this.itemPrice = scanInt.nextInt();

        view.adminNewItem("info");
        this.itemInfo = scanString.nextLine();

        view.adminNewItem("stock");
        this.itemStock = scanInt.nextInt();
    }

    public ArrayList<String> newCustomer() {
        ArrayList<String> customerInfo = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        view.enterInfo("first name");
        customerInfo.add(scan.nextLine());
        view.enterInfo("last name");
        customerInfo.add(scan.nextLine());
        view.enterInfo("address");
        customerInfo.add(scan.nextLine());
        view.enterInfo("zip code");
        customerInfo.add(scan.nextLine());
        view.enterInfo("city");
        customerInfo.add(scan.nextLine());
        view.enterInfo("email");
        customerInfo.add(scan.nextLine());
        view.enterInfo("phone");
        customerInfo.add(scan.nextLine());
        return customerInfo;
    }

    public void removeItemFromCart(int id) {
        for (int i = 0; i < cart.size(); i++) {
            if (id == cart.get(i)[0]) {
                if (cart.get(i)[1] > 1) {
                    cart.get(i)[1]--;
                } else {
                    cart.remove(i);
                    break;
                }
            }
        }
    }


}
