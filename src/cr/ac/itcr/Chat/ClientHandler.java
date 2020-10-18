package cr.ac.itcr.Chat;

import cr.ac.itcr.UI.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

/**
 * Esta clase se encarga de controlar el tráfico de información entre el server y los usuarios
 * @Author Juan Peña
 */

public class ClientHandler {

    Socket socket;
    boolean flag = true;
    DataOutputStream out;
    DataInputStream in;
    String name;
    Servidor server;
    private static Logger log = LoggerFactory.getLogger(ClientHandler.class);
    /**
     * Este método constructor de la clase sostiene la conexión mediante el thread
     * donde se procesa el mensaje para saber a donde será enviado
     * @param socket conexión abierta del usuario
     * @param name nombre del usuario
     * @param server conexion en escucha del servidor
     * @throws IOException
     */
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
                } catch (IOException e){
                    log.debug("Server Reseted");
                    log.warn(e.getMessage(), e);
                }
            }
        });
        t.start();
    }

    /**
     * Este método envía la info contenida en el Stream de salida
     * @param message mensaje que se procesa en el server
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {

        try{
            out.writeUTF(message);
            out.flush();
        }catch (IOException e){
            throw new IOException(e);
        }

    }

}
