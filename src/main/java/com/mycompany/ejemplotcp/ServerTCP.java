package com.mycompany.ejemplotcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Implementación del TRANSPORTE DE ESCUCHA (El "Mesero"). Sabe cómo escuchar en
 * un puerto y aceptar conexiones. Depende de un iProcesador (la "Cocina") para
 * manejar la lógica.
 */
public class ServerTCP implements iListener{

    private final iProcesador procesador; // El "enchufe" a la lógica (Cocina)
    private volatile boolean ejecutando = true;
    private ServerSocket serverSocket;

    /**
     * Constructor que inyecta la dependencia de la lógica.
     *
     * @param procesador La implementación de la lógica (sea ServidorBlackboard
     * o ProcesadorCliente).
     */
    public ServerTCP(iProcesador procesador) {
        if (procesador == null) {
            throw new IllegalArgumentException("El procesador no puede ser nulo");
        }
        this.procesador = procesador;
    }

    /**
     * Inicia el bucle de escucha en el puerto especificado.
     *
     * @param puerto El puerto en el que escuchar.
     * @throws IOException Si el puerto ya está en uso.
     */
    public void iniciar(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        System.out.println("[Listener " + puerto + "] Escuchando en el puerto " + puerto);

        while (ejecutando) {
            Socket socketCliente = null;
            DataInputStream in = null;
            DataOutputStream out = null;

            try {
                // 1. Acepta una conexión
                socketCliente = serverSocket.accept();
                String ipCliente = socketCliente.getInetAddress().getHostAddress();
                System.out.println("[Listener " + puerto + "] Conexión aceptada de: **" + ipCliente + "**");

                in = new DataInputStream(socketCliente.getInputStream());
                out = new DataOutputStream(socketCliente.getOutputStream());

                // 2. Lee el mensaje
                String msgRecibido = in.readUTF();
                System.out.println("[Listener " + puerto + "] Recibido de **" + ipCliente + "** <- " + msgRecibido);

                // 3. DELEGA LA LÓGICA
                // El "Mesero" (ServerTCP) pasa la comanda a la "Cocina" (iProcesador).
                // No sabe qué implementación de "Cocina" es.
                String respuesta = this.procesador.procesar(ipCliente, msgRecibido);

                // 4. Envía la respuesta
                out.writeUTF(respuesta);
                System.out.println("[Listener " + puerto + "] Enviado a **" + ipCliente + "** -> " + respuesta);

            } catch (SocketException e) {
                if (!ejecutando) {
                    System.out.println("[Listener " + puerto + "] Detenido limpiamente.");
                } else {
                    System.err.println("[Listener " + puerto + "] SocketException: " + e.getMessage());
                }
            } catch (IOException e) {
                if (ejecutando) {
                    e.printStackTrace();
                }
            } finally {
                // 5. Cierra la conexión actual (intermitente)
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (socketCliente != null) {
                        socketCliente.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } // Fin del while, vuelve a esperar
    }

    /**
     * Detiene el bucle de escucha y cierra el ServerSocket.
     */
    public void detener() throws IOException {
        ejecutando = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close(); // Esto interrumpirá el serverSocket.accept()
        }
    }
}
