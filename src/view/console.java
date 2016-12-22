package view;

import java.util.ArrayList;

public class console {

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

    public void displayCategories(ArrayList<String> categories) {
        System.out.print(" ");
        for (int i = 0; i < categories.size(); i++) {
            if (i == categories.size() - 1) {
                System.out.println(i + 1 + ". " + categories.get(i) + " | 8. Previous Orders | 9. Show Cart | 10. Exit ");
            } else {
                System.out.print(i + 1 + ". " + categories.get(i) + " | ");
            }
        }
    }

    public void displayAdminCategories(ArrayList<String> categories) {
        System.out.print(" ");
        for (int i = 0; i < categories.size(); i++) {
            if (i == categories.size() - 1) {
                System.out.println(i + 1 + ". " + categories.get(i) + " | 10. Exit ");
            } else {
                System.out.print(i + 1 + ". " + categories.get(i) + " | ");
            }
        }
    }


    public void displayItemsInCategory(ArrayList<String> itemsInCategory) {
        for (int i = 0; i < itemsInCategory.size(); i++) {
            if (i == itemsInCategory.size() - 1) {
                System.out.println(i + 1 + ". " + itemsInCategory.get(i) + " | 10. Exit");
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
        int totalPrice = 0;
        System.out.println("ID | Item Name | Price | Quantity");
        for (int i = 0; i < bag.size(); i++) {
            for (int j = 0; j < bag.get(i).size(); j++) {
                System.out.print(bag.get(i).get(j) + " ");
                if (j == 2) {
                    System.out.print("SEK ");
                    totalPrice += Integer.valueOf(bag.get(i).get(j));
                }
            }
            System.out.println();
        }
        System.out.println("Total Price: " + totalPrice + " SEK");


    }

    public void loginRegister() {
        System.out.println("1. Login | 2. Register Customer");
    }

    public void customerID() {
        System.out.print("Enter your customer ID: ");
    }

    public void enterInfo(String info) {
        System.out.print("Enter " + info + ": ");
    }

    public void displayLine() {System.out.println("---------------------------------------------------------------------");}

    public void displayOrderInfo(ArrayList<String> ordersInfo){
        System.out.println("ID: " + ordersInfo.get(0) + " | Order Date: " + ordersInfo.get(1) +
                " | Shipped Date: " + ordersInfo.get(2));
    }

    public void displayItemInfoPrevOrder(ArrayList<String> orderItems, ArrayList<String> itemInfo){
        System.out.println(orderItems.get(1) + " " + itemInfo.get(0) + " Price: " + itemInfo.get(1));

    }

    public void displayTotalPrice(int totalPrice){
        System.out.println("Total Price: " + totalPrice);
    }

    public void customerIdDontExist(int id){
        System.out.println("ID " + id + " doesn't exist in the database");
    }

    public void noItemsInCart(){
        System.out.println("You have no items in the cart");
    }

    public void checkoutRemoveContinue(){
        System.out.println("1. Checkout | 2. Remove Item | 3. Continue Shopping");
    }

    public void thanksForOrder(){
        System.out.println("Thanks for you order!");
    }

    public void itemIDRemoveFromCart(){
        System.out.println("Enter item id to be removed from the cart");
    }

    public void addCartGoBack(){
        System.out.println("1. Add to cart | 2. Go back to categories");
    }

    public void newCustomer(int customer){
        System.out.println("Your Customer id: " + customer);
    }

}