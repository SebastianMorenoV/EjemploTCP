package com.mycompany.ejemplotcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

public class ServerTCP implements iProcesador {

    private int puerto;
    private iProcesador procesador; // El "qué hacer"
    private volatile boolean ejecutando = true;
    private ServerSocket serverSocket;

    public ServerTCP() {
    }

    public ServerTCP(int puerto, iProcesador procesador) {
        this.puerto  = puerto;
        this.procesador = procesador;
    }

    @Override
    public void iniciarServidor(int puerto) throws IOException {
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
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (socketCliente != null) {
                        socketCliente.close();
                    }
                    System.out.println("[Listener " + puerto + "] Conexión con cliente cerrada.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } // Fin del while, vuelve a esperar
    }

    @Override
    public String procesar(String ipCliente, String mensaje) {
//
//        String[] partes = mensaje.split(":");
//        String idCliente = partes[0];
//        String comando = partes[1];
//        String payload = (partes.length > 2) ? partes[2] : "";
//
//        // --- CAMBIO AQUÍ ---
//        // **BORRAMOS la línea 'String ipCliente = "localhost";'**
//        if (comando.equals("REGISTRAR")) {
//            int puertoCliente = Integer.parseInt(payload);
//
//            // --- CAMBIO AQUÍ ---
//            // Guardamos la IP y Puerto reales en el directorio
//            directorioClientes.put(idCliente, new ServidorBlackBoard.ClienteInfo(ipCliente, puertoCliente));
//            System.out.println("[Servidor] Cliente " + idCliente + " registrado en **" + ipCliente + ":" + puertoCliente + "**");
//            return "REGISTRADO";
//        }
//
//        // Simulación del juego
//        if (comando.equals("MOVER")) {
//            System.out.println("[Servidor] " + idCliente + " (" + ipCliente + ") hizo un movimiento. Notificando a todos...");
//
//            // Reenviar a todos los demás clientes (Broadcast)
//            // --- CAMBIO AQUÍ ---
//            for (Map.Entry<String, ServidorBlackBoard.ClienteInfo> entry : directorioClientes.entrySet()) {
//
//                if (entry.getKey().equals(idCliente)) {
//                    continue;
//                }
//
//                String idDestino = entry.getKey();
//                // --- CAMBIO AQUÍ ---
//                // Obtenemos la IP y Puerto del destinatario
//                ServidorBlackBoard.ClienteInfo infoDestino = entry.getValue();
//                String ipDestino = infoDestino.getHost();
//                int puertoDestino = infoDestino.getPuerto();
//
//                String msgUpdate = "[Servidor] Actualización: " + idCliente + " se movió.";
//
//                try {
//                    new Thread(() -> {
//                        try {
//                            // --- CAMBIO CRÍTICO AQUÍ ---
//                            // Enviamos a la IP y Puerto del DESTINO
//                            despachador.enviar(ipDestino, puertoDestino, msgUpdate);
//                        } catch (IOException e) {
//                            System.err.println("[Servidor] Error al notificar a " + idDestino);
//                        }
//                    }).start();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            return "MOVIMIENTO_RECIBIDO";
//        }
//
//        return "COMANDO_DESCONOCIDO";
        return null;
    }

    public void detener() throws IOException {
        ejecutando = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close(); // Interrumpe el accept()
        }
    }
    
    
}
