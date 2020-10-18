package cr.ac.itcr.Chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * La clase cliente crea el socket para conectarse al servidor,
 * se reciben mensajes y se envían mensajes a un usuario en específico
 * @Author Juan Peña
 */
public class Cliente {
    final String localhost = "127.0.0.1";
    int puerto = 14500;
    boolean flag = true;
    private String name;
    private HashMap<String, String> chat = new HashMap<>();
    private DataInputStream in;
    private DataOutputStream out;
    JList list;
    JTextArea areaMensajes;
    private static Logger log = LoggerFactory.getLogger(Cliente.class);

    /**
     *
     * @param name nombre del cliente
     * @param list  lista donde se despliegan los clientes conectados
     * @param areaMensajes espacio de texto donde se despliega el historial de conversación
     * @throws IOException
     */
    public Cliente(String name, JList list, JTextArea areaMensajes) throws IOException {
        this.name = name;
        this.list = list;
        this.areaMensajes = areaMensajes;

        Socket socketCliente = new Socket(localhost, puerto);
        in = new DataInputStream(socketCliente.getInputStream());
        out = new DataOutputStream(socketCliente.getOutputStream());
        out.writeUTF(name);
        Thread thread = new Thread(() -> {
            while(flag) {
                try {
                    String message = in.readUTF();
                    processMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * Este método toma la información del remitente y su mensaje,
     * lo divide para ubicarlo en el hashmap chat
     * @param message
     */
    public void processMessage(String message){
        System.out.println("Processing message: " + message );
        String[] components = message.split("%", 2);
        //Se agrega un nuevo usuario en caso de que no se encuentre en lista
        if (components[0].equals("server") ){
            String[] personas = components[1].split(";");
            for (String persona: personas){
                if (!chat.containsKey(persona) && persona != name){
                    chat.put(persona, "");
                }
            }
            UploadElements();

        }else {
            //Se actualiza el chat de acuerdo con quien deseo hablar,
            // en la lista de chats se muestra el nombre de quien me escribió o a quien le escribo
            String historial = chat.get(components[0]);
            historial += components[0] +": " + components[1] + "\n";
            list.setSelectedIndex(index(chat.keySet(), components[0]));
            chat.put(components[0],historial );
            areaMensajes.setText("");
            areaMensajes.setText(historial);
        }

    }

    /**
     * Este método se encarga se conactenar los mensajes en una
     * conversación del chat cuando se envían
     * @param name nombre del usuario
     * @param message info enviada
     * @throws IOException
     */
    public void sendMessage(String name,String message) throws IOException {
        String historial = chat.get(name) + "Yo: " + message + "\n";
        chat.put(name, historial);
        out.writeUTF(name + "%" + message);
        out.flush();
        try{
            out.writeUTF(name + "%" + message);
            out.flush();
        }catch (IOException e){
            log.info("Ocurrió un error en la escritura del mensaje a enviar");
            throw new IOException(e);
        }
    }

    /**
     * Este método toma los nombres de los usuarios y sibirlos a la lista de conectados
     */
    public void UploadElements(){
        DefaultListModel listModel = new DefaultListModel();
        for (String key: chat.keySet()){
            System.out.println(key);
            listModel.addElement(key);
        }
        list.setModel(listModel);
    }

    public HashMap<String, String> getChat() {
        return chat;
    }

    /**
     * Este método busca el indice en la lista de conectados
     * del usuario con que se chatea para desplegar su conversación en el espacio del historial
     * @param keys son las llaves asociadas a los usuarios del chat
     * @param name nombre del remiente
     * @return
     */
    public int index(Set<String> keys, String name){
        int cont = 0;
        for(String key: keys){
            if (key.equals(name)){
                return cont;
            }
            cont ++;
        }
        return 0;
    }
}