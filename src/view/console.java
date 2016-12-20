package view;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import java.util.ArrayList;

/**
 * Created by kallepetersson on 2016-12-18.
 */
public class Console {


    public void chooseUser() {
        System.out.println("1. Admin | 2. Customer");
    }

    public void loggedInAs(String user) {
        System.out.println("[Logged in as " + user + "]");
    }

    public void adminCommand() {
        System.out.println("1. Add item to database | 2. Update item | 3. Remove item");
    }

    public void adminNewItem(String item) {
        System.out.println("Enter " + item);
    }

    public void showItems(String item) {
        System.out.print(item);
    }

    public void displayCategories(ArrayList<String> categories) {
        for (int i = 0; i < categories.size(); i++) {
            if (i == categories.size() - 1) {
                System.out.println(i + 1 + ". " + categories.get(i) + " | 9. Show Bag");
            } else {
                System.out.print(i + 1 + ". " + categories.get(i) + " | ");
            }
        }
    }

    public void displayItemsInCategory(ArrayList<String> itemsInCategory) {
        for (int i = 0; i < itemsInCategory.size(); i++) {
            if (i == itemsInCategory.size() - 1) {
                System.out.println(i + 1 + ". " + itemsInCategory.get(i));
            } else {
                System.out.print(i + 1 + ". " + itemsInCategory.get(i) + " | ");
            }
        }
    }

    public void displayItemInfo(ArrayList<String> itemInfo) {
        System.out.println(itemInfo.get(1));
        System.out.println("--------------------");
        System.out.println("Price: " + itemInfo.get(3));
        System.out.println("ID: " + itemInfo.get(0));
        System.out.println("In Stock: " + itemInfo.get(4));
        System.out.println(itemInfo.get(5));
    }


    public void displayBag(ArrayList<ArrayList<String>> bag) {
        int totalPrice=0;
        System.out.println("ID | Item Name | Price | Quanity");
        for (int i = 0; i < bag.size(); i++) {
            for (int j = 0; j < bag.get(i).size(); j++) {
                System.out.print(bag.get(i).get(j) + " ");
                if(j==2){
                    System.out.print("SEK ");
                    totalPrice += Integer.valueOf(bag.get(i).get(j));
                }
            }
            System.out.println();
        }
        System.out.println("Total Price: "+totalPrice+" SEK");


    }


}
