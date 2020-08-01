package task_2_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer {

    private final int port;
    private List<ClientHandler> clients;
    private AuthService authService;
    private ExecutorService executor = Executors.newCachedThreadPool();

    public AuthService getAuthService(){
        return authService;
    }

    public MyServer(int port) {
        this.port = port;
        startWork();
    }

    public void startWork() {
        try (ServerSocket server = new ServerSocket(port)){
            authService = new BaseAuthService();
            clients = new ArrayList<>();
            while (true){
                System.out.println("Сервер подключён к порту: "+port);
                System.out.println("Сервер ожидает подключение");
                Socket socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(this, socket);
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Ошибка в работе сервера");
        }finally {
            if(authService != null){
//                executor.shutdownNow();
                authService.stop();
            }
        }
    }

    public synchronized boolean isNickFree(String nick){
        for (ClientHandler clientHandler: clients){
            if(clientHandler.getName().equals(nick)){
                return false;
            }
        }
        return true;
    }

    public synchronized void broadcastMsg(String msg){
        for(ClientHandler clientHandler : clients){
            clientHandler.sendMsg(msg);
        }
    }

    public synchronized void sendPrivateMsg(ClientHandler fromClientHandler, String nickReceiver, String msg){
        for(ClientHandler clientHandler: clients){
            if(clientHandler.getName().equals(nickReceiver)){
                clientHandler.sendMsg(String.format("от %s: %s", fromClientHandler.getName(), msg));
                fromClientHandler.sendMsg(String.format("отправлено %s: %s", nickReceiver, msg));
                return;
            }
        }
        fromClientHandler.sendMsg(String.format("Участника с ником %s нет в чате", nickReceiver));
    }

    public synchronized void unsubscribe(ClientHandler clientHandler){
        clients.remove(clientHandler);
    }

    public synchronized void subscribe(ClientHandler clientHandler){
        clients.add(clientHandler);
    }

    public ExecutorService getExecutor() {
        return executor;
    }
}

