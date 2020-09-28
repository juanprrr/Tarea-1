package cr.ac.itcr.UI;
import cr.ac.itcr.Chat.*;
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

    public Window(){

        Widgets();

        setTitle("Mi Chat");
        setBounds(100,100, 390, 400);
        setLayout(null);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
                    ioException.printStackTrace();
                }
            }
        });

        Login.addActionListener(e -> {
            String nombre = miNombre.getText();
            if(nombre != ""){
                try {
                    cliente = new Cliente(nombre, listaChats, areaMensajes);
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
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
