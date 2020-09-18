import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args){
        ServerSocket servidor;
        Socket socket;
        DataInputStream in;
        DataOutputStream out;

        int puerto = 14500;
        try{
            servidor = new ServerSocket(puerto);
            System.out.println("Escuchando...");

            while(true){
                socket = servidor.accept(); //socket del cliente

                System.out.println("Cliente conectado");
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                String mensaje = in.readUTF();
                System.out.println(mensaje);
                out.writeUTF("Salu2 desde el servidor");

                socket.close();
                System.out.println("Cliente desconectado");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}