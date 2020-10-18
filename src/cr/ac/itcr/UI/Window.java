package cr.ac.itcr.UI;
import cr.ac.itcr.Chat.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Window extends JFrame {
    DefaultListModel listModel = new DefaultListModel();
    JList listaChats = new JList(listModel);
    JButton Enviar = new JButton("Enviar");
    JTextArea areaMensajes = new JTextArea();
    JTextField textoChat = new JTextField();
    JTextField nombre = new JTextField("Nombre:");
    JTextField miNombre = new JTextField();
    JButton Login = new JButton("Login");
    Cliente cliente;
    String destinatario;
    private static Logger log = LoggerFactory.getLogger(Window.class);

    public Window() throws IOException {

        Widgets();

        setTitle("Mi Chat");
        setBounds(100,100, 390, 400);
        setLayout(null);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        int cont = 1;

        while (cont == 1){
            try{
                Servidor servidor = new Servidor();
                cont ++;
            } catch (IOException e){
                log.info("Servidor ya está ocupado");
                log.warn(e.getMessage(), e);
            }
        }

    }
    public void Widgets(){

        listaChats.setBounds(0,0, 120, 400);
        listaChats.setBackground(Color.GRAY);

        Enviar.setLocation(300,330);
        Enviar.setSize(70, 30);
        Enviar.setBackground(Color.lightGray);

        areaMensajes.setBounds(125, 25, 300, 300);
        areaMensajes.setBackground(Color.lightGray);

        textoChat.setBounds(125, 330, 170, 30);

        nombre.setBounds(125, 0 , 50, 20);
        nombre.setEditable(false);

        miNombre.setBounds(200, 0 , 60, 20);

        Login.setLocation(270,0);
        Login.setSize(70, 20);
        Login.setBackground(Color.WHITE);

        Enviar.addActionListener(e -> {
            String myMessage = textoChat.getText();
            if (myMessage != ""){
                try {
                    cliente.sendMessage(destinatario, myMessage);
                    areaMensajes.setText("");
                    areaMensajes.setText(cliente.getChat().get(destinatario));
                } catch (IOException ioException) {
                    log.info(ioException.getMessage(), ioException);
                }
            }
        });

        Login.addActionListener(e -> {
            String nombre = miNombre.getText();
            if(nombre != ""){
                try {
                    cliente = new Cliente(nombre, listaChats, areaMensajes);
                } catch (IOException io) {
                    log.warn(io.getMessage(), io);

                }
            } else log.info("No ingreso nombre"); log.error("NO puede entrar sin registrarse");
        });

        listaChats.addListSelectionListener(e -> {
            destinatario = listaChats.getSelectedValue().toString();
            areaMensajes.setText("");
            areaMensajes.setText(cliente.getChat().get(destinatario));

        });


        add(listaChats);
        add(Enviar);
        add(areaMensajes);
        add(textoChat);
        add(nombre);
        add(miNombre);
        add(Login);
    }
}
