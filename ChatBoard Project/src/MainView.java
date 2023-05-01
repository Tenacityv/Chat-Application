import com.sun.tools.javac.Main;
import java.sql.*;
import java.util.Scanner;


public class MainView {
    protected static final String url = "jdbc:postgresql://localhost:5432/accountdatabase";
    protected static final String User = "postgres";
    protected static final String Pass = "aple26";
    protected static String currRoom = "";


    public static boolean joinRoom(LoginAccount account) throws SQLException {
        /*
        Given a room name, “joins” the chat room if it exists. If it does not exist,
        present the user with an appropriate error message.
         */
        Scanner consoleRegister = new Scanner(System.in);
        String chatName = "";

        boolean redoChat = true;
        while (redoChat){
            System.out.print("Chatboard name: ");
            chatName = consoleRegister.next();
            boolean skip = false;

            for (int i = 0; i < chatName.length(); i++) {
                char character = chatName.charAt(i);

                if (!Character.isDigit(character) && !Character.isAlphabetic(character)){
                    skip = true;
                    break;
                }
            }

            if (skip){
                System.out.println("The chatboard name can only include digits or alphabetical letters");
                continue;
            }

            if (doesExistsChat(account.getUser(), chatName)){
                currRoom = chatName;
                return true;

            } else {
                System.out.println("The chatboard name does not exist");
                break;
            }
        }
        return false;
    }


    public static boolean createRoom(LoginAccount account) throws SQLException {
        /*
        Creates a new chat room and automatically joins it. The chat room
        name must be unique and only contain lowercase letters and numbers.
         */
        Scanner consoleRegister = new Scanner(System.in);
        String chatName = "";
        String query = "INSERT INTO users(username, password, chatboard) VALUES(?, ?, ?)";

        boolean redoChat = true;
        while (redoChat) {
            System.out.print("Create chatboard name: ");
            chatName = consoleRegister.next();

            boolean skip = false;

            for (int i = 0; i < chatName.length(); i++) {
                char character = chatName.charAt(i);

                if (!Character.isDigit(character) && !Character.isAlphabetic(character)) {
                    skip = true;
                    break;
                }

                if (Character.isUpperCase(character)){
                    skip = true;
                    break;
                }
            }

            if (skip) {
                System.out.println("The chatboard name can only include digits or lower-case alphabetical letters");
                continue;
            }

            if (doesExistsChat(account.getUser(), chatName)) {
                System.out.println("The chatboard name already exists");
                continue;
            }


            String insertChat = chatName + " " + account.getChatboard() + " ";
            currRoom = chatName;
            account.setChatboard(insertChat);


            Connection con = DriverManager.getConnection(url, User, Pass);
            Statement stmt = con.createStatement();


            con.setAutoCommit(false);
            stmt = con.createStatement();
            String sql = "UPDATE USERS set CHATBOARD  = \'" + insertChat + "\' where USERNAME = "
                    + "\'" + account.getUser() + "\';";

            stmt.executeUpdate(sql);

            con.commit();
            stmt.close();
            con.close();
            System.out.println("Room created");
            break;
        }
        return true;
    }

    public static String getCurrRoom(){
        return currRoom;
    }

