package controller;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by kallepetersson on 2016-12-19.
 */
public class WebShop {

    view.Console view = new view.Console();
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

        view.chooseUser();
        model.setConnection();
        Scanner scan = new Scanner(System.in);

        int user = scan.nextInt();
        int selected = -1;
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
                        Scanner itemToDelete = new Scanner(System.in);
                        model.deleteItem(itemToDelete.nextLine());

                        break;


                }


                break;

            case 2:     // Customer
                view.loggedInAs("Customer");
                ArrayList<String> categories;
                do {
                    while (true) {
                        categories = model.getCategories();
                        view.displayCategories(categories);

                        selected = scan.nextInt() - 1;

                        if (selected != 8) {
                            break;
                        }
                        switch (selected) {
                            case 8:
                                view.displayBag(model.cartInfo(cart));
                                if (cart.size() == 0) {
                                    System.out.println("You have no items in the cart");
                                    break;
                                }
                                System.out.println("1. Checkout | 2. Remove Item | 3. Continue Shopping");
                                selected = scan.nextInt();

                                switch (selected) {
                                    case 1:
                                        //TODO place order query
                                        break;
                                    case 2:
                                        System.out.println("Enter item id to be removed from the cart");
                                        selected = scan.nextInt();
                                        removeItemFromCart(selected);
                                        break;
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

                    System.out.println("1. Add to cart | 2. Go back to categories");
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

                break;
            default:
                return;

        }

        model.closeConnection();

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

    public void removeItemFromCart(int id) {
        for (int i = 0; i < cart.size(); i++) {
            if (id == cart.get(i)[0]) {
                if(cart.get(i)[1]>1){
                    cart.get(i)[1]--;
                }else {
                    cart.remove(i);
                    break;
                }
            }
        }
    }


}
