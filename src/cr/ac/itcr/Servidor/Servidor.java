package cr.ac.itcr.Servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    static final int puerto = 14500;

    public void Clientes(){
        ServerSocket servidor;
        Socket socket;
        DataInputStream in;
        DataOutputStream out;

        try{
            servidor = new ServerSocket(puerto);
            System.out.println("Escuchando...");

            while(true){
                socket = servidor.accept(); //socket del cliente

                in = new DataInputStream(socket.getInputStream());  //Crea un objeto para recibir mensajes del usuario
                OutputStream escribir = socket.getOutputStream(); //Objeto para mandar a escribir en el cliente
                out = new DataOutputStream(escribir);  //Aqui se escriben las cosas

                ServerWorker sw = new ServerWorker(socket ,in ,out);  //Parametros, la conexion , y los objetos de escritura/lectura
                sw.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        //Se crea una instancia de la clase Servidor
        Servidor servidor = new Servidor();
        servidor.Clientes();
    }
}