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

    public void startClient(){

        view.chooseUser();

        Scanner scan = new Scanner(System.in);

        int user = scan.nextInt();

        switch (user) {     // 1. Admin | 2. Customer

            case 1:     // Admin

                view.loggedInAs("Admin");
                view.adminCommand();

                switch (scan.nextInt()) {   // 1. Add item | 2. Update item | 3. Remove item

                    case 1:     // Add item

                        setItemInfo();
                        model.createItem(itemId,itemName,itemCategory,itemPrice,itemInfo,itemStock);

                        break;
                    case 2:     // Update item

                        setItemInfo();
                        model.updateItem(itemId,itemName,itemCategory,itemPrice,itemInfo,itemStock);

                        break;
                    case 3:     // // Delete item
                        Scanner itemToDelete = new Scanner(System.in);
                        model.deleteItem(itemToDelete.nextLine());

                        break;


                }




                break;

            case 2:     // Customer
                view.loggedInAs("Customer");
                ArrayList<String> items;

                items = model.getItems();

                for(int i = 1; i<=items.size();i++){
                    view.showItems(String.valueOf(i) + ". " + items.get(i-1) + " | ");
                }

                int device = scan.nextInt();
                System.out.println(device);





                break;

        }


    }


    public void setItemInfo(){

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



}