    private static boolean doesExistsChat(String currUser, String newChat) throws SQLException {

        Connection con = DriverManager.getConnection(url, User, Pass);
        Statement stmt = con.createStatement();

        try {
            ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS;" );

            while ( rs.next() ) {
                String u = rs.getString("username");
                String c = rs.getString("chatboard");

                if (u.equals(currUser)){
                    if (c.length() == 0) {
                        return false;
                    } else {
                        String[] listChats = c.split( " ");

                        for (String chat : listChats){
                            if (chat.equals(newChat)){
                                return true;
                            }
                        }
                        return false;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    public static void updateAccount(LoginAccount account) throws SQLException {
        /*
         Give the user the ability to change their username or password.
         */

        Scanner consoleRegister = new Scanner(System.in);
        Connection con = DriverManager.getConnection(url, User, Pass);
        Statement stmt = con.createStatement();


        System.out.println("Do you wish to change your username, password, or both?");
        System.out.println("To update username type 1, to update password type 2, to update both type 3");
        boolean askAgain = true;

        while (askAgain) {
            String option = consoleRegister.nextLine();

            if (option.equals("1")) {
                System.out.println("Old Username: " + account.getUser());
                System.out.println(account.getChatboard());

                System.out.print("New Username: ");
                String newUser = consoleRegister.next();

                boolean chooseAgain = true;

                while (chooseAgain) {
                    if (!doesExists(newUser)){
                        try {
                            con.setAutoCommit(false);
                            stmt = con.createStatement();

                            String sql  = "DELETE from USERS where USERNAME = \'" + account.getUser() + "\';";
                            stmt.executeUpdate(sql);

                            sql = "INSERT INTO USERS("
                                    + "USERNAME,PASSWORD,CHATBOARD) "
                                    + "VALUES(\'" + newUser + "\', \'" + account.getPass() + "\', \'"
                                    + account.getChatboard() + "\');";
                            stmt.executeUpdate(sql);

                            account.setUser(newUser);


                            stmt.close();
                            con.commit();
                            con.close();


                            System.out.println("Account Updated");
                            chooseAgain = false;
                        } catch (Exception e){
                            e.printStackTrace();
                            System.err.println(e.getClass().getName() + ": " + e.getMessage());
                            System.exit(0);
                        }
                    } else {
                        System.out.println("That username already exists choose a new one");
                        System.out.print("New Username: ");
                        newUser = consoleRegister.next();
                    }
                }

                askAgain = false;

            } else if (option.equals("2")) {
                System.out.println("Old Password: " + account.getPass());

                System.out.print("New Password: ");
                String newPass = consoleRegister.next();

                con.setAutoCommit(false);
                stmt = con.createStatement();
                String sql = "UPDATE USERS set PASSWORD  = \'" + newPass + "\' where USERNAME = "
                        + "\'" + account.getUser() + "\';";

                stmt.executeUpdate(sql);

                account.setPass(newPass);

                con.commit();
                stmt.close();
                con.close();

                System.out.println("Password Updated");
                askAgain = false;

            } else if (option.equals("3")) {
                System.out.println("Old Username: " + account.getUser());
                System.out.println("Old Password: " + account.getPass());

                System.out.print("New Username: ");
                String newUser = consoleRegister.next();
                System.out.print("New Password: ");
                String newPass = consoleRegister.next();

                boolean chooseAgain = true;

                while (chooseAgain) {
                    if (!doesExists(newUser)){
                        try {
                            con.setAutoCommit(false);
                            stmt = con.createStatement();
                            String sql  = "DELETE from USERS where USERNAME = \'" + account.getUser() + "\';";
                            stmt.executeUpdate(sql);

                            sql = "INSERT INTO USERS("
                                    + "USERNAME,PASSWORD,CHATBOARD) "
                                    + "VALUES(\'" + newUser + "\', \'" + newPass + "\', \'"
                                    + account.getChatboard() + "\');";
                            stmt.executeUpdate(sql);

                            account.setUser(newUser);
                            account.setPass(newPass);

                            stmt.close();
                            con.commit();
                            con.close();

                            chooseAgain = false;
                        } catch (Exception e){
                            e.printStackTrace();
                            System.err.println(e.getClass().getName() + ": " + e.getMessage());
                            System.exit(0);
                        }

                    } else {
                        System.out.println("That username already exists chose a new one");
                        System.out.print("New Username: ");
                        newUser = consoleRegister.next();
                    }
                }

                System.out.println("Account Updated");
                askAgain = false;

            } else {
                System.out.println("Type 1, 2, or 3");
            }
        }

        System.out.println();

        /*
        con = DriverManager.getConnection(url, User, Pass);
        stmt = con.createStatement();

        ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS;" );

        while ( rs.next() ) {
            String u = rs.getString("username");
            String p = rs.getString("password");
            String c = rs.getString("chatboard");

            System.out.println("" + u + " : " + p + " : " + c);
        }
         */
    }

    private static boolean doesExists(String newUser) throws SQLException {

        Connection con = DriverManager.getConnection(url, User, Pass);
        Statement stmt = con.createStatement();

        try {
            ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS;" );

            while ( rs.next() ) {
                String u = rs.getString("username");

                if (u.equals(newUser)){
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void logOut(){
        /*
         Sign the user out and return to the initial view.
         */
    }
}
