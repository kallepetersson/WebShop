package view;

/**
 * Created by kallepetersson on 2016-12-18.
 */
public class Console {


    public void chooseUser(){
        System.out.println("1. Admin | 2. Customer");
    }

    public void loggedInAs(String user){
        System.out.println("[Logged in as " + user + "]");
    }

    public void adminCommand(){
        System.out.println("1. Add item to database | 2. Update item | 3. Remove item");
    }

    public void adminNewItem(String item){
        System.out.println("Enter " + item);
    }

    public void showItems(String item){
        System.out.print(item);
    }

}
