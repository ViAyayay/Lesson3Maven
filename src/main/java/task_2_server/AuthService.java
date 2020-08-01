package task_2_server;

public interface AuthService {
    String changeNick(String nick, String newNick);
    void start();
    String getNickByLoginPass(String login, String pass);
    void stop();
}
