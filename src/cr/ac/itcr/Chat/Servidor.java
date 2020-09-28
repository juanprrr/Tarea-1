package cr.ac.itcr.Chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

public class Servidor {

    static final int puerto = 14500;
    private boolean flag = true;
    private HashMap<String, ClientHandler> usuarios = new HashMap<>();

    public Servidor() throws IOException {

        ServerSocket ss = new ServerSocket(puerto);
        System.out.println("Escuchando...");
        while (flag){
            Socket socket =  ss.accept(); //socket del cliente
            processConnection(socket);
        }
    }

    public void processConnection(Socket socket) throws IOException {

        DataInputStream in = new DataInputStream(socket.getInputStream());
        String name = in.readUTF();
        ClientHandler handler = new ClientHandler(socket, name, this);
        usuarios.put(name, handler);
        notifyClients();

    }

    public void processMessage (String name, String message) throws IOException {

        System.out.println("Processing message: " + message );
        String[] components = message.split("%");
        ClientHandler handler = usuarios.get(components[0]);
        if (handler != null){
            handler.sendMessage(name + "%" + components[1]);
        }

    }
    public void notifyClients() throws IOException {

        Set<String> keys = usuarios.keySet();
        String message = "server" + "%" + unifyKeys(keys);
        for (String key: keys){
            usuarios.get(key).sendMessage(message);
        }
    }
    public String unifyKeys(Set<String> keys){
        String set = "";
        for (String key: keys){
            set += key + ";";
        }
        set = set.substring(0, set.length()-1);
        return set;
    }
    public static void main(String[] args) throws IOException {
        Servidor servidor = new Servidor();
    }

}