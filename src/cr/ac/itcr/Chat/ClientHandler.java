package cr.ac.itcr.Chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    Socket socket;
    boolean flag = true;
    DataOutputStream out;
    DataInputStream in;
    String name;
    Servidor server;

    public ClientHandler(Socket socket, String name, Servidor server) throws IOException {

        this.socket = socket;
        this.in = new DataInputStream (socket.getInputStream());
        this.out = new DataOutputStream (socket.getOutputStream());
        this.name = name;
        this.server = server;

        Thread t = new Thread(() -> {
            while(flag) {
                try {
                    String message = in.readUTF();
                    server.processMessage(name, message);
                } catch (EOFException e){
                    System.out.println("No hay mensaje");
                } catch (IOException e) {

                    try {
                        socket.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
    public void sendMessage(String message) throws IOException {

        out.writeUTF(message);
        out.flush();
    }

}
