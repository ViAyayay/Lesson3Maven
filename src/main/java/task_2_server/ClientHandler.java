package task_2_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private MyServer myServer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private String name;

    public String getName(){
        return name;
    }

    public ClientHandler(MyServer myServer, Socket socket){
        try{
            this.myServer = myServer;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.name = "";
            myServer.getExecutor().execute(() ->{
                try {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                authentication();
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                        }
                    });
                    thread.setDaemon(true);
                    thread.start();
                    long endTimeMillis = System.currentTimeMillis() + (120 *1000);
                    while (thread.isAlive()) {
                        if (System.currentTimeMillis() > endTimeMillis) {
                            System.out.println("Time authentication out...");
                            closeConnection();
                            return;
                        }
                    }
                    readMessages();
                } catch (IOException e) {
                    System.out.println("disconnect");
//                    e.printStackTrace();
                } finally {
                    closeConnection();
                }

            });
        }catch (IOException e){
            throw new RuntimeException("Проблемы при создании обработчика клиента");
        }
    }

    public void authentication() throws IOException{
        while (true){
            String str = in.readUTF();
//            socket.setSoTimeout(120 * 1000);
            if(str.startsWith("/auth")){
                String[] parts = str.split("\\s");
                String nick = myServer.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                if(nick!= null){
                    if(myServer.isNickFree(nick)){
                        sendMsg("/authok "+nick);
                        name = nick;
                        myServer.broadcastMsg(name+" звшел в чат");
                        myServer.subscribe(this);
                        return;
                    }else {
                        sendMsg("Учетная запись уже используется");
                    }
                }else {
                    sendMsg("Неверный логин/пароль");
                }
            }
        }
    }

    public void readMessages() throws IOException{
        while (true){

            String strFromClient = in.readUTF();
//            socket.setSoTimeout(120*1000);
            if(strFromClient.startsWith("/")){
                if (strFromClient.equals("/end")){
                    break;
                }
                if (strFromClient.startsWith("/w ")){
                    String[] tokens = strFromClient.split("\\s");
                    String nick = tokens[1];
                    String msg = strFromClient.substring(4 + nick.length());
                    myServer.sendPrivateMsg(this, nick, msg);
                }
                if (strFromClient.startsWith("/chnick ")){
                    String[] tokens = strFromClient.split("\\s");
                    String newNick = myServer.getAuthService().changeNick(name, tokens[1]);
                    if(newNick == null){
                        sendMsg("/cnfail");
                    }else {
                        name = newNick;
                        sendMsg("/cnok "+name);
                    }
                }
                continue;
            }
            System.out.println("от " + name+": " + strFromClient);
            myServer.broadcastMsg(name  + ": " + strFromClient);
        }
    }

    public void sendMsg(String msg){
        try {
            out.writeUTF(msg);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        myServer.unsubscribe(this);
        myServer.broadcastMsg(name + " вышел из чата");
        try{
            in.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        try{
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
