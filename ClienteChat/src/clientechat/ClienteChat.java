/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientechat;

import static clientechat.JFrame1.nick;
import static clientechat.JFrame1.os;
import static clientechat.JFrame1.is;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Christian
 */
public class ClienteChat {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        try {
            //Introducimos nuestro nombre con la varible nick static previamente creada en el JFrame.
            nick = JOptionPane.showInputDialog("Escriba su nombre de usuario:");
            System.out.println("Creando socket cliente");
            //Creamos el socket para conectarnos con el servidor.
            Socket clienteSocket = new Socket();
            System.out.println("Estableciendo la conexión");
            
            InetSocketAddress addr = new InetSocketAddress("localhost", 5555);
            clienteSocket.connect(addr);
            
            //Creamos una variable de tipo JFrame y la ponemos visible para que nos muestre el form.
            JFrame1 chat = new JFrame1();
            chat.setVisible(true);
            chat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            
            //Utilizamos las variables estaticas previamente creadas en el Jframe para escribir el mensaje y recibirlo
            is = clienteSocket.getInputStream();
            os = clienteSocket.getOutputStream();
            
            os.write(nick.getBytes());
            //Mientras la variable que retorne el metodo recibirMensaje sea diferente que 1 en este caso 1 es abandonó
            //Mostrara el mensaje en cada de que sea 1 entonces se cierra.
            do {
                chat.recibirMensaje();
            } while(chat.recibirMensaje()!="1");
            
            chat.setVisible(false);
            System.exit(0);
            
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    
