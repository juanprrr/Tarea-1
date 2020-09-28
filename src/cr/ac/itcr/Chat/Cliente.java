package cr.ac.itcr.Chat;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

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
            String historial = chat.get(components[0]);
            historial += components[0] +": " + components[1] + "\n";

            chat.put(components[0],historial );
            areaMensajes.setText("");
            areaMensajes.setText(historial);
        }

    }
    public void sendMessage(String name,String message) throws IOException {
        String historial = chat.get(name) + "Yo: " + message + "\n";
        chat.put(name, historial);
        out.writeUTF(name + "%" + message);
        out.flush();
    }
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

}