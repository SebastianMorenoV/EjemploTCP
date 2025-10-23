package com.mycompany.ejemplotcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ListenerIntermitente {

    private int puerto;
    private iProcesador procesador; // El "qué hacer"
    private volatile boolean ejecutando = true;
    private ServerSocket serverSocket;

    public ListenerIntermitente(int puerto, iProcesador procesador) {
        this.puerto = puerto;
        this.procesador = procesador;
    }

    public void iniciar() throws IOException {
        serverSocket = new ServerSocket(puerto);
        System.out.println("[Listener " + puerto + "] Escuchando en el puerto " + puerto);

        while (ejecutando) {
            Socket socketCliente = null;
            DataInputStream in = null;
            DataOutputStream out = null;

            try {
                // 1. Acepta una conexión
                socketCliente = serverSocket.accept();
                
                // --- CAMBIO AQUÍ ---
                // Obtenemos la IP real del cliente que se conectó
                String ipCliente = socketCliente.getInetAddress().getHostAddress();
                
                System.out.println("[Listener " + puerto + "] Cliente conectado: **" + ipCliente + "**");

                in = new DataInputStream(socketCliente.getInputStream());
                out = new DataOutputStream(socketCliente.getOutputStream());

                // 2. Lee el mensaje
                String msgRecibido = in.readUTF();
                
                // --- CAMBIO AQUÍ ---
                System.out.println("[Listener " + puerto + "] Recibido de **" + ipCliente + "** <- " + msgRecibido);

                // 3. Pasa el mensaje Y LA IP al procesador para obtener una respuesta
                // --- CAMBIO AQUÍ ---
                String respuesta = procesador.procesar(ipCliente, msgRecibido);
                
                // 4. Envía la respuesta
                out.writeUTF(respuesta);
                System.out.println("[Listener " + puerto + "] Enviado -> " + respuesta);

            } catch (SocketException e) {
                if (!ejecutando) {
                    System.out.println("[Listener " + puerto + "] Detenido limpiamente.");
                } else {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 5. Cierra la conexión (INTERMITENTE)
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (socketCliente != null) socketCliente.close();
                    System.out.println("[Listener " + puerto + "] Conexión con cliente cerrada.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } // Fin del while, vuelve a esperar
    }

    public void detener() throws IOException {
        ejecutando = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close(); // Interrumpe el accept()
        }
    }
}