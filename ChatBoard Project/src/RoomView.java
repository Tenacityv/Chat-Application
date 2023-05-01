import javax.swing.*;
import java.sql.*;

public class RoomView {
    /*
    Upon entering the room, you must make it obvious to the user that they
    are now viewing a chat window. You do not need to show chat history.

    At this point, anything the user enters should be treated as a chat
    message. If the chat message starts with a forward slash (“/”), you should
    treat the message as a command and attempt to handle it instead. If the
    command is invalid, present a warning to the user.
     */

    protected static String user;
    protected static String room;
    protected static final String url = "jdbc:postgresql://localhost:5432/chatdatabase";
    protected static final String User = "postgres";
    protected static final String Pass = "aple26";


    public RoomView(String user, String room) {
        this.user = user;
        this.room = room;
    }

    public void add(String text) throws SQLException {
        Connection con = DriverManager.getConnection(url, User, Pass);
        Statement stmt = con.createStatement();

        String previousMessages = retrieve(this.room);
        String newText = "\n" + this.user + " : " + text;

        String insertText = previousMessages + newText;



        try {
            con.setAutoCommit(false);
            stmt = con.createStatement();

            String sql  = "DELETE from CHATS where ROOM = \'" + this.room + "\';";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO CHATS("
                    + "ROOM, MESSAGES) "
                    + "VALUES(\'" + this.room + "\', \'" + insertText + "\');";
            stmt.executeUpdate(sql);

            stmt.close();
            con.commit();
            con.close();

        } catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    private static String retrieve(String currRoom) throws SQLException {
        Connection con = DriverManager.getConnection(url, User, Pass);
        Statement stmt = con.createStatement();


        try {
            ResultSet rs = stmt.executeQuery( "SELECT * FROM CHATS;" );
            while ( rs.next() ) {
                String r = rs.getString("room");
                String m = rs.getString("messages");
                if (r.equals(currRoom)){
                    return m;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public String list(){
        /*
        Return a list of users currently in this chat room
         */
        return this.user;

    }


    public void history() throws SQLException {
         /*
        Print all past messages for the room
         */

        Connection con = DriverManager.getConnection(url, User, Pass);
        Statement stmt = con.createStatement();

        try {
            ResultSet rs = stmt.executeQuery( "SELECT * FROM CHATS;" );
            while ( rs.next() ) {
                String r = rs.getString("room");
                String m = rs.getString("messages");

                if (r.equals(this.room)){
                   System.out.println(m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
