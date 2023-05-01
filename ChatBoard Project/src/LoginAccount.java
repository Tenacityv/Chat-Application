public class LoginAccount {
    protected static String user;
    protected static String pass;
    protected static String chatboard;

    public LoginAccount(String u, String p, String c){
        this.user = u;
        this.pass = p;
        this.chatboard = c;
    }

    public static String getUser(){
        return user;
    }

    public static String getPass(){
        return pass;
    }

    public static String getChatboard(){
        return chatboard;
    }

    public void setUser(String s){
        this.user = s;
    }

    public void setPass(String s){
        this.pass = s;
    }

    public void setChatboard(String s){
        this.chatboard = s;
    }

}
