import com.sun.tools.javac.Main;
import java.sql.*;
import java.util.Scanner;


public class InitialView {

    protected static LoginAccount loginAccount;
    protected static final String url = "jdbc:postgresql://localhost:5432/accountdatabase";
    protected static final String User = "postgres";
    protected static final String Pass = "aple26";

    public static void register() throws ClassNotFoundException, SQLException {
        /*
        The user should set a username and password. The username must be unique.
         */

        Scanner consoleRegister = new Scanner(System.in);

        String user = "";
        String pass = "";

        String query = "INSERT INTO users(username, password, chatboard) VALUES(?, ?, ?)";

        boolean redoUser = true;
        while (redoUser){
            System.out.print("Username: ");
            user = consoleRegister.next();
            System.out.print("Password: ");
            pass = consoleRegister.next();

            try (Connection con = DriverManager.getConnection(url, User, Pass);
                 PreparedStatement pst = con.prepareStatement(query)){

                pst.setString(1,user);
                pst.setString(2,pass);
                pst.setString(3,"");
                pst.executeUpdate();

                redoUser = false;
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("This username " + user + " already exists");
            }

        }
        // add try catch, connect to database

        System.out.println(user + " : " + pass);

    }

    public static boolean login() throws SQLException {
        /*
        If the login fails (invalid username/password), you must display an appropriate error message.
        If the login succeeds, you should show the next view (main view).

        Ensure user exists, if it does make sure the password matches user
         */


        Scanner consoleLogin = new Scanner(System.in);

        String user = "";
        String pass = "";


        Connection con = DriverManager.getConnection(url, User, Pass);
        Statement stmt = con.createStatement();

        boolean redoUser = true;
        while (redoUser) {
            System.out.print("Username: ");
            user = consoleLogin.next();

            System.out.print("Password: ");
            pass = consoleLogin.next();

            try {
                ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS;" );

                while ( rs.next() ) {
                    String u = rs.getString("username");
                    String p = rs.getString("password");
                    String c = rs.getString("chatboard");

                    if (u.equals(user) && p.equals(pass)){
                        redoUser = false;
                        System.out.println("Entering chat room...");
                        loginAccount = new LoginAccount(user,pass,c);
                        return true;
                    }
                }

                System.out.println("This username and password are not correct");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static LoginAccount getLoginAccount() {
        return loginAccount;
    }
}
