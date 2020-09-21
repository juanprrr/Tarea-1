package cr.ac.itcr.Servidor;
import java.io.*;
import java.net.*;

public class ServerWorker extends Thread {

    Socket socket = null;
    DataInputStream dis = null;
    DataOutputStream dos = null;


    public ServerWorker(Socket socket, DataInputStream in, DataOutputStream out) {  //Constructor

        this.socket = socket;
        dis = in;
        dos = out;
    }

    public void run() {  //Esto es un metodo, que es lo que correra cada hilo del servidor

        System.out.println("Se acepto una nueva conexion desde : " + socket.getPort());
        try {

            String mensaje = dis.readUTF();  //Recibe un mensaje

            System.out.println(mensaje);  //Imprime el mensaje recibido
        } catch (Exception e) {

        }

    }
}
