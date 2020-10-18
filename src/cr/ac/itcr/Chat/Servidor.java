package cr.ac.itcr.Chat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * Esta clase se encarga de abrir la conexion donde se
 * reciben las solicitudes de los usuarios,
 */
public class Servidor {
    private static Logger log = LoggerFactory.getLogger(Servidor.class);
    static final int puerto = 14500;
    private boolean flag = true;
    private HashMap<String, ClientHandler> usuarios = new HashMap<>();

    /**
     * Este método constructor abre el socket del servidor en el puerto 14500
     * donde los usuarios solicitan ingresar
     * @throws IOException
     */
    public Servidor() throws IOException {

        try{
            ServerSocket ss = new ServerSocket(puerto);
            log.debug("Listening connections...");
            while (flag){
                Socket socket =  ss.accept(); //socket del cliente
                processConnection(socket);
            }
        } catch (IOException ex) {
            log.info("Server in use...");
            log.warn(ex.getMessage(), ex);
        }
    }

    /**
     * Este método toma el socket del usuario que ingresa al server y registra
     * su entrada en el hashmap de usuarios
     * @param socket conexion del usuario
     * @throws IOException
     */
    public void processConnection(Socket socket) throws IOException {

        DataInputStream in = new DataInputStream(socket.getInputStream());
        String name = in.readUTF();
        ClientHandler handler = new ClientHandler(socket, name, this);
        usuarios.put(name, handler);
        notifyClients();

    }

    /**
     * Este método recibe el nombre de usuario y el mensaje para indicarle al handler cómo
     * extraer esos datos y subirlos al hashmap usuarios
     * @param name nombre de usuario
     * @param message mensaje enviado
     * @throws IOException
     */
    public void processMessage (String name, String message) throws IOException {

        System.out.println("Processing message: " + message );
        String[] components = message.split("%");
        ClientHandler handler = usuarios.get(components[0]);
        if (handler != null){
            handler.sendMessage(name + "%" + components[1]);
        } else throw new ArrayIndexOutOfBoundsException();

    }

    /**
     * Este método le indica al hash map de usuarios que
     * agregue sus nombres cada vez que se acepta uno nuevo
     * y convierte el formato del mensaje para que sea leido en el handler y lo procese
     * @throws IOException
     */
    public void notifyClients() throws IOException {

        Set<String> keys = usuarios.keySet();
        String message = "server" + "%" + unifyKeys(keys);
        for (String key: keys){
            usuarios.get(key).sendMessage(message);
        }
    }

    /**
     * Este metodo define cómo se van a separar los keys asociados a cada usuario
     * mediante su concatenación con el identificador
     * @param keys usuarios en línea
     * @return el set de entradas que recibe el
     */
    public String unifyKeys(Set<String> keys){
        StringBuilder set = new StringBuilder();
        for (String key: keys){
            set.append(key).append(";");
        }
        set = new StringBuilder(set.substring(0, set.length() - 1));
        return set.toString();
    }

}