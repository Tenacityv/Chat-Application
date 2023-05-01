import com.sun.tools.javac.Main;

import java.sql.*;
import java.util.Scanner;

public class ChatDriver {
    protected static LoginAccount account;


    public static void loginSetUp() throws SQLException, ClassNotFoundException {
        Scanner console = new Scanner(System.in);
        InitialView initialView = new InitialView();
        boolean loggedIn = false;

        System.out.println(" ");
        System.out.println("Please select from the following options");

        System.out.println("(R)egister, (L)ogin, (Q)uit");
        String option = console.next();


        while (true) {
            if (option.equalsIgnoreCase("R") || option.equals("Register")){
                initialView.register();
            } //ignoresCase()

            else if (option.equalsIgnoreCase("L") || option.equals("Login")){
                if (initialView.login()){
                   account = initialView.getLoginAccount();
                   chatSetUp(account);
                   break;
                }
            }

            else if (option.equalsIgnoreCase("Q") ||option.equals("Quit")){
                System.out.println("Thank you for joining the chat board");
                break;
            }


            System.out.println("-----------------------------------------");
            System.out.println("Please select from the following options");
            System.out.println("(R)egister, (L)ogin, (Q)uit");
            option = console.next();

        }
    }


    public static void chatSetUp(LoginAccount account) throws SQLException, ClassNotFoundException {
        Scanner console = new Scanner(System.in);
        MainView mainView = new MainView();
        System.out.println(" ");
        System.out.println("Please select from the following options");


        System.out.println("(J)oin, (C)reate, (A)ccount, (L)ogout");
        String option = console.next();


        while (true) {
            if (option.equalsIgnoreCase("J") || option.equals("Join")){
                if (mainView.joinRoom(account)){
                    roomSetup(account,mainView.getCurrRoom());
                    break;
                }
            }

            else if (option.equalsIgnoreCase("C") || option.equals("Create")){
                if (mainView.createRoom(account)){
                    roomSetup(account,mainView.getCurrRoom());
                    break;
                }
            }

            else if (option.equalsIgnoreCase("A") || option.equals("Account")){
                mainView.updateAccount(account);
            }

            else if (option.equalsIgnoreCase("L") ||option.equals("Logout")){
                System.out.println("Welcome back to the main room...");
                loginSetUp();
                break;
            }

            System.out.println("-----------------------------------------");
            System.out.println("Please select from the following options");
            System.out.println("(J)oin, (C)reate, (A)ccount, (L)ogout");
            option = console.next();
        }
    }


    public static void roomSetup(LoginAccount account,String room) throws SQLException, ClassNotFoundException {
        Scanner console = new Scanner(System.in);
        RoomView roomView = new RoomView(account.getUser(),room);
        System.out.println(" ");
        System.out.println("Welcome to " + room + " " + account.getUser());
        System.out.println("Type \'/help\' for help");
        String option = console.nextLine();
        Character first = option.charAt(0);


        while (true) {
            if (option.equals("/help")){

                System.out.println("Commands: /leave, /list, /history");
                String command = console.nextLine();

                if (command.equals("/list")){
                    System.out.println("People in the Room: " + roomView.list());
                }

                else if (command.equals("/leave")){
                    System.out.println("Welcome back to the chat room...");
                    chatSetUp(account);
                    break;
                }

                else if (command.equals("/history")){
                    roomView.history();
                }

                else {
                    System.out.println("Invalid command");
                }

            } else if (first == '/'){
                System.out.println("Invalid command");
            }
            else {
                roomView.add(option);
            }

            System.out.println("-----------------------------------------");
            System.out.println("Welcome to " + account.getChatboard() + " " + account.getUser());
            System.out.println("Type \'/help\' for help");
            option = console.nextLine();
            first = option.charAt(0);
        }

    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("\'Welcome to the Matrix...\'");
        loginSetUp();

    }
}
