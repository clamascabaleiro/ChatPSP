/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Christian
 */
public class Servidor extends Thread {

     private Socket clientSocket;
    public static ArrayList<Cliente> clientes = new ArrayList<>();
    
    public Servidor(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    public static class Cliente extends Thread {
        InputStream is;
        OutputStream os;
        String nick;

        public Cliente(InputStream is, OutputStream os, String nick) {
            this.is = is;
            this.os = os;
            this.nick = nick;
        }
        
        @Override
        //Metodo run donde se conectan y se desconectan los clientes
        public void run() {
            try {
                String mensaje = nick + " se conectó";
                for (Cliente c : Servidor.clientes) {
                    c.os.write(mensaje.getBytes());
                }
            
                do {
                    String envio = "";
                    byte[] msg2 = new byte[100];
                    is.read(msg2);
                    mensaje = new String(msg2);
                    //Al poner bye el usuario abandona el chat
                    if(mensaje.contains("/bye")){
                        System.out.println(mensaje);
                        String salir = nick+" abandonó el chat";
                        for (Cliente c : Servidor.clientes) {
                            c.os.write(salir.getBytes());
                        }
                    }
                    else{
                        //Si el mensaje no es bye entonces el mensaje se envia.
                        envio = nick + ": " + mensaje;
                        System.out.println(envio);
                        for (Cliente c : Servidor.clientes) {
                            c.os.write(envio.getBytes());
                        }
                    }
                }while (mensaje.contains("/bye")==false);

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Conexión terminada");
        }
    }

    
    //Metodo donde contamos las conexiones activas con una variable contador para limitar el tamaño del chat-
    
    public static void main(String[] args) {
        int contador = 0;
        System.out.println("Creando socket servidor");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            System.out.println("Realizando el bind");
            InetSocketAddress addr = new InetSocketAddress("localhost", 5555);
            serverSocket.bind(addr);
        } catch (IOException e){
            e.printStackTrace();
        }
        
        System.out.println("Aceptando conexiones");
        //Mientras el contador sea menor que 3 que son las conexiones de los usuarios al servidor, seguira aceptando usuarios al ser mayor que 3 no dejara entrar.
        //Si es menor que 3 se inicia un hilo por cada cliente para que se comuniquen entre si.
        while (contador < 3) {
            try {
                Socket newSocket = serverSocket.accept();
                System.out.println("Conexión recibida");
                
                InputStream is = newSocket.getInputStream();
                OutputStream os = newSocket.getOutputStream();
            
                byte[] msg1 = new byte[50];
                is.read(msg1);
                String nick = new String(msg1);
                System.out.println(nick);
                Cliente hilo = new Cliente(is, os, nick);
                clientes.add(hilo);
                hilo.start();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
 
    
}
