package task_chat_server;

import java.sql.*;

public class BaseAuthService implements AuthService{
    Connection conn = null;
    Statement stmt;
    PreparedStatement ps = null;

    public synchronized String changeNick(String nick, String newNick){
        try {
            String sqlRead = String.format("UPDATE Entries SET Nick = '%s' WHERE Nick = '%s' ", newNick, nick);
            stmt.executeUpdate(sqlRead);
            return newNick;
        } catch (SQLException throwables) {
            MyServer.getLOGGER().warn(throwables);
            return null;
        }
    }

    @Override
    public void start() {
        try {

            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:lesson.db");
            stmt = conn.createStatement();

            stmt.executeUpdate("CREATE TABLE if NOT EXISTS Entries (Login STRING UNIQUE NOT NULL, Pass STRING NOT NULL, Nick STRING UNIQUE NOT NULL)");

            MyServer.getLOGGER().info("Сервис аутентификации запущен");
        } catch (ClassNotFoundException | SQLException e) {
            MyServer.getLOGGER().warn(e);
        }
    }

    @Override
    public synchronized String getNickByLoginPass(String login, String pass) {
        String sqlRead = String.format("SELECT * FROM Entries WHERE login = '%s' AND pass = '%s'", login, pass);
        try(ResultSet rs = stmt.executeQuery(sqlRead)){
            return rs.getString("Nick");
        } catch (SQLException throwables) {
            MyServer.getLOGGER().warn(throwables);
            return null;
        }
    }

    @Override
    public void stop() {
        try {
            conn.close();
            stmt.close();
            ps.close();
            MyServer.getLOGGER().info("Сервис аутентификации остановлен");
        } catch (SQLException throwables) {
            MyServer.getLOGGER().warn(throwables);
        }
    }

    public BaseAuthService() {
        start();
    }

}
