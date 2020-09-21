package cr.ac.itcr.Cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        final String localhost = "127.0.0.1";
        int puerto = 14500;
        DataInputStream in;
        DataOutputStream out;

        try {
            Socket socketCliente = new Socket(localhost, puerto);
            in = new DataInputStream(socketCliente.getInputStream());
            out = new DataOutputStream(socketCliente.getOutputStream());

            out.writeUTF("Cliente a servidor: hola ");
            String mensaje = in.readUTF();
            System.out.println(mensaje);
            //socketCliente.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}